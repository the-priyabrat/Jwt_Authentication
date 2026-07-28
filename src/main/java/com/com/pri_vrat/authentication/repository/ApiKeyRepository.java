package com.com.pri_vrat.authentication.repository;

import com.com.pri_vrat.authentication.entity.ApiKeyDetails;
import com.com.pri_vrat.authentication.entity.ApiPrimaryKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApiKeyRepository extends JpaRepository<ApiKeyDetails, ApiPrimaryKey> {
    public List<ApiKeyDetails> findByApiKey(String apiKey);

    public List<ApiKeyDetails> findByPrimaryKey_UserNameAndPrimaryKey_TenantId(String userName, String tenantId);
}
