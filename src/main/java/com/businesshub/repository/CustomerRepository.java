package com.businesshub.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.businesshub.entity.CustomerEntity;

public interface CustomerRepository extends JpaRepository<CustomerEntity, Long> {

	boolean existsByBusiness_IdAndPhone(Long businessId, String phone);

	boolean existsByBusiness_IdAndPhoneAndIdNot(Long businessId, String phone, Long id);

	Page<CustomerEntity> findByBusiness_IdAndBusiness_Owner_Email(Long businessId, String email, Pageable pageable);

	Page<CustomerEntity> findByBusinessIdAndNameContainingIgnoreCase(Long businessId, String name, Pageable pageable);

	@Query("""
			    SELECT c FROM CustomerEntity c
			    WHERE c.business.id = :businessId
			    AND c.business.owner.email = :ownerEmail
			    AND (
			        LOWER(c.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
			        OR LOWER(c.email) LIKE LOWER(CONCAT('%', :keyword, '%'))
			        OR c.phone LIKE CONCAT('%', :keyword, '%')
			    )
			""")
	Page<CustomerEntity> searchCustomers(@Param("businessId") Long businessId, @Param("ownerEmail") String ownerEmail,
			@Param("keyword") String keyword, Pageable pageable);

	Page<CustomerEntity> findByBusiness_IdAndBusiness_Owner_EmailAndNameContainingIgnoreCase(Long businessId,
			String ownerEmail, String name, Pageable pageable);

	@Query("""
			    SELECT c FROM CustomerEntity c
			    WHERE c.business.id = :businessId
			    AND c.business.owner.email = :ownerEmail
			    AND (:name = '' OR LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%')))
			    AND (:email = '' OR LOWER(c.email) LIKE LOWER(CONCAT('%', :email, '%')))
			    AND (:phone = '' OR c.phone LIKE CONCAT('%', :phone, '%'))
			""")
	Page<CustomerEntity> filterCustomers(@Param("businessId") Long businessId, @Param("ownerEmail") String ownerEmail,
			@Param("name") String name, @Param("email") String email, @Param("phone") String phone, Pageable pageable);
}
