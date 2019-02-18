package com.cloud.serverusersecurity.service;

import com.cloud.serverusersecurity.utils.BcryptUtil;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class CustomUserDetailsService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        if ("huang".equals(s)) {
            return new User(s, BcryptUtil.encode("123456"), Collections.singleton(new SimpleGrantedAuthority("admin")));
        } else {
            throw new UsernameNotFoundException(s);
        }
    }
}
