package com.cloud.security.sso.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * 自定义UserDetailsService
 */
@Component
public class SsoUserDetailsService implements UserDetailsService {

    /**
     * PasswordEncoder 在 SecurityConfig 中已经声明为Bean 了，所以这里可以直接注入
     */
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public SsoUserDetailsService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * 在这里做登录的校验，以及设置用户的权限信息
     * 也可以通过实现 UserDetails，来使用自己的User服务(例如从数据库存取权限，增加自定义属性等)
     *
     * @param username 用户的唯一标识(如手机号、邮箱等)
     * @return 包含权限信息的对象
     * @throws UsernameNotFoundException 未查找到用户时抛出的异常
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // $2a$10$zwZkpFeUHe181WCkJ5lrLupw6sk6TFwKXMuLRHST/aOQgHab8BQFG
        return new User(username, passwordEncoder.encode("123456")
                , AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_USER"));
    }
}
