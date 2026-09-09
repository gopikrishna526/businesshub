package com.businesshub.business.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class CustomerRequestDTO {

	@NotBlank(message = "Customer name is required")
	@Size(min = 3, max = 100, message = "Customer name must be between 3 and 100 characters")
	private String name;

	@Email(message = "Please provide a valid email address")
	private String email;

	@NotBlank(message = "Phone number is required")
	@Pattern(regexp = "^[0-9]{10}$", message = "Phone number must contain exactly 10 digits")
	private String phone;

	private String address;

	@NotNull(message = "Business ID is required")
	private Long businessId;

	public CustomerRequestDTO() {
	}

	public String getName() {
		return name;
	}

	public String getEmail() {
		return email;
	}

	public String getPhone() {
		return phone;
	}

	public String getAddress() {
		return address;
	}

	public Long getBusinessId() {
		return businessId;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setBusinessId(Long businessId) {
		this.businessId = businessId;
	}
}