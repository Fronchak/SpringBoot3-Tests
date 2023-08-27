package com.fronchak.petshopv1.builders;

import com.fronchak.petshopv1.dtos.client.ClientUpdateDTO;

public class ClientUpdateDTOBuilder {

	private ClientUpdateDTO clientDTO;
	
	private ClientUpdateDTOBuilder() {
		clientDTO = new ClientUpdateDTO();
	}
	
	public static ClientUpdateDTOBuilder clientInsertDTO() {
		return new ClientUpdateDTOBuilder();
	}
	
	public ClientUpdateDTOBuilder withName(String name) {
		clientDTO.setName(name);
		return this;
	}
	
	public ClientUpdateDTOBuilder withEmail(String email) {
		clientDTO.setEmail(email);
		return this;
	}
	
	public ClientUpdateDTO get() {
		return clientDTO;
	}
}
