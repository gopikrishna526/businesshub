package com.businesshub.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.businesshub.entity.BusinessEntity;

public interface BusinessRepository extends JpaRepository<BusinessEntity, Long> {

}
