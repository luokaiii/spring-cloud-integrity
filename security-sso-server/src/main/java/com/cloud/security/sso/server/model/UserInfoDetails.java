package com.cloud.security.sso.server.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * 自定义 UserDetails,将UserInfo中的信息与UserDetails对应
 */
public class UserInfoDetails extends UserInfo implements UserDetails {

    private String loginName;

    public UserInfoDetails() {
    }

    public UserInfoDetails(String loginName) {
        this.loginName = loginName;
        this.setRoles("DEFAULT");
    }

    /**
     * 提取用户权限，转换为Authorization
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();

        if (!getRoles().isEmpty()) {
            String[] roles = getRoles().split(",");
            for (String role : roles) {
                authorities.add(new SimpleGrantedAuthority(role));
            }
        }

        return authorities;
    }

    @Override
    public String getPassword() {
        return "******";
    }

    @Override
    public String getUsername() {
        return this.loginName;
    }

    @Override
    public boolean isAccountNonExpired() {
        // 账户是否过期
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        // 账户是否被锁
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // 身份是否过期
        return true;
    }

    @Override
    public boolean isEnabled() {
        // 账户是否可用
        return true;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }
}
