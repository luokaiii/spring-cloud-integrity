package com.cloud.serverusersecurity.model;

import lombok.Data;

import java.util.Arrays;
import java.util.List;

@Data
public class User {

    /**
     * 用户名
     */
    private String username = "zhangsan";

    /**
     * 密码
     */
    private String password = "e10adc3949ba59abbe56e057f20f883e";

    /**
     * 权限
     */
    private List<RoleName> roles = Arrays.asList(RoleName.PLATINA, RoleName.GOLD);

    /**
     * 锁定
     */
    private Boolean enabled = true;
}