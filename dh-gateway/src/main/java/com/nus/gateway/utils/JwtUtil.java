package com.nus.gateway.utils;


import com.nus.common.result.Result;
import com.nus.common.result.ResultUtil;
import com.nus.dhmodel.pojo.User;
import com.nus.gateway.vo.UserVO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;
import com.nus.common.constant.JwtConstant;

/**
 * JWT工具类
 * @author: zealon
 * @since: 2020/4/14
 */
public class JwtUtil {

    /**
     * 身份认证
     * @param jwt 令牌
     * @return 成功状态返回200，其它均为失败
     */
    public static Result<UserVO> validationToken(String jwt) {
        try {
            //解析JWT字符串中的数据，并进行最基础的验证
            Claims claims = Jwts.parser()
                .setSigningKey(JwtConstant.SECRET_KEY)
                .parseClaimsJws(jwt)
                .getBody();
            UserVO user = new UserVO();
            user.setId(Long.parseLong(claims.get("id").toString()));
//            user.setPassword(claims.get("password").toString());
            user.setUsername(claims.get("username").toString());
            user.setIsAdmin(Integer.parseInt(claims.get("isAdmin").toString()));
            return ResultUtil.success(user);
        } catch (ExpiredJwtException e) {
            // 已过期令牌
            return ResultUtil.authExpired();
        } catch (SignatureException e) {
            // 伪造令牌
            return ResultUtil.unAuthorized();
        } catch (Exception e) {
            // 系统错误
            return ResultUtil.unAuthorized();
        }
    }

    public static void main(String[] args){
        String jwt = "eyJhbGciOiJIUzI1NiJ9.eyJleHAiOjE1ODkxMTcyNzcsImxvZ2luTmFtZSI6IjAwIiwibmlja05hbWUiOiIwMCIsImhlYWRJbWdVcmwiOiJodHRwOi8vcTk0aXN3ejM3LmJrdC5jbG91ZGRuLmNvbS9kZWZhdWx0LmpwZyIsInV1aWQiOiIyMjk0YzBiNmM2Y2Y0MjI1YWRmNTRhNzE2YzNmNDNiZSJ9.diWIKZPpefc5nfGa7S2u2eYIWbF1TYO-MUTHqQCtCFs";
        Result<UserVO> jwtValid = validationToken(jwt);
        if (jwtValid.getCode() == 200) {
            System.out.println(jwtValid.getData().toString());
        }
    }
}
