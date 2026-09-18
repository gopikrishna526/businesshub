package com.businesshub.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.businesshub.entity.BusinessEntity;

public interface BusinessRepository extends JpaRepository<BusinessEntity, Long> {
	
	boolean existsByIdAndOwner_Email(Long id, String email);
	
	Optional<BusinessEntity> findByIdAndOwner_Email(Long id, String email);
	
}
