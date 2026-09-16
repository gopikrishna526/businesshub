package com.businesshub.service;

import java.util.List;

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

@Service
public class CustomerService {

	private final CustomerRepository customerRepository;
	private final BusinessRepository businessRepository;
	private final CustomerMapper customerMapper;

	public CustomerService(CustomerRepository customerRepository, BusinessRepository businessRepository,
			CustomerMapper customerMapper) {

		this.customerRepository = customerRepository;
		this.businessRepository = businessRepository;
		this.customerMapper = customerMapper;
	}

	public CustomerResponseDTO createCustomer(CustomerRequestDTO request) {

		BusinessEntity business = businessRepository.findById(request.getBusinessId()).orElseThrow(
				() -> new BusinessNotFoundException("Business not found with id: " + request.getBusinessId()));

		CustomerEntity customer = new CustomerEntity();

		customer.setName(request.getName());
		customer.setEmail(request.getEmail());
		customer.setPhone(request.getPhone());
		customer.setAddress(request.getAddress());

		customer.setBusiness(business);

		CustomerEntity savedCustomer = customerRepository.save(customer);

		return customerMapper.toResponseDTO(savedCustomer);
	}

	public List<CustomerResponseDTO> getAllCustomers() {

		return customerRepository.findAll().stream().map(customerMapper::toResponseDTO).toList();
	}

	public CustomerResponseDTO getCustomerById(Long id) {

		CustomerEntity customer = customerRepository.findById(id)
				.orElseThrow(() -> new CustomerNotFoundException("Customer not found with id: " + id));

		return customerMapper.toResponseDTO(customer);
	}

	public List<CustomerResponseDTO> getCustomersByBusiness(Long businessId) {

		if (!businessRepository.existsById(businessId)) {
			throw new BusinessNotFoundException("Business not found with id: " + businessId);
		}

		return customerRepository.findByBusinessId(businessId).stream().map(customerMapper::toResponseDTO).toList();
	}

	public CustomerResponseDTO updateCustomer(Long id, CustomerRequestDTO request) {

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

	public void deleteCustomer(Long id) {

		CustomerEntity existingCustomer = customerRepository.findById(id)
				.orElseThrow(() -> new CustomerNotFoundException("Customer not found with id: " + id));

		customerRepository.delete(existingCustomer);
	}
}