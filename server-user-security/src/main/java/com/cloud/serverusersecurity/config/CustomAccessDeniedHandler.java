package com.cloud.serverusersecurity.config;

import com.cloud.serverusersecurity.model.ResponseResult;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    /**
     * 授权失败时返回的信息
     */
    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {
        // 返回json格式数据
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setContentType("application/json");

        ResponseResult result = ResponseResult.build(HttpServletResponse.SC_BAD_REQUEST, "未授权");
        httpServletResponse.getWriter().print(result.toString());
        httpServletResponse.getWriter().flush();
    }
}
