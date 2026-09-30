package com.businesshub.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.businesshub.dto.ProductRequestDTO;
import com.businesshub.dto.ProductResponseDTO;
import com.businesshub.service.ProductService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/products")
public class ProductController {

	private final ProductService productService;

	public ProductController(ProductService productService) {
		this.productService = productService;
	}

	@PostMapping("/business/{businessId}")
	public ResponseEntity<ProductResponseDTO> createProduct(@PathVariable Long businessId,
			@Valid @RequestBody ProductRequestDTO requestDTO, Authentication authentication) {

		String email = authentication.getName();

		ProductResponseDTO response = productService.createProduct(businessId, requestDTO, email);

		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
	
	@GetMapping("/business/{businessId}")
	public ResponseEntity<Page<ProductResponseDTO>> getProductsByBusiness(
	        @PathVariable Long businessId,
	        @RequestParam(required = false) String name,
	        Pageable pageable,
	        Authentication authentication) {

	    String email = authentication.getName();

	    Page<ProductResponseDTO> response =
	            productService.getProductsByBusiness(
	                    businessId,
	                    name,
	                    pageable,
	                    email);

	    return ResponseEntity.ok(response);
	}
	
	@GetMapping("/business/{businessId}/projection")
	public ResponseEntity<Page<ProductResponseDTO>> getProductsAsDTO(
	        @PathVariable Long businessId,
	        Pageable pageable) {

	    return ResponseEntity.ok(
	            productService.getProductsAsDTO(businessId, pageable)
	    );
	}
	
	@PutMapping("/{productId}/business/{businessId}")
	public ResponseEntity<ProductResponseDTO> updateProduct(
	        @PathVariable Long productId,
	        @PathVariable Long businessId,
	        @Valid @RequestBody ProductRequestDTO requestDTO,
	        Authentication authentication) {

	    String email = authentication.getName();

	    ProductResponseDTO response =
	            productService.updateProduct(
	                    productId,
	                    businessId,
	                    requestDTO,
	                    email);

	    return ResponseEntity.ok(response);
	}
	
	@DeleteMapping("/{productId}/business/{businessId}")
	public ResponseEntity<Void> deleteProduct(
	        @PathVariable Long productId,
	        @PathVariable Long businessId,
	        Authentication authentication) {

	    String email = authentication.getName();

	    productService.deleteProduct(
	            productId,
	            businessId,
	            email);

	    return ResponseEntity.noContent().build();
	}
	
}