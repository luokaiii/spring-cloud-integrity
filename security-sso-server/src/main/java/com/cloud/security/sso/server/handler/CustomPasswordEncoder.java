package com.cloud.security.sso.server.handler;

import com.cloud.security.sso.server.util.MD5Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;

@Slf4j
public class CustomPasswordEncoder implements PasswordEncoder {
    @Override
    public String encode(CharSequence rawPassword) {
        return MD5Utils.md5(rawPassword.toString());
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        log.info("登录密码：" + rawPassword + ",数据库密码：" + encodedPassword);
        return rawPassword.toString().equalsIgnoreCase(encodedPassword);
    }
}
