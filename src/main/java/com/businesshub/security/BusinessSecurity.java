package com.businesshub.security;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.businesshub.repository.BusinessRepository;

@Component
public class BusinessSecurity {

	private final BusinessRepository businessRepository;

	public BusinessSecurity(BusinessRepository businessRepository) {
		this.businessRepository = businessRepository;
	}

	public boolean isOwner(Long businessId, Authentication authentication) {

		String email = authentication.getName();

		return businessRepository.existsByIdAndOwner_Email(businessId, email);
	}
}