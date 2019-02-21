package com.cloud.security.sso.server.repository;

import com.cloud.security.sso.server.model.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserInfoRepository extends JpaRepository<UserInfo, Integer> {

    Optional<UserInfo> findByPhoneOrEmail(String phone, String email);
}
