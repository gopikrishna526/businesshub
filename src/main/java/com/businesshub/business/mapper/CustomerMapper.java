package com.businesshub.business.mapper;

import org.springframework.stereotype.Component;

import com.businesshub.business.Customer;
import com.businesshub.business.dto.CustomerResponseDTO;

@Component
public class CustomerMapper {

    public CustomerResponseDTO toResponseDTO(Customer customer) {

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