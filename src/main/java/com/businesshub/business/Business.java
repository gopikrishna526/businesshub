package com.businesshub.business;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "businesses")
public class Business {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank(message = "Business name is required")
	@Size(min = 3, max = 100, message = "Business name must be between 3 and 100 characters")
	@Column(nullable = false)
	private String businessName;

	@Email(message = "Please provide a valid email address")
	private String email;

	@NotBlank(message = "Phone number is required")
	@Size(min = 10, max = 10, message = "Phone number must contain 10 digits")
	@Pattern(regexp = "^[0-9]{10}$", message = "Phone number must contain exactly 10 digits")
	private String phone;

	private String address;

	public Business() {
	}

	public Business(Long id, String businessName, String email, String phone, String address) {
		this.id = id;
		this.businessName = businessName;
		this.email = email;
		this.phone = phone;
		this.address = address;
	}

	public Long getId() {
		return id;
	}

	public String getBusinessName() {
		return businessName;
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

	public void setId(Long id) {
		this.id = id;
	}

	public void setBusinessName(String businessName) {
		this.businessName = businessName;
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
}