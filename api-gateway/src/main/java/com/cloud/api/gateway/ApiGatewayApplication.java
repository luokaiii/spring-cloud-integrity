package com.cloud.api.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

/**
 * 由API-Gateway进行统一认证，并向下进行token转发
 */
@SpringBootApplication
@EnableZuulProxy // 开启Zuul代理，并向下转发Security
@EnableDiscoveryClient // 服务启动时向Eureka服务中心进行注册
@EnableOAuth2Sso // 开启SSO，在路由网关上的sso，应该是和其他网关之间的sso，因为由网关代理的服务都被zuul转发了
//@EnableFeignClients
public class ApiGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApplication.class, args);
    }
}
