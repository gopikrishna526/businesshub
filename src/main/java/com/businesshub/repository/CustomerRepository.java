package com.businesshub.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.businesshub.entity.CustomerEntity;

public interface CustomerRepository extends JpaRepository<CustomerEntity, Long> {
	List<CustomerEntity> findByBusinessId(Long businessId);
}
