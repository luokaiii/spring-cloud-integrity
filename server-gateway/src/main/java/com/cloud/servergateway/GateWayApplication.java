package com.cloud.servergateway;

import com.cloud.servergateway.filter.PreRequestZuulFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/**
 * EnableOAuth2Sso、EnableZuulProxy
 * 当启动Zuul的反向代理时，就可以向下转发OAuth2访问令牌到Zuul代理的服务中
 * spring cloud security features:
 * 1. 在 Zuul proxy 中传递 SSO Tokens
 * 2. 资源服务器之间的传递tokens
 * 3. feign 客户端拦截器行为，如 OAuthRestTemplate
 * 4. 在 Zuul proxy 配置下游认证
 */
@SpringBootApplication
@EnableOAuth2Sso
@EnableZuulProxy
public class GateWayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GateWayApplication.class, args);
    }

    @Bean
    public PreRequestZuulFilter preRequestZuulFilter() {
        return new PreRequestZuulFilter();
    }

    /**
     * 同步客户端的调用，简化了Http协议，用于服务间的调用
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
