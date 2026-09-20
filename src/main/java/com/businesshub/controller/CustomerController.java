package com.businesshub.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.businesshub.dto.CustomerRequestDTO;
import com.businesshub.dto.CustomerResponseDTO;
import com.businesshub.dto.CustomerUpdateDTO;
import com.businesshub.service.CustomerService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

	private final CustomerService customerService;

	public CustomerController(CustomerService customerService) {
		this.customerService = customerService;
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public CustomerResponseDTO createCustomer(@Valid @RequestBody CustomerRequestDTO request) {
		return customerService.createCustomer(request);
	}

	@GetMapping
	public Page<CustomerResponseDTO> getAllCustomers(
	        @PageableDefault(size = 10, sort = "name") Pageable pageable) {
	    return customerService.getAllCustomers(pageable);
	}

	@GetMapping("/{id}")
	public CustomerResponseDTO getCustomerById(@PathVariable Long id) {
		return customerService.getCustomerById(id);
	}

	@GetMapping("/business/{businessId}")
	public Page<CustomerResponseDTO> getCustomersByBusiness(
	        @PathVariable Long businessId,
	        @PageableDefault(size = 5, sort = "name") Pageable pageable) {

	    return customerService.getCustomersByBusiness(
	            businessId,
	            pageable);
	}
	
	@GetMapping("/business/{businessId}/search")
	public Page<CustomerResponseDTO> searchCustomers(
	        @PathVariable Long businessId,
	        @RequestParam @NotBlank String name,
	        @PageableDefault(size = 5, sort = "name") Pageable pageable) {

	    return customerService.searchCustomers(
	            businessId,
	            name,
	            pageable);
	}
	
	@GetMapping("/business/{businessId}/search/keyword")
	public Page<CustomerResponseDTO> searchCustomersByKeyword(
	        @PathVariable Long businessId,
	        @RequestParam @NotBlank String keyword,
	        @PageableDefault(size = 5, sort = "name") Pageable pageable) {

	    return customerService.searchCustomersByKeyword(
	            businessId,
	            keyword,
	            pageable);
	}
	
	@GetMapping("/business/{businessId}/filter")
	public Page<CustomerResponseDTO> filterCustomers(
	        @PathVariable Long businessId,
	        @RequestParam(required = false) String name,
	        @RequestParam(required = false) String email,
	        @RequestParam(required = false) String phone,
	        @PageableDefault(size = 5, sort = "name") Pageable pageable) {

	    return customerService.filterCustomers(
	            businessId,
	            name,
	            email,
	            phone,
	            pageable);
	}

	@PutMapping("/{id}")
	public CustomerResponseDTO updateCustomer(@PathVariable Long id, @Valid @RequestBody CustomerUpdateDTO request) {
		return customerService.updateCustomer(id, request);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteCustomer(@PathVariable Long id) {
		customerService.deleteCustomer(id);
	}
}