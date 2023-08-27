package com.fronchak.petshopv1.dtos.client;

public class ClientUpdateDTO extends ClientInputDTO {

	public ClientUpdateDTO() {
	}
	
	public ClientUpdateDTO(String name, String email) {
		super(name, email);
	}
}
