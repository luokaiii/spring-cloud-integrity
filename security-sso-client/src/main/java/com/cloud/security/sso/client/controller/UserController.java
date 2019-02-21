package com.cloud.security.sso.client.controller;

import com.cloud.security.sso.client.model.UserInfo;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @GetMapping
    public ResponseEntity user(Authentication user) {
        return ResponseEntity.ok(user);
    }

    @PostMapping
    public ResponseEntity register(UserInfo userInfo) {
        return ResponseEntity.ok("注册:" + userInfo.getPhone());
    }
}
