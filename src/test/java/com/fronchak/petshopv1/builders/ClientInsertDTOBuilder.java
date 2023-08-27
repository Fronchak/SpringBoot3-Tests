package com.fronchak.petshopv1.builders;

import com.fronchak.petshopv1.dtos.client.ClientInsertDTO;

public class ClientInsertDTOBuilder {

	private ClientInsertDTO clientDTO;
	
	private ClientInsertDTOBuilder() {
		clientDTO = new ClientInsertDTO();
	}
	
	public static ClientInsertDTOBuilder clientInsertDTO() {
		return new ClientInsertDTOBuilder();
	}
	
	public ClientInsertDTOBuilder withName(String name) {
		clientDTO.setName(name);
		return this;
	}
	
	public ClientInsertDTOBuilder withEmail(String email) {
		clientDTO.setEmail(email);
		return this;
	}
	
	public ClientInsertDTO get() {
		return clientDTO;
	}
}
