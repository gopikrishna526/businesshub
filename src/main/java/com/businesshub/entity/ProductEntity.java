package com.businesshub.entity;

import java.math.BigDecimal;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "products")
public class ProductEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank(message = "Product name is required")
	@Size(min = 2, max = 100, message = "Product name must be between 2 and 100 characters")
	@Column(nullable = false)
	private String name;

	@NotNull(message = "Price is required")
	@DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
	@Column(nullable = false)
	private BigDecimal price;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "business_id", nullable = false)
	private BusinessEntity business;
	
	@Version
	private Long version;

	public ProductEntity() {
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public BusinessEntity getBusiness() {
		return business;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public void setBusiness(BusinessEntity business) {
		this.business = business;
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}
}