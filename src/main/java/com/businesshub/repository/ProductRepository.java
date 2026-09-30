package com.businesshub.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.Lock;

import com.businesshub.dto.ProductResponseDTO;
import com.businesshub.entity.ProductEntity;


public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

	Page<ProductEntity> findByBusiness_Id(Long businessId, Pageable pageable);

	Page<ProductEntity> findByBusiness_IdAndNameContainingIgnoreCase(Long businessId, String name, Pageable pageable);

	Optional<ProductEntity> findByIdAndBusiness_Id(Long productId, Long businessId);
	
	@Query("""
		    SELECT new com.businesshub.dto.ProductResponseDTO(
		        p.id,
		        p.name,
		        p.price,
		        b.id,
		        b.businessName
		    )
		    FROM ProductEntity p
		    JOIN p.business b
		    WHERE b.id = :businessId
		""")
		Page<ProductResponseDTO> findProductDTOsByBusinessId(
		        @Param("businessId") Long businessId,
		        Pageable pageable);
	
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("SELECT p FROM ProductEntity p WHERE p.id = :id")
	Optional<ProductEntity> findByIdWithLock(@Param("id") Long id);
	
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("""
	       SELECT p
	       FROM ProductEntity p
	       WHERE p.id = :productId
	       AND p.business.id = :businessId
	       """)
	Optional<ProductEntity> findByIdAndBusinessIdWithLock(
	        @Param("productId") Long productId,
	        @Param("businessId") Long businessId);
}