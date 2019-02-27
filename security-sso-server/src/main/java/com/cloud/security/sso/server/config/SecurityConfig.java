package com.cloud.security.sso.server.config;

import com.cloud.security.sso.server.handler.CustomPasswordEncoder;
import com.cloud.security.sso.server.service.SsoUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.data.repository.query.SecurityEvaluationContextExtension;

/**
 * SpringSecurity安全配置
 */
@EnableWebSecurity
@Order(2) // Security与ResourceServer 都是以拦截器的方式处理请求，因此需要为这两个拦截器定义执行顺序
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private SsoUserDetailsService userDetailsService;

    @Bean
    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    /**
     * 密码校验逻辑，可以通过实现 PasswordEncoder 来实现自己的校验逻辑
     *
     * @return 加密器
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new CustomPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .formLogin()
                .and()
                .authorizeRequests().anyRequest().permitAll();
//                .antMatchers("/**").permitAll()
//                .anyRequest().authenticated();
    }

    /**
     * 在 WebSecurityConfigurerAdapter 中声明 SecurityEvaluationContextExtension
     * 即可为 SpringSecurity 开启 Spring Data
     * 详见 https://www.baeldung.com/spring-data-security
     *
     * @return
     */
    @Bean
    public SecurityEvaluationContextExtension securityEvaluationContextExtension() {
        return new SecurityEvaluationContextExtension();
    }
}
