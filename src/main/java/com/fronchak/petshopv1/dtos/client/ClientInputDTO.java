package com.fronchak.petshopv1.dtos.client;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ClientInputDTO	 {

	@NotNull(message = "Name is required")
	@NotBlank(message = "Name is required")
	private String name;
	
	@NotNull(message = "Email is required")
	@Email(message = "Invalid email", 
		regexp = "^\\w+([\\.-]?\\w+)*@\\w+([\\.-]?\\w+)*(\\.\\w{2,3})+$")
	private String email;

	public ClientInputDTO() {
	}
	
	public ClientInputDTO(String name, String email) {
		this.name = name;
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
}
