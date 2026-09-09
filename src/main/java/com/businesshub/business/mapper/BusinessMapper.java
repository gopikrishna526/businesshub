package com.businesshub.business.mapper;

import org.springframework.stereotype.Component;

import com.businesshub.business.Business;
import com.businesshub.business.dto.BusinessRequestDTO;
import com.businesshub.business.dto.BusinessResponseDTO;

@Component
public class BusinessMapper {

	public Business toEntity(BusinessRequestDTO request) {

		Business business = new Business();

		business.setBusinessName(request.getBusinessName());
		business.setEmail(request.getEmail());
		business.setPhone(request.getPhone());
		business.setAddress(request.getAddress());

		return business;
	}

	public BusinessResponseDTO toResponseDTO(Business business) {

		return new BusinessResponseDTO(business.getId(), business.getBusinessName(), business.getEmail(),
				business.getPhone(), business.getAddress());
	}
}