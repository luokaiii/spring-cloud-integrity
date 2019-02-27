package com.cloud.security.sso.server.service;

import com.cloud.security.sso.server.model.UserInfo;
import com.cloud.security.sso.server.model.UserInfoDetails;
import com.cloud.security.sso.server.repository.UserInfoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

/**
 * 自定义UserDetailsService
 */
@Slf4j
@Component
public class SsoUserDetailsService implements UserDetailsService {
    /**
     * 注入User的仓库，用来做身份认证
     */
    private final UserInfoRepository userInfoRepository;

    @Autowired
    public SsoUserDetailsService(UserInfoRepository userInfoRepository) {
        this.userInfoRepository = userInfoRepository;
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
        UserInfo userInfo = userInfoRepository.findByPhoneOrEmail(username, username)
                .orElse(null);

        if (userInfo == null)
            log.info("抱歉，为找到您的身份信息!");

        log.info("查询用户信息: {},开始装载权限.", username);

        return new UserInfoDetails(username);
    }
}
