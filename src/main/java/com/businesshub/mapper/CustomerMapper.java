package com.businesshub.mapper;

import org.springframework.stereotype.Component;

import com.businesshub.dto.CustomerRequestDTO;
import com.businesshub.dto.CustomerResponseDTO;
import com.businesshub.entity.CustomerEntity;

@Component
public class CustomerMapper {

	public CustomerEntity toEntity(CustomerRequestDTO request) {

	    CustomerEntity customer = new CustomerEntity();

	    customer.setName(request.getName());
	    customer.setEmail(request.getEmail());
	    customer.setPhone(request.getPhone());

	    return customer;
	}
	
    public CustomerResponseDTO toResponseDTO(CustomerEntity customer) {

        return new CustomerResponseDTO(
                customer.getId(),
                customer.getName(),
                customer.getEmail(),
                customer.getPhone(),
                customer.getAddress(),
                customer.getBusiness().getId()
        );
    }
}