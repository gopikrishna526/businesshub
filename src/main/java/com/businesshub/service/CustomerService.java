package com.businesshub.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.businesshub.dto.CustomerRequestDTO;
import com.businesshub.dto.CustomerResponseDTO;
import com.businesshub.dto.CustomerUpdateDTO;
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
		
		if (customerRepository.existsByBusiness_IdAndPhone(
		        request.getBusinessId(),
		        request.getPhone())) {

		    throw new IllegalArgumentException(
		            "Customer with this phone number already exists in this business");
		}

		CustomerEntity customer = customerMapper.toEntity(request);

		customer.setBusiness(business);

		CustomerEntity savedCustomer = customerRepository.save(customer);

		return customerMapper.toResponseDTO(savedCustomer);
	}

	@PreAuthorize("hasAuthority('ADMIN')")
	public Page<CustomerResponseDTO> getAllCustomers(Pageable pageable) {

	    return customerRepository.findAll(pageable)
	            .map(customerMapper::toResponseDTO);
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

	public Page<CustomerResponseDTO> getCustomersByBusiness(
	        Long businessId,
	        Pageable pageable) {

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
	    
	    String email = authentication.getName();
	    
	    Page<CustomerEntity> customers =
	            customerRepository.findByBusiness_IdAndBusiness_Owner_Email(
	                    businessId,
	                    email,
	                    pageable);

	    return customers.map(customerMapper::toResponseDTO);
	}
	
	public Page<CustomerResponseDTO> searchCustomers(
	        Long businessId,
	        String name,
	        Pageable pageable) {

	    Page<CustomerEntity> customers =
	            customerRepository.findByBusinessIdAndNameContainingIgnoreCase(
	                    businessId,
	                    name,
	                    pageable);

	    return customers.map(customerMapper::toResponseDTO);
	}
	
	public Page<CustomerResponseDTO> searchCustomersByKeyword(
	        Long businessId,
	        String keyword,
	        Pageable pageable) {

	    Authentication authentication =
	            SecurityContextHolder.getContext().getAuthentication();

	    String email = authentication.getName();

	    Page<CustomerEntity> customers =
	            customerRepository.searchCustomers(
	                    businessId,
	                    email,
	                    keyword,
	                    pageable);

	    return customers.map(customerMapper::toResponseDTO);
	}
	
	public Page<CustomerResponseDTO> filterCustomers(
	        Long businessId,
	        String name,
	        String email,
	        String phone,
	        Pageable pageable) {

	    Authentication authentication =
	            SecurityContextHolder.getContext().getAuthentication();

	    String ownerEmail = authentication.getName();

	    name = name == null ? "" : name.trim();
	    email = email == null ? "" : email.trim();
	    phone = phone == null ? "" : phone.trim();

	    Page<CustomerEntity> customers =
	            customerRepository.filterCustomers(
	                    businessId,
	                    ownerEmail,
	                    name,
	                    email,
	                    phone,
	                    pageable);

	    return customers.map(customerMapper::toResponseDTO);
	}

	public CustomerResponseDTO updateCustomer(
	        Long id,
	        CustomerUpdateDTO request) {

	    Authentication authentication =
	            SecurityContextHolder.getContext().getAuthentication();

	    CustomerEntity existingCustomer =
	            customerRepository.findById(id)
	                    .orElseThrow(() ->
	                            new CustomerNotFoundException(
	                                    "Customer not found with id: " + id));

	    if (!businessSecurity.isOwner(
	            existingCustomer.getBusiness().getId(),
	            authentication)) {

	        throw new AccessDeniedException(
	                "You are not the owner of this customer");
	    }

	    if (customerRepository.existsByBusiness_IdAndPhoneAndIdNot(
	            existingCustomer.getBusiness().getId(),
	            request.getPhone(),
	            id)) {

	        throw new IllegalArgumentException(
	                "Customer with this phone number already exists in this business");
	    }
	    
	    existingCustomer.setName(request.getName());
	    existingCustomer.setEmail(request.getEmail());
	    existingCustomer.setPhone(request.getPhone());
	    existingCustomer.setAddress(request.getAddress());

	    CustomerEntity updatedCustomer =
	            customerRepository.save(existingCustomer);

	    return customerMapper.toResponseDTO(updatedCustomer);
	}

	@PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
	public void deleteCustomer(Long id) {

	    CustomerEntity existingCustomer =
	            customerRepository.findById(id)
	                    .orElseThrow(() ->
	                            new CustomerNotFoundException(
	                                    "Customer not found with id: " + id));

	    Authentication authentication =
	            SecurityContextHolder.getContext().getAuthentication();

	    boolean isAdmin = authentication.getAuthorities().stream()
	            .anyMatch(a -> a.getAuthority().equals("ADMIN"));

	    if (!isAdmin && !businessSecurity.isOwner(
	            existingCustomer.getBusiness().getId(),
	            authentication)) {

	        throw new AccessDeniedException(
	                "You are not the owner of this business");
	    }

	    customerRepository.delete(existingCustomer);
	}
}