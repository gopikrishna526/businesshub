package com.businesshub.business;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.businesshub.business.dto.CustomerRequestDTO;
import com.businesshub.business.dto.CustomerResponseDTO;

import jakarta.validation.Valid;

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
	public List<CustomerResponseDTO> getAllCustomers() {
		return customerService.getAllCustomers();
	}

	@GetMapping("/{id}")
	public CustomerResponseDTO getCustomerById(@PathVariable Long id) {
		return customerService.getCustomerById(id);
	}

	@GetMapping("/business/{businessId}")
	public List<CustomerResponseDTO> getCustomersByBusiness(@PathVariable Long businessId) {
		return customerService.getCustomersByBusiness(businessId);
	}

	@PutMapping("/{id}")
	public CustomerResponseDTO updateCustomer(@PathVariable Long id, @Valid @RequestBody CustomerRequestDTO request) {
		return customerService.updateCustomer(id, request);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteCustomer(@PathVariable Long id) {
		customerService.deleteCustomer(id);
	}
}