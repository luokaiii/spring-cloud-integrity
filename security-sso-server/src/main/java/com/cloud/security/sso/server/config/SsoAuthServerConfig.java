package com.cloud.security.sso.server.config;

import com.cloud.security.sso.server.handler.CustomPasswordEncoder;
import com.cloud.security.sso.server.service.SsoUserDetailsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

/**
 * 认证服务器配置
 */
@Slf4j
@Configuration
@EnableAuthorizationServer
public class SsoAuthServerConfig extends AuthorizationServerConfigurerAdapter {

    private PasswordEncoder passwordEncoder = new CustomPasswordEncoder();

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private SsoUserDetailsService userDetailsService;


    /**
     * 使用 JDBC 的方式管理 Client 实例，注意表结构
     *  详见 {@link JdbcClientDetailsService}
     */
//    @Bean
//    public JdbcClientDetailsService clientDetailsService(DataSource dataSource){
//        return new JdbcClientDetailsService(dataSource);
//    }


    /**
     * 客户端相关配置
     * 添加哪些客户端可以进行Token申请，以及客户端的相关配置
     * <p>
     * 授权登录的方式，共4+1种：
     * refresh_token - RefreshTokenGranter 刷新token
     * authorization_code - AuthorizationCodeTokenGranter 授权码模式
     * implicit - ImplicitTokenGranter 简化模式，即直接返回token，否则需要先返回授权码，再通过授权码获得token
     * password - ResourceOwnerPasswordTokenGranter 密码模式
     * client_credentials - ClientCredentialsTokenGranter
     *
     * @param clients x
     * @throws Exception x
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
//        clients.withClientDetails(clientDetailsService(dataSource));
        log.info("实例初始密码为：" + passwordEncoder.encode("clientSecret"));
        clients.inMemory()
                .withClient("client")
                // 这个有个非常大的问题: 即 Spring-Security-OAuth2 依赖和 Spring-Cloud-Starter-OAuth2 依赖的区别：
                //   1. SpringSecurityOAuth2 直接填写客户端密码即可
                //   2. Cloud 版本则需要将密码进行 PasswordEncoder，且该配置需要与Security中配置相同
                .secret("clientSecret")
                .authorizedGrantTypes("authorization_code", "refresh_token", "password")
//                .redirectUris("http://localhost:8080/client1/login")
                .scopes("all");
//                .and()
//                .withClient("client2")
//                .secret("clientSecret")
//                .authorizedGrantTypes("authorization_code", "refresh_token", "password")
////                .redirectUris("http://localhost:8060/client2/login")
//                .scopes("all");
    }

    /**
     * 认证服务器的安全配置
     * 访问TokenKey时，是否需要身份认证，默认是全部拒绝的
     *
     * @param security x
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) {
        security
                .allowFormAuthenticationForClients() // 允许对客户端进行表单身份验证
                .tokenKeyAccess("permitAll()")
                .checkTokenAccess("isAuthenticated()");
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        endpoints
                .tokenStore(tokenStore())
                .userDetailsService(userDetailsService)
                .authenticationManager(authenticationManager);
//                .accessTokenConverter(jwtAccessTokenConverter());
    }

    /**
     * todo 使用 Jwt 作为验签方式时，TokenGranter 只有四种类型，缺少了一种 password，why
     */
//    @Bean
//    public TokenStore jwtTokenStore() {
//        return new JwtTokenStore(jwtAccessTokenConverter());
//    }
    @Bean
    public TokenStore tokenStore() {
        return new RedisTokenStore(redisConnectionFactory);
    }

    /**
     * Jwt的签名，拥有签名就可以解析token
     */
//    @Bean
//    public JwtAccessTokenConverter jwtAccessTokenConverter() {
//        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
//        converter.setSigningKey("koral");
//        return converter;
//    }
}
