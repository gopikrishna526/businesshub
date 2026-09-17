package com.businesshub.service;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.businesshub.dto.BusinessRequestDTO;
import com.businesshub.dto.BusinessResponseDTO;
import com.businesshub.entity.BusinessEntity;
import com.businesshub.exception.BusinessNotFoundException;
import com.businesshub.mapper.BusinessMapper;
import com.businesshub.repository.BusinessRepository;

@Service
public class BusinessService {

	private final BusinessRepository businessRepository;
	private final BusinessMapper businessMapper;

	public BusinessService(BusinessRepository businessRepository, BusinessMapper businessMapper) {
		this.businessRepository = businessRepository;
		this.businessMapper = businessMapper;
	}

//	public Business createBusiness(Business business) {
//		return businessRepository.save(business);
//	}
//
//	public List<Business> getAllBusinesses() {
//		return businessRepository.findAll();
//	}
//
//    public Business getBusinessById(Long id) {
//        return businessRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Business not found with id: " + id));
//    }
//
//	public Business getBusinessById(Long id) {
//		return businessRepository.findById(id)
//				.orElseThrow(() -> new BusinessNotFoundException("Business not found with id: " + id));
//	}
//
//	public Business updateBusiness(Long id, Business updatedBusiness) {
//
//		Business existingBusiness = businessRepository.findById(id)
//				.orElseThrow(() -> new BusinessNotFoundException("Business not found with id: " + id));
//
//		existingBusiness.setBusinessName(updatedBusiness.getBusinessName());
//		existingBusiness.setEmail(updatedBusiness.getEmail());
//		existingBusiness.setPhone(updatedBusiness.getPhone());
//		existingBusiness.setAddress(updatedBusiness.getAddress());
//
//		return businessRepository.save(existingBusiness);
//	}
//
	@PreAuthorize("hasAuthority('ADMIN')")
	public void deleteBusiness(Long id) {

		BusinessEntity existingBusiness = businessRepository.findById(id)
				.orElseThrow(() -> new BusinessNotFoundException("Business not found with id: " + id));
		businessRepository.delete(existingBusiness);
	}

//	=======================>  Above Using Entity  <=======================

//	public BusinessResponseDTO createBusiness(BusinessRequestDTO request) {
//
//		Business business = new Business();
//
//		business.setBusinessName(request.getBusinessName());
//		business.setEmail(request.getEmail());
//		business.setPhone(request.getPhone());
//		business.setAddress(request.getAddress());
//
//		Business savedBusiness = businessRepository.save(business);
//
//		return new BusinessResponseDTO(savedBusiness.getId(), savedBusiness.getBusinessName(), savedBusiness.getEmail(),
//				savedBusiness.getPhone(), savedBusiness.getAddress());
//	}
//
//	public BusinessResponseDTO getBusinessById(Long id) {
//		Business business = businessRepository.findById(id)
//				.orElseThrow(() -> new BusinessNotFoundException("Business not found with id: " + id));
//		return new BusinessResponseDTO(business.getId(), business.getBusinessName(), business.getEmail(),
//				business.getPhone(), business.getAddress());
//	}
//
//	public List<BusinessResponseDTO> getAllBusinesses() {
//
//		return businessRepository
//				.findAll().stream().map(business -> new BusinessResponseDTO(business.getId(),
//						business.getBusinessName(), business.getEmail(), business.getPhone(), business.getAddress()))
//				.toList();
//	}
//
//	public BusinessResponseDTO updateBusiness(Long id, BusinessRequestDTO request) {
//
//		Business existingBusiness = businessRepository.findById(id)
//				.orElseThrow(() -> new BusinessNotFoundException("Business not found with id: " + id));
//
//		existingBusiness.setBusinessName(request.getBusinessName());
//		existingBusiness.setEmail(request.getEmail());
//		existingBusiness.setPhone(request.getPhone());
//		existingBusiness.setAddress(request.getAddress());
//
//		Business updatedBusiness = businessRepository.save(existingBusiness);
//
//		return new BusinessResponseDTO(updatedBusiness.getId(), updatedBusiness.getBusinessName(),
//				updatedBusiness.getEmail(), updatedBusiness.getPhone(), updatedBusiness.getAddress());
//	}

//	=======================> Below Using Mappers  <=======================

	public BusinessResponseDTO createBusiness(BusinessRequestDTO request) {

		BusinessEntity business = businessMapper.toEntity(request);

		BusinessEntity savedBusiness = businessRepository.save(business);

		return businessMapper.toResponseDTO(savedBusiness);
	}

	public BusinessResponseDTO getBusinessById(Long id) {

		BusinessEntity business = businessRepository.findById(id)
				.orElseThrow(() -> new BusinessNotFoundException("Business not found with id: " + id));

		return businessMapper.toResponseDTO(business);
	}

	public List<BusinessResponseDTO> getAllBusinesses() {

		return businessRepository.findAll().stream().map(businessMapper::toResponseDTO).toList();
	}

	public BusinessResponseDTO updateBusiness(Long id, BusinessRequestDTO request) {

		BusinessEntity existingBusiness = businessRepository.findById(id)
				.orElseThrow(() -> new BusinessNotFoundException("Business not found with id: " + id));

		existingBusiness.setBusinessName(request.getBusinessName());
		existingBusiness.setEmail(request.getEmail());
		existingBusiness.setPhone(request.getPhone());
		existingBusiness.setAddress(request.getAddress());

		BusinessEntity updatedBusiness = businessRepository.save(existingBusiness);

		return businessMapper.toResponseDTO(updatedBusiness);
	}
}
