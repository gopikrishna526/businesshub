package com.businesshub.business;

import jakarta.persistence.*;

@Entity
@Table(name = "customers")
public class Customer {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String name;

	private String email;

	private String phone;

	private String address;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "business_id", nullable = false)
	private Business business;

	public Customer() {
	}

	public Long getId() {
		return id;
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

	public Business getBusiness() {
		return business;
	}

	public void setId(Long id) {
		this.id = id;
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

	public void setBusiness(Business business) {
		this.business = business;
	}
}