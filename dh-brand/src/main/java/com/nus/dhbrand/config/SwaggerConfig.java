//package com.nus.dhbrand.config;
//
//import com.alibaba.nacos.shaded.com.google.common.collect.Lists;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.annotation.Order;
//import springfox.documentation.builders.ApiInfoBuilder;
//import springfox.documentation.builders.PathSelectors;
//import springfox.documentation.builders.RequestHandlerSelectors;
//import springfox.documentation.service.ApiInfo;
//import springfox.documentation.service.ApiKey;
//import springfox.documentation.service.AuthorizationScope;
//import springfox.documentation.service.SecurityReference;
//import springfox.documentation.spi.DocumentationType;
//import springfox.documentation.spi.service.contexts.SecurityContext;
//import springfox.documentation.spring.web.plugins.Docket;
//import java.util.List;
//import springfox.documentation.swagger2.annotations.EnableSwagger2;
//
//@Configuration
//@EnableSwagger2
//public class SwaggerConfig {
//
//  @Bean(value = "userApi")
//  @Order(value = 1)
//  public Docket groupRestApi() {
//    return new Docket(DocumentationType.SWAGGER_2)
//        .apiInfo(groupApiInfo())
//        .select()
//        .apis(RequestHandlerSelectors.any())
//        .paths(PathSelectors.any())
//        .build();
//  }
//
//  private ApiInfo groupApiInfo(){
//    return new ApiInfoBuilder()
//        .title("swagger好用！！！")
//        .description("<div style='font-size:14px;color:red;'>swagger-bootstrap-ui-demo RESTful APIs</div>")
//        .termsOfServiceUrl("http://www.group.com/")
//        .version("1.0")
//        .build();
//  }
//
//  private ApiKey apiKey() {
//    return new ApiKey("BearerToken", "Authorization", "header");
//  }
//  private ApiKey apiKey1() {
//    return new ApiKey("BearerToken1", "Authorization-x", "header");
//  }
//
//  private SecurityContext securityContext() {
//    return SecurityContext.builder()
//        .securityReferences(defaultAuth())
//        .forPaths(PathSelectors.regex("/.*"))
//        .build();
//  }
//  private SecurityContext securityContext1() {
//    return SecurityContext.builder()
//        .securityReferences(defaultAuth1())
//        .forPaths(PathSelectors.regex("/.*"))
//        .build();
//  }
//
//  List<SecurityReference> defaultAuth() {
//    AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
//    AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
//    authorizationScopes[0] = authorizationScope;
//    return Lists.newArrayList(new SecurityReference("BearerToken", authorizationScopes));
//  }
//  List<SecurityReference> defaultAuth1() {
//    AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
//    AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
//    authorizationScopes[0] = authorizationScope;
//    return Lists.newArrayList(new SecurityReference("BearerToken1", authorizationScopes));
//  }
//}
//
