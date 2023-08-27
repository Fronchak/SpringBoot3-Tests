package com.fronchak.petshopv1.builders;

import com.fronchak.petshopv1.entities.Client;

public class ClientBuilder {

	private Client client;
	
	private ClientBuilder() {
		client = new Client();
	}
	
	public static ClientBuilder client() {
		return new ClientBuilder();
	}
	
	public ClientBuilder withId(Long id) {
		client.setId(id);
		return this;
	}
	
	public ClientBuilder withName(String name) {
		client.setName(name);
		return this;
	}
	
	public ClientBuilder withEmail(String email) {
		client.setEmail(email);
		return this;
	}
	
	public Client get() {
		return client;
	}
}
