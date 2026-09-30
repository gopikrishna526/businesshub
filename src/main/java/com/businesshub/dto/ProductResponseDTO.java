package com.businesshub.dto;

import java.math.BigDecimal;

public class ProductResponseDTO {

	private Long id;
	private String name;
	private BigDecimal price;
	private Long businessId;
	private String businessName;

	public ProductResponseDTO() {
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

	public Long getBusinessId() {
		return businessId;
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

	public void setBusinessId(Long businessId) {
		this.businessId = businessId;
	}

	public String getBusinessName() {
		return businessName;
	}

	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}

	public ProductResponseDTO(Long id, String name, BigDecimal price, Long businessId, String businessName) {
		super();
		this.id = id;
		this.name = name;
		this.price = price;
		this.businessId = businessId;
		this.businessName = businessName;
	}
}