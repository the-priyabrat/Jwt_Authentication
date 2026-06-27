package com.com.pri_vrat.authentication.repository;

import com.com.pri_vrat.authentication.entity.AuthUserPk;
import com.com.pri_vrat.authentication.entity.UserAuth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthUserRepository extends JpaRepository<UserAuth, AuthUserPk> {
    public List<UserAuth> findByUserPrimaryKey_UserName(String userName);
    public List<UserAuth> findByUserPrimaryKey_Email(String email);
}
