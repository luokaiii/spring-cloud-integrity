package com.cloud.serverusersecurity.config;

import com.cloud.serverusersecurity.utils.MD5Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 自定义密码加密方式
 */
@Slf4j
public class CustPasswordEncoder implements PasswordEncoder {
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
