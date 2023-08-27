package com.fronchak.petshopv1.dtos.client;

public class ClientInsertDTO extends ClientInputDTO {

	public ClientInsertDTO() {
	}
	
	public ClientInsertDTO(String name, String email) {
		super(name, email);
	}
}
