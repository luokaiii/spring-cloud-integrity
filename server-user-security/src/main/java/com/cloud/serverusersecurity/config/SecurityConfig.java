package com.cloud.serverusersecurity.config;

import com.cloud.serverusersecurity.handler.CustomAuthcFailureHandler;
import com.cloud.serverusersecurity.handler.CustomAuthcSuccessHandler;
import com.cloud.serverusersecurity.service.LoginUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

/**
 * Spring Security 安全配置类
 *  配置请求拦截、密码校验方式、过滤规则等
 */
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final LoginUserService loginUserService;

    private final CustomAuthcFailureHandler authcFailureHandler;

    private final CustomAuthcSuccessHandler authcSuccessHandler;

    private final DataSource dataSource;

    @Autowired
    public SecurityConfig(LoginUserService loginUserService,
                          CustomAuthcFailureHandler authcFailureHandler,
                          CustomAuthcSuccessHandler authcSuccessHandler,
                          DataSource dataSource) {
        this.loginUserService = loginUserService;
        this.authcFailureHandler = authcFailureHandler;
        this.authcSuccessHandler = authcSuccessHandler;
        this.dataSource = dataSource;
    }

    /**
     * 通过向 PersistentTokenRepository 中注入 数据源，来持久化用户登录时生成的token
     * {@link JdbcTokenRepositoryImpl#CREATE_TABLE_SQL} 用于创建数据库表，以存储用户令牌的默认SQL
     *
     * @return 持久化 Token 令牌
     */
    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        tokenRepository.setDataSource(dataSource);
        // 在项目启动时区执行创建表的操作(只在第一次执行，否则会报错)
//        tokenRepository.setCreateTableOnStartup(true);
        return tokenRepository;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
//                .addFilterBefore(validateCodeFilter, UsernamePasswordAuthenticationFilter.class)
//                .httpBasic() // 使用basic的方式登录
                .formLogin() // 使用 form 表单登录
                .loginPage("/authentication/require")   // 指定登录的页面
                .loginProcessingUrl("/authentication/login")   // 指定登录请求的url
                .successHandler(authcSuccessHandler) // 使用自定义的登录成功及失败处理器
                .failureHandler(authcFailureHandler)
                .and()
                .rememberMe() // 添加 RememberMe功能
                .tokenRepository(persistentTokenRepository()) // 指定要使用的 token repository
                .tokenValiditySeconds(1800) // 允许令牌的有效时间
                .userDetailsService(loginUserService)
                .and()
                .authorizeRequests()  // 处理请求
                .antMatchers("/authentication/require"  // 过滤登录请求、图形验证码
                        , "/signIn.html",
                        "/code/image").permitAll()
                .anyRequest() // 匹配任意请求
                .authenticated() // 需要认证
                .and()
                .csrf().disable(); // 如果不做csrf防护的话，会抛出403异常：Could not verify the provided CSRF token because your session was not found.
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring()
                .antMatchers("/favor.ico");
    }

    @Bean
    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    /**
     * 自定义验证规则
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) {

        auth.authenticationProvider(authenticationProvider());
    }

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new CustPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(loginUserService);
        provider.setPasswordEncoder(passwordEncoder());
        provider.setHideUserNotFoundExceptions(false);
        return provider;
    }
}
