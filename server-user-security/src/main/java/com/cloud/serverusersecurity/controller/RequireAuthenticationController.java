package com.cloud.serverusersecurity.controller;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
public class RequireAuthenticationController {

    private final static Logger logger = LoggerFactory.getLogger(RequireAuthenticationController.class);

    // 包含当前请求的缓存数据
    private RequestCache requestCache = new HttpSessionRequestCache();

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    /**
     * 需要认证时，跳转到这里
     */
    @RequestMapping("/authentication/require")
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity requireAuthentication(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 取到请求缓存的请求路径
        SavedRequest savedRequest = requestCache.getRequest(request, response);
        String url = savedRequest.getRedirectUrl();

        if (url != null) {
            logger.info("引发跳转的路径是：" + url);
            if (StringUtils.endsWith(url, ".html")) {
                redirectStrategy.sendRedirect(request, response, url);
            }
        }
        return ResponseEntity.ok("访问的服务需要身份认证，请引导用户到登录页面");
    }
}
