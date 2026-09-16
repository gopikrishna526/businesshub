package com.businesshub.mapper;

import org.springframework.stereotype.Component;

import com.businesshub.dto.CustomerResponseDTO;
import com.businesshub.entity.CustomerEntity;

@Component
public class CustomerMapper {

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