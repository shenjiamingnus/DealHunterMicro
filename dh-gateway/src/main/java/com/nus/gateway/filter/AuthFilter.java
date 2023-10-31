package com.nus.gateway.filter;

import com.alibaba.fastjson.JSONObject;
import com.nus.common.result.HttpCodeEnum;
import com.nus.common.result.Result;
import com.nus.gateway.config.SystemPropertiesConfig;
import com.nus.gateway.utils.JwtUtil;
import com.nus.gateway.vo.UserVO;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 身份认证过滤器
 * @author: zealon
 * @since: 2020/4/12
 */
@Component
public class AuthFilter implements GlobalFilter, Ordered {

    @Autowired
    private SystemPropertiesConfig systemPropertiesConfig;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 白名单Path
        Set<String> whiteList = this.getWhiteList();
        String path = exchange.getRequest().getPath().toString();

      // 白名单接口、开放接口放行
//        if (bookMatch || whiteList.contains(path)) {
        if (whiteList.contains(path)) {
            return chain.filter(exchange);
        }
        if (Objects.requireNonNull(exchange.getRequest().getMethod()).equals(HttpMethod.GET)){
            String token = exchange.getRequest().getHeaders().getFirst("Authorization");
            if (token != null){
              return getMono(exchange, chain, token);
            }
            return chain.filter(exchange);
        }

        String[] segments = path.split("/");
        if (!whiteList.contains(segments[1])) {
            // 认证
            String token = exchange.getRequest().getHeaders().getFirst("Authorization");
            return getMono(exchange, chain, token);
        }
        return chain.filter(exchange);
    }

  private Mono<Void> getMono(ServerWebExchange exchange, GatewayFilterChain chain, String token) {
      Result<UserVO> result = JwtUtil.validationToken(token);
      if (result.getCode() == HttpCodeEnum.OK.getCode()) {
          UserVO user = result.getData();
          // 追加请求头用户信息
          Consumer<HttpHeaders> httpHeaders = httpHeader -> {
            httpHeader.set("userId", user.getId().toString());
            httpHeader.set("username", user.getUsername());
            httpHeader.set("isAdmin", user.getIsAdmin().toString());
          };
          ServerHttpRequest serverHttpRequest = exchange.getRequest()
              .mutate()
              .headers(httpHeaders)
              .build();
          exchange.mutate().request(serverHttpRequest).build();
          return chain.filter(exchange);
      }

      // 认证过期、失败，均返回401
      ServerHttpResponse response = exchange.getResponse();
      byte[] bits = JSONObject.toJSONString(result).getBytes(StandardCharsets.UTF_8);
      DataBuffer buffer = response.bufferFactory().wrap(bits);
      response.setStatusCode(HttpStatus.UNAUTHORIZED);
      return response.writeWith(Mono.just(buffer));
  }

  @Override
    public int getOrder() {
        return 1;
    }

    /**
     * 请求白名单
     * @return
     */
    private Set<String> getWhiteList(){
        String whitelists = this.systemPropertiesConfig.getWhitelist();
        if (StringUtils.isEmpty(whitelists)) {
            return new HashSet<>();
        }
        Set<String> whiteList = new HashSet<>();
        String[] whiteArray = whitelists.split(",");
        for (int i = 0; i < whiteArray.length; i++) {
            whiteList.add(whiteArray[i]);
        }
        return whiteList;
    }
}
