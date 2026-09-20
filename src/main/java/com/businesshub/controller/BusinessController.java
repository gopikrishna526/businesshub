package com.businesshub.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.businesshub.dto.BusinessRequestDTO;
import com.businesshub.dto.BusinessResponseDTO;
import com.businesshub.service.BusinessService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

@RestController
@RequestMapping("/api/businesses")
public class BusinessController {

	private final BusinessService businessService;

	public BusinessController(BusinessService businessService) {
		this.businessService = businessService;
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteBusiness(@PathVariable Long id) {
	    businessService.deleteBusiness(id);
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public BusinessResponseDTO createBusiness(
	        @Valid @RequestBody BusinessRequestDTO request) {
	    return businessService.createBusiness(request);
	}
	
	@GetMapping("/search")
	public ResponseEntity<Page<BusinessResponseDTO>> searchBusinesses(
	        @RequestParam @NotBlank String businessName,
	        @PageableDefault(size = 5) Pageable pageable) {

	    return ResponseEntity.ok(
	            businessService.searchBusinesses(businessName, pageable)
	    );
	}
	
	@GetMapping("/filter")
	public ResponseEntity<Page<BusinessResponseDTO>> filterBusinesses(
	        @RequestParam(required = false) String keyword,
	        @RequestParam(required = false) String email,
	        @PageableDefault(size = 5, sort = "businessName") Pageable pageable) {

	    return ResponseEntity.ok(
	            businessService.filterBusinesses(keyword, email, pageable)
	    );
	}
	
	@GetMapping("/{id}")
	public BusinessResponseDTO getBusinessById(@PathVariable Long id) {
	    return businessService.getBusinessById(id);
	}
	
	@GetMapping("/search/keyword")
	public ResponseEntity<Page<BusinessResponseDTO>> searchBusinessesByKeyword(
	        @RequestParam @NotBlank String keyword,
	        @PageableDefault(size = 5) Pageable pageable) {

	    return ResponseEntity.ok(
	            businessService.searchBusinessesByKeyword(keyword, pageable)
	    );
	}
	
	@GetMapping
	public Page<BusinessResponseDTO> getAllBusinesses(
	        @PageableDefault(size = 10, sort = "businessName") Pageable pageable) {
	    return businessService.getAllBusinesses(pageable);
	}
	
	@PutMapping("/{id}")
	public BusinessResponseDTO updateBusiness(
	        @PathVariable Long id,
	        @Valid @RequestBody BusinessRequestDTO request) {
	    return businessService.updateBusiness(id, request);
	}
	
}