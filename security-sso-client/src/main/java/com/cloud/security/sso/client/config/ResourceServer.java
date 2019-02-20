package com.cloud.security.sso.client.config;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

//@Configuration
//@EnableResourceServer
public class ResourceServer extends ResourceServerConfigurerAdapter {

    @Override
    public void configure(HttpSecurity http) throws Exception {
//        http.csrf().disable();
//        http.headers().cacheControl().disable();
//        http.authorizeRequests().antMatchers("/**").permitAll();
    }
}
