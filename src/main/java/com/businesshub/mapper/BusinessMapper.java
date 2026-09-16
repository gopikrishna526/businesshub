package com.businesshub.mapper;

import org.springframework.stereotype.Component;

import com.businesshub.dto.BusinessRequestDTO;
import com.businesshub.dto.BusinessResponseDTO;
import com.businesshub.entity.BusinessEntity;

@Component
public class BusinessMapper {

	public BusinessEntity toEntity(BusinessRequestDTO request) {

		BusinessEntity business = new BusinessEntity();

		business.setBusinessName(request.getBusinessName());
		business.setEmail(request.getEmail());
		business.setPhone(request.getPhone());
		business.setAddress(request.getAddress());

		return business;
	}

	public BusinessResponseDTO toResponseDTO(BusinessEntity business) {

		return new BusinessResponseDTO(business.getId(), business.getBusinessName(), business.getEmail(),
				business.getPhone(), business.getAddress());
	}
}