package com.cloud.security.sso.client.config;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

/**
 * 资源服务器的配置
 * todo 资源服务器开启之后，就无法进行鉴权操作
 */
//@Configuration
//@EnableResourceServer
public class ResourceServer extends ResourceServerConfigurerAdapter {

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
//                .requestMatchers().antMatchers("/**")
//                .and()
                .authorizeRequests()
                .antMatchers("/user").permitAll()
                .anyRequest().authenticated();
    }
}
