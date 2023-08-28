package com.fronchak.petshopv1.builders;

import com.fronchak.petshopv1.dtos.client.ClientDTO;

public class ClientDTOBuilder {

	private ClientDTO client;
	
	private ClientDTOBuilder() {
		client = new ClientDTO();
	}
	
	public static ClientDTOBuilder clientDTO() {
		return new ClientDTOBuilder();
	}
	
	public ClientDTOBuilder withId(Long id) {
		client.setId(id);
		return this;
	}
	
	public ClientDTOBuilder withName(String name) {
		client.setName(name);
		return this;
	}
	
	public ClientDTOBuilder withEmail(String email) {
		client.setEmail(email);
		return this;
	}
	
	public ClientDTO get() {
		return client;
	}
}
