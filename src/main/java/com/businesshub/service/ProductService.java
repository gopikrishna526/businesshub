package com.businesshub.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.businesshub.dto.ProductRequestDTO;
import com.businesshub.dto.ProductResponseDTO;
import com.businesshub.entity.BusinessEntity;
import com.businesshub.entity.ProductEntity;
import com.businesshub.mapper.ProductMapper;
import com.businesshub.repository.BusinessRepository;
import com.businesshub.repository.ProductRepository;
import com.businesshub.exception.ResourceNotFoundException;


@Service
public class ProductService {

	private final ProductRepository productRepository;
	private final BusinessRepository businessRepository;
	private final ProductMapper productMapper;

	public ProductService(ProductRepository productRepository, BusinessRepository businessRepository,
			ProductMapper productMapper) {
		this.productRepository = productRepository;
		this.businessRepository = businessRepository;
		this.productMapper = productMapper;
	}

	@Transactional
	public ProductResponseDTO createProduct(Long businessId, ProductRequestDTO requestDTO, String email) {

		BusinessEntity business = businessRepository.findByIdAndOwner_Email(businessId, email)
				.orElseThrow(() -> new AccessDeniedException("You do not have access to this business"));

		ProductEntity product = productMapper.toEntity(requestDTO, business);

		ProductEntity savedProduct = productRepository.save(product);

		return productMapper.toResponseDTO(savedProduct);
	}

	@Transactional(readOnly = true)
	public Page<ProductResponseDTO> getProductsByBusiness(Long businessId, String name, Pageable pageable,
			String email) {

		BusinessEntity business = businessRepository.findByIdAndOwner_Email(businessId, email)
				.orElseThrow(() -> new AccessDeniedException("You do not have access to this business"));

		Page<ProductEntity> products;

		if (name == null || name.isBlank()) {
			products = productRepository.findByBusiness_Id(business.getId(), pageable);
		} else {
			products = productRepository.findByBusiness_IdAndNameContainingIgnoreCase(business.getId(), name, pageable);
		}

		return products.map(productMapper::toResponseDTO);
	}

	@Transactional
	public ProductResponseDTO updateProduct(Long productId, Long businessId, ProductRequestDTO requestDTO,
			String email) {

		BusinessEntity business = businessRepository.findByIdAndOwner_Email(businessId, email)
				.orElseThrow(() -> new AccessDeniedException("You do not have access to this business"));

		ProductEntity product = productRepository
		        .findByIdAndBusinessIdWithLock(productId, business.getId())
		        .orElseThrow(() -> new ResourceNotFoundException("Product Not Found"));

		product.setName(requestDTO.getName());
		product.setPrice(requestDTO.getPrice());

		ProductEntity updatedProduct = productRepository.save(product);

		return productMapper.toResponseDTO(updatedProduct);
	}

	@Transactional
	public void deleteProduct(Long productId, Long businessId, String email) {

		BusinessEntity business = businessRepository.findByIdAndOwner_Email(businessId, email)
				.orElseThrow(() -> new ResourceNotFoundException("Business not found"));

		ProductEntity product = productRepository.findByIdAndBusiness_Id(productId, business.getId())
				.orElseThrow(() -> new ResourceNotFoundException("Product not found"));

		productRepository.delete(product);
	}
	
	@Transactional(readOnly = true)
	public Page<ProductResponseDTO> getProductsAsDTO(
	        Long businessId,
	        Pageable pageable) {

	    return productRepository.findProductDTOsByBusinessId(
	            businessId,
	            pageable
	    );
	}
	
}