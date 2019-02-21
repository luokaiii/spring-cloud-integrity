package com.cloud.security.sso.client.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "user_info")
public class UserInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String profile;

    private String sex;

    private String signature;

    private String phone;

    private String roles; // 用户权限

    private Boolean locked; // 锁定

    @Email
    private String email;

    private String password;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
