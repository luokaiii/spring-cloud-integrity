package com.cloud.serverusersecurity.service;

import com.cloud.serverusersecurity.model.RoleName;
import com.cloud.serverusersecurity.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 用户信息，通过扩展 UserDetails 来实现自己的服务
 * {@link UserDetails} 包含用户登录之后的认证信息
 */
public class UserInfoDetails extends User implements UserDetails {

    public UserInfoDetails(String username) {
        super.setUsername(username);
    }

    /**
     * 将用户存储的权限提取为认证信息中的权限
     * 判断的权限可以通过 {@link org.springframework.security.access.prepost.PreAuthorize()} 来验证
     *
     * @return 用户权限 GrantedAuthority
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<RoleName> roles = getRoles();

        final List<GrantedAuthority> authorities = new ArrayList<>();
        for (RoleName role : roles) {
            authorities.add(new SimpleGrantedAuthority(role.toString()));
        }

        return authorities;
    }

    @Override
    public String getPassword() {
        return "******";
    }

    @Override
    public String getUsername() {
        return super.getUsername();
    }

    /**
     * 账户是否过期
     * 验证第三步
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * 账户是否被锁
     * 验证第二步
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * 用户凭证 Credential 是否过期
     * 验证第四步
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * 账户是否被禁用，禁用的账户无法进行身份验证
     * 验证第一步
     */
    @Override
    public boolean isEnabled() {
        return super.getEnabled();
    }
}

