package com.cloud.serverusersecurity.controller;

import com.netflix.discovery.converters.Auto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class UserController {

    private final TokenStore tokenStore;

    @Autowired
    public UserController(@Qualifier("redisTokenStore") TokenStore tokenStore) {
        this.tokenStore = tokenStore;
    }

    @GetMapping("/user")
    public Principal user(Principal user) {
        return user;
    }

    @RequestMapping("/oauth/remove_token")
    public ResponseEntity removeToken(@RequestParam("token") String token) {
        if (token != null) {
            OAuth2AccessToken accessToken = tokenStore.readAccessToken(token);
            tokenStore.removeAccessToken(accessToken);
        } else {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }
}
