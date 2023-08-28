package com.fronchak.petshopv1.dtos.client;

import com.fronchak.petshopv1.validators.client.ClientInsertValid;

@ClientInsertValid
public class ClientInsertDTO extends ClientInputDTO {

	public ClientInsertDTO() {
	}
	
	public ClientInsertDTO(String name, String email) {
		super(name, email);
	}
}
