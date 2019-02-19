package com.cloud.servergateway.controller;

import com.cloud.servergateway.handler.ResponseResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.security.oauth2.OAuth2ClientProperties;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Collections;

@RestController
@RequestMapping("/hq")
public class LoginController {
    public static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    private final OAuth2ClientProperties oAuth2ClientProperties;

    private final OAuth2ProtectedResourceDetails oAuth2ProtectedResourceDetails;

    private final RestTemplate restTemplate;

    private final TokenStore tokenStore;

    @Autowired
    public LoginController(OAuth2ClientProperties oAuth2ClientProperties,
                           OAuth2ProtectedResourceDetails oAuth2ProtectedResourceDetails,
                           RestTemplate restTemplate,
                           @Qualifier("redisTokenStore") TokenStore tokenStore) {
        this.oAuth2ClientProperties = oAuth2ClientProperties;
        this.oAuth2ProtectedResourceDetails = oAuth2ProtectedResourceDetails;
        this.restTemplate = restTemplate;
        this.tokenStore = tokenStore;
    }

    class User {

    }

    @PostMapping(value = "/login")
    public ResponseResult login(@RequestParam("username") String username,
                                @RequestParam("password") String password) {
        // 验证用户名密码 todo 完善User实体,同时这里是通过service查询的

        return null;
    }

    @RequestMapping("/logout")
    public ResponseResult exit(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");
        // 退出逻辑
        return null;
    }

    @PostMapping(value = "/hello")
    public ResponseEntity hello() {
        return ResponseEntity.ok("hello");
    }

    public ResponseEntity<OAuth2AccessToken> getToken(String username, String password) {
        // Http Basic验证
        String clientAndSecret = oAuth2ClientProperties.getClientId() + ":" + oAuth2ClientProperties.getClientSecret();
        clientAndSecret = "Basic" + Base64.getEncoder().encodeToString(clientAndSecret.getBytes());
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", clientAndSecret);
        // 授权请求信息
        LinkedMultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("username", Collections.singletonList(username));
        map.put("password", Collections.singletonList(password));
        map.put("grant_type", Collections.singletonList(oAuth2ProtectedResourceDetails.getGrantType()));
        map.put("scope", oAuth2ProtectedResourceDetails.getScope());
        // HttpEntity
        HttpEntity<LinkedMultiValueMap<String, String>> httpEntity = new HttpEntity<>(map, httpHeaders);
        // 获取Token
        return restTemplate.exchange(oAuth2ProtectedResourceDetails.getAccessTokenUri(), HttpMethod.POST, httpEntity, OAuth2AccessToken.class);
    }
}
