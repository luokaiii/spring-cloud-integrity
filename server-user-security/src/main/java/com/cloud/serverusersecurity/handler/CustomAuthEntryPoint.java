package com.cloud.serverusersecurity.handler;

import com.cloud.serverusersecurity.model.ResponseResult;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomAuthEntryPoint implements AuthenticationEntryPoint {

    /**
     * 自定义 EntryPoint 用于 token 校验失败返回信息
     */
    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e)
            throws IOException, ServletException {
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setContentType("application/json");

        ResponseResult result = ResponseResult.build(HttpServletResponse.SC_BAD_REQUEST, "Token校验失败");
        httpServletResponse.getWriter().print(result.toString());
        httpServletResponse.getWriter().flush();
    }
}
