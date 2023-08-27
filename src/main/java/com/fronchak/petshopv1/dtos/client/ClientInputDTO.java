package com.fronchak.petshopv1.dtos.client;

public class ClientInputDTO	 {

	private String name;
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
