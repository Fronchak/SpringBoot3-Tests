package com.fronchak.petshopv1.dtos.client;

import com.fronchak.petshopv1.validators.client.ClientUpdateValid;

@ClientUpdateValid
public class ClientUpdateDTO extends ClientInputDTO {

	public ClientUpdateDTO() {
	}
	
	public ClientUpdateDTO(String name, String email) {
		super(name, email);
	}
}
