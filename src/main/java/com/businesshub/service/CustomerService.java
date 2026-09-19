package com.businesshub.service;

import java.util.List;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.businesshub.dto.CustomerRequestDTO;
import com.businesshub.dto.CustomerResponseDTO;
import com.businesshub.entity.BusinessEntity;
import com.businesshub.entity.CustomerEntity;
import com.businesshub.exception.BusinessNotFoundException;
import com.businesshub.exception.CustomerNotFoundException;
import com.businesshub.mapper.CustomerMapper;
import com.businesshub.repository.BusinessRepository;
import com.businesshub.repository.CustomerRepository;
import com.businesshub.security.BusinessSecurity;

@Service
public class CustomerService {

	private final CustomerRepository customerRepository;
	private final BusinessRepository businessRepository;
	private final CustomerMapper customerMapper;
	private final BusinessSecurity businessSecurity;

	public CustomerService(CustomerRepository customerRepository, BusinessRepository businessRepository, CustomerMapper customerMapper,
			BusinessSecurity businessSecurity) {

		this.customerRepository = customerRepository;
		this.businessRepository = businessRepository;
		this.customerMapper = customerMapper;
		this.businessSecurity = businessSecurity;
	}

	public CustomerResponseDTO createCustomer(CustomerRequestDTO request) {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (!businessSecurity.isOwner(request.getBusinessId(), authentication)) {
			throw new AccessDeniedException("You are not the owner of this business");
		}

		BusinessEntity business = businessRepository.findById(request.getBusinessId()).orElseThrow(
				() -> new BusinessNotFoundException("Business not found with id: " + request.getBusinessId()));

		CustomerEntity customer = customerMapper.toEntity(request);

		customer.setBusiness(business);

		CustomerEntity savedCustomer = customerRepository.save(customer);

		return customerMapper.toResponseDTO(savedCustomer);
	}

	public List<CustomerResponseDTO> getAllCustomers() {

		return customerRepository.findAll().stream().map(customerMapper::toResponseDTO).toList();
	}

	public CustomerResponseDTO getCustomerById(Long id) {

	    CustomerEntity customer = customerRepository.findById(id)
	            .orElseThrow(() ->
	                new CustomerNotFoundException(
	                    "Customer not found with id: " + id));

	    Authentication authentication =
	            SecurityContextHolder.getContext().getAuthentication();

	    if (!businessSecurity.isOwner(
	            customer.getBusiness().getId(), authentication)) {

	        throw new AccessDeniedException(
	                "You are not the owner of this business");
	    }

	    return customerMapper.toResponseDTO(customer);
	}

	public List<CustomerResponseDTO> getCustomersByBusiness(Long businessId) {

	    if (!businessRepository.existsById(businessId)) {
	        throw new BusinessNotFoundException(
	                "Business not found with id: " + businessId);
	    }

	    Authentication authentication =
	            SecurityContextHolder.getContext().getAuthentication();

	    if (!businessSecurity.isOwner(businessId, authentication)) {
	        throw new AccessDeniedException(
	                "You are not the owner of this business");
	    }

	    return customerRepository.findByBusinessId(businessId)
	            .stream()
	            .map(customerMapper::toResponseDTO)
	            .toList();
	}

	public CustomerResponseDTO updateCustomer(Long id, CustomerRequestDTO request) {
		
		Authentication authentication =
		        SecurityContextHolder.getContext().getAuthentication();

		if (!businessSecurity.isOwner(request.getBusinessId(), authentication)) {
		    throw new AccessDeniedException(
		            "You are not the owner of this business");
		}

		CustomerEntity existingCustomer = customerRepository.findById(id)
				.orElseThrow(() -> new CustomerNotFoundException("Customer not found with id: " + id));

		BusinessEntity business = businessRepository.findById(request.getBusinessId()).orElseThrow(
				() -> new BusinessNotFoundException("Business not found with id: " + request.getBusinessId()));

		existingCustomer.setName(request.getName());
		existingCustomer.setEmail(request.getEmail());
		existingCustomer.setPhone(request.getPhone());
		existingCustomer.setAddress(request.getAddress());
		existingCustomer.setBusiness(business);

		CustomerEntity updatedCustomer = customerRepository.save(existingCustomer);

		return customerMapper.toResponseDTO(updatedCustomer);
	}

	@PreAuthorize("hasAuthority('ADMIN')")
	public void deleteCustomer(Long id) {

	    CustomerEntity existingCustomer = customerRepository.findById(id)
	            .orElseThrow(() ->
	                new CustomerNotFoundException(
	                    "Customer not found with id: " + id));

	    Authentication authentication =
	            SecurityContextHolder.getContext().getAuthentication();

	    if (!businessSecurity.isOwner(
	            existingCustomer.getBusiness().getId(), authentication)) {

	        throw new AccessDeniedException(
	                "You are not the owner of this business");
	    }

	    customerRepository.delete(existingCustomer);
	}
}