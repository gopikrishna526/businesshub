package com.businesshub.business;

import java.util.List;

import org.springframework.stereotype.Service;

import com.businesshub.business.dto.CustomerRequestDTO;
import com.businesshub.business.dto.CustomerResponseDTO;
import com.businesshub.business.mapper.CustomerMapper;

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

		Business business = businessRepository.findById(request.getBusinessId()).orElseThrow(
				() -> new BusinessNotFoundException("Business not found with id: " + request.getBusinessId()));

		Customer customer = new Customer();

		customer.setName(request.getName());
		customer.setEmail(request.getEmail());
		customer.setPhone(request.getPhone());
		customer.setAddress(request.getAddress());

		customer.setBusiness(business);

		Customer savedCustomer = customerRepository.save(customer);

		return customerMapper.toResponseDTO(savedCustomer);
	}

	public List<CustomerResponseDTO> getAllCustomers() {

		return customerRepository.findAll().stream().map(customerMapper::toResponseDTO).toList();
	}

	public CustomerResponseDTO getCustomerById(Long id) {

		Customer customer = customerRepository.findById(id)
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

		Customer existingCustomer = customerRepository.findById(id)
				.orElseThrow(() -> new CustomerNotFoundException("Customer not found with id: " + id));

		Business business = businessRepository.findById(request.getBusinessId()).orElseThrow(
				() -> new BusinessNotFoundException("Business not found with id: " + request.getBusinessId()));

		existingCustomer.setName(request.getName());
		existingCustomer.setEmail(request.getEmail());
		existingCustomer.setPhone(request.getPhone());
		existingCustomer.setAddress(request.getAddress());
		existingCustomer.setBusiness(business);

		Customer updatedCustomer = customerRepository.save(existingCustomer);

		return customerMapper.toResponseDTO(updatedCustomer);
	}

	public void deleteCustomer(Long id) {

		Customer existingCustomer = customerRepository.findById(id)
				.orElseThrow(() -> new CustomerNotFoundException("Customer not found with id: " + id));

		customerRepository.delete(existingCustomer);
	}
}