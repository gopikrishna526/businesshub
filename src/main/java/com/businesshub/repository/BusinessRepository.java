package com.businesshub.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.businesshub.entity.BusinessEntity;

public interface BusinessRepository extends JpaRepository<BusinessEntity, Long> {

	boolean existsByIdAndOwner_Email(Long id, String email);

	Optional<BusinessEntity> findByIdAndOwner_Email(Long id, String email);

	Page<BusinessEntity> findByOwner_Email(String email, Pageable pageable);

	Page<BusinessEntity> findByOwner_EmailAndBusinessNameContainingIgnoreCase(String email, String businessName,
			Pageable pageable);

	@Query("SELECT b FROM BusinessEntity b WHERE b.owner.email = :email AND (LOWER(b.businessName) LIKE LOWER(CONCAT('%', :keyword, '%')) "
			+ " OR LOWER(b.address) LIKE LOWER(CONCAT('%', :keyword, '%')))")
	Page<BusinessEntity> searchByKeyword(@Param("email") String email, @Param("keyword") String keyword,
			Pageable pageable);
	
	@Query("""
		    SELECT b FROM BusinessEntity b
		    WHERE b.owner.email = :ownerEmail
		    AND (
		        :keyword = ''
		        OR LOWER(b.businessName) LIKE LOWER(CONCAT('%', :keyword, '%'))
		        OR LOWER(b.address) LIKE LOWER(CONCAT('%', :keyword, '%'))
		    )
		    AND (
		        :email = ''
		        OR LOWER(b.email) = LOWER(:email)
		    )
		""")
		Page<BusinessEntity> filterBusinesses(
		        @Param("ownerEmail") String ownerEmail,
		        @Param("keyword") String keyword,
		        @Param("email") String email,
		        Pageable pageable);
}
