package com.businesshub.service;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.businesshub.dto.BusinessRequestDTO;
import com.businesshub.dto.BusinessResponseDTO;
import com.businesshub.entity.BusinessEntity;
import com.businesshub.exception.BusinessNotFoundException;
import com.businesshub.mapper.BusinessMapper;
import com.businesshub.repository.BusinessRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.businesshub.entity.UserEntity;
import com.businesshub.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
public class BusinessService {

	private final BusinessRepository businessRepository;
	private final BusinessMapper businessMapper;
	private final UserRepository userRepository;

	public BusinessService(BusinessRepository businessRepository, BusinessMapper businessMapper,
			UserRepository userRepository) {

		this.businessRepository = businessRepository;
		this.businessMapper = businessMapper;
		this.userRepository = userRepository;
	}
	
	@PreAuthorize("hasAuthority('ADMIN')")
	public void deleteBusiness(Long id) {

		BusinessEntity existingBusiness = businessRepository.findById(id)
				.orElseThrow(() -> new BusinessNotFoundException("Business not found with id: " + id));
		businessRepository.delete(existingBusiness);
	}

	public BusinessResponseDTO createBusiness(BusinessRequestDTO request) {

		BusinessEntity business = businessMapper.toEntity(request);

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		String email = authentication.getName();

		UserEntity owner = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

		business.setOwner(owner);

		BusinessEntity savedBusiness = businessRepository.save(business);

		return businessMapper.toResponseDTO(savedBusiness);
	}

	@PreAuthorize("hasAuthority('ADMIN') or @businessSecurity.isOwner(#id, authentication)")
	public BusinessResponseDTO getBusinessById(Long id) {

		BusinessEntity business = businessRepository.findById(id)
				.orElseThrow(() -> new BusinessNotFoundException("Business not found with id: " + id));

		return businessMapper.toResponseDTO(business);
	}

	public Page<BusinessResponseDTO> getAllBusinesses(Pageable pageable) {

	    Authentication authentication =
	            SecurityContextHolder.getContext().getAuthentication();

	    String email = authentication.getName();

	    Page<BusinessEntity> businesses =
	            businessRepository.findByOwner_Email(email, pageable);

	    return businesses.map(businessMapper::toResponseDTO);
	}

	@PreAuthorize("hasAuthority('ADMIN') or @businessSecurity.isOwner(#id, authentication)")
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
	
	public Page<BusinessResponseDTO> searchBusinesses(
	        String businessName,
	        Pageable pageable) {

	    Authentication authentication =
	            SecurityContextHolder.getContext().getAuthentication();

	    String email = authentication.getName();

	    Page<BusinessEntity> businesses =
	            businessRepository.findByOwner_EmailAndBusinessNameContainingIgnoreCase(
	                    email,
	                    businessName,
	                    pageable);

	    return businesses.map(businessMapper::toResponseDTO);
	}
	
	public Page<BusinessResponseDTO> searchBusinessesByKeyword(
	        String keyword,
	        Pageable pageable) {

	    Authentication authentication =
	            SecurityContextHolder.getContext().getAuthentication();

	    String email = authentication.getName();

//	    Page<BusinessEntity> businesses =
//	            businessRepository
//	                    .findByOwner_EmailAndBusinessNameContainingIgnoreCaseOrOwner_EmailAndAddressContainingIgnoreCase(
//	                            email,
//	                            keyword,
//	                            email,
//	                            keyword,
//	                            pageable);
	    
	    Page<BusinessEntity> businesses = businessRepository.searchByKeyword(email, keyword, pageable);

	    return businesses.map(businessMapper::toResponseDTO);
	}
	
	public Page<BusinessResponseDTO> filterBusinesses(
	        String keyword,
	        String email,
	        Pageable pageable) {

	    Authentication authentication =
	            SecurityContextHolder.getContext().getAuthentication();

	    String ownerEmail = authentication.getName();

	    keyword = keyword == null ? "" : keyword.trim();
	    email = email == null ? "" : email.trim();

	    Page<BusinessEntity> businesses =
	            businessRepository.filterBusinesses(
	                    ownerEmail,
	                    keyword,
	                    email,
	                    pageable);

	    return businesses.map(businessMapper::toResponseDTO);
	}
}
