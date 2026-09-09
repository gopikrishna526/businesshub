package com.businesshub.business.dto;

public class BusinessResponseDTO {

	private Long id;
	private String businessName;
	private String email;
	private String phone;
	private String address;

	public BusinessResponseDTO() {
	}

	public BusinessResponseDTO(Long id, String businessName, String email, String phone, String address) {

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