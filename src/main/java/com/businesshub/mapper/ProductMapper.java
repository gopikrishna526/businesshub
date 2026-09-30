package com.businesshub.mapper;

import org.springframework.stereotype.Component;

import com.businesshub.dto.ProductRequestDTO;
import com.businesshub.dto.ProductResponseDTO;
import com.businesshub.entity.BusinessEntity;
import com.businesshub.entity.ProductEntity;

@Component
public class ProductMapper {

	public ProductEntity toEntity(ProductRequestDTO dto, BusinessEntity business) {

		ProductEntity product = new ProductEntity();

		product.setName(dto.getName());
		product.setPrice(dto.getPrice());
		product.setBusiness(business);

		return product;
	}

	public ProductResponseDTO toResponseDTO(ProductEntity product) {

		return new ProductResponseDTO(
		        product.getId(),
		        product.getName(),
		        product.getPrice(),
		        product.getBusiness().getId(),
		        product.getBusiness().getBusinessName()
		);
	}
}