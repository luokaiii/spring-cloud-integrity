package com.cloud.servergateway;

import com.cloud.servergateway.filter.PreRequestZuulFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
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
