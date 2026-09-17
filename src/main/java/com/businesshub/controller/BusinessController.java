package com.businesshub.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.businesshub.dto.BusinessRequestDTO;
import com.businesshub.dto.BusinessResponseDTO;
import com.businesshub.service.BusinessService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/businesses")
public class BusinessController {

	private final BusinessService businessService;

	public BusinessController(BusinessService businessService) {
		this.businessService = businessService;
	}

//	@PostMapping
//	@ResponseStatus(HttpStatus.CREATED)
//	public Business createBusiness(@Valid @RequestBody Business business) {
//		return businessService.createBusiness(business);
//	}
//
//	@GetMapping
//	public List<Business> getAllBusinesses() {
//		return businessService.getAllBusinesses();
//	}
//
//	@GetMapping("/{id}")
//	public Business getBusinessById(@PathVariable Long id) {
//		return businessService.getBusinessById(id);
//	}
//
//	@PutMapping("/{id}")
//	public Business updateBusiness(@PathVariable Long id, @Valid @RequestBody Business business) {
//		return businessService.updateBusiness(id, business);
//	}
//	
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteBusiness(@PathVariable Long id) {
	    businessService.deleteBusiness(id);
	}
	
	

//	=======================>  Using Entity  <=======================
	
	

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public BusinessResponseDTO createBusiness(
	        @Valid @RequestBody BusinessRequestDTO request) {
	    return businessService.createBusiness(request);
	}
	
	@GetMapping("/{id}")
	public BusinessResponseDTO getBusinessById(@PathVariable Long id) {
	    return businessService.getBusinessById(id);
	}
	
	@GetMapping
	public List<BusinessResponseDTO> getAllBusinesses() {
	    return businessService.getAllBusinesses();
	}
	
	@PutMapping("/{id}")
	public BusinessResponseDTO updateBusiness(
	        @PathVariable Long id,
	        @Valid @RequestBody BusinessRequestDTO request) {
	    return businessService.updateBusiness(id, request);
	}
}