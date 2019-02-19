package com.cloud.serverusersecurity.config;

import com.cloud.serverusersecurity.handler.CustomAccessDeniedHandler;
import com.cloud.serverusersecurity.handler.CustomAuthEntryPoint;
import com.cloud.serverusersecurity.handler.CustomWebResponseExceptionTranslator;
import com.cloud.serverusersecurity.service.CustomUserDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

import javax.sql.DataSource;
import java.util.concurrent.TimeUnit;

/**
 * 认证服务器配置
 */
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {
    private static final Logger logger = LoggerFactory.getLogger(AuthorizationServerConfig.class);

    private final RedisConnectionFactory redisConnectionFactory;

    private final AuthenticationManager authenticationManager;

    private final CustomAuthEntryPoint customAuthEntryPoint;

    private final CustomAccessDeniedHandler customAccessDeniedHandler;

    private final CustomUserDetailsService customUserDetailsService;

    private final DataSource dataSource;

    private final CustomWebResponseExceptionTranslator customWebResponseExceptionTranslator;

    @Autowired
    public AuthorizationServerConfig(CustomAccessDeniedHandler customAccessDeniedHandler,
                                     CustomAuthEntryPoint customAuthEntryPoint,
                                     RedisConnectionFactory redisConnectionFactory,
                                     AuthenticationManager authenticationManager,
                                     CustomUserDetailsService customUserDetailsService,
                                     DataSource dataSource,
                                     CustomWebResponseExceptionTranslator customWebResponseExceptionTranslator) {
        this.customAccessDeniedHandler = customAccessDeniedHandler;
        this.customAuthEntryPoint = customAuthEntryPoint;
        this.redisConnectionFactory = redisConnectionFactory;
        this.authenticationManager = authenticationManager;
        this.customUserDetailsService = customUserDetailsService;
        this.dataSource = dataSource;
        this.customWebResponseExceptionTranslator = customWebResponseExceptionTranslator;
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.allowFormAuthenticationForClients()
                .checkTokenAccess("isAuthenticated")
                .tokenKeyAccess("permitAll()")
                .authenticationEntryPoint(customAuthEntryPoint)
                .accessDeniedHandler(customAccessDeniedHandler);
        logger.info("AuthorizationServerSecurityConfigurer is complete!");
    }

    /**
     * 配置客户端详情信息(Client Details)
     * ClientId: 表示客户的ID
     * secret：客户端安全码(需要值得信任的客户端)
     * scope：限制客户端的访问范围，为空则为全部访问
     * authorizedGrantTypes：此客户端可以使用的授权类型，默认为空
     * authorities：此客户端可以使用的权限
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.withClientDetails(clientDetails());
        logger.info("ClientDetailsServiceConfigurer is complete!");
    }

    /**
     * 配置授权、令牌的访问端点和令牌服务
     * tokenStore：采用redis储存
     * authenticationManager：身份认证管理器，用于"password"授权模式
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints
                .authenticationManager(authenticationManager)
                .userDetailsService(customUserDetailsService)
                .tokenServices(tokenServices())
                .exceptionTranslator(customWebResponseExceptionTranslator);
    }

    /**
     * 指定Token的存储位置为 Redis
     */
    @Bean
    public TokenStore redisTokenStore() {
        return new RedisTokenStore(redisConnectionFactory);
    }

    /**
     * 客户端信息配置在数据库
     */
    @Bean
    public ClientDetailsService clientDetails() {
        return new JdbcClientDetailsService(dataSource);
    }

    /**
     * 采用 ras 加密 jwt
     */
//    @Bean
//    public JwtAccessTokenConverter jwtAccessTokenConverter() {
//        KeyStoreKeyFactory keyFactory = new KeyStoreKeyFactory(new ClassPathResource("hq-jwt.jks"), "hq940313".toCharArray());
//        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
//        converter.setKeyPair(keyFactory.getKeyPair("hq-jwt"));
//        return converter;
//    }

    /**
     * 配置生成 token 的有效期以及存储方式
     */
    @Bean
    public DefaultTokenServices tokenServices() {
        DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        defaultTokenServices.setTokenStore(redisTokenStore());
//        defaultTokenServices.setTokenEnhancer(jwtAccessTokenConverter());
        defaultTokenServices.setSupportRefreshToken(true);
        defaultTokenServices.setAccessTokenValiditySeconds((int) TimeUnit.MINUTES.toSeconds(30));
        defaultTokenServices.setRefreshTokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(1));
        return defaultTokenServices;
    }
}
