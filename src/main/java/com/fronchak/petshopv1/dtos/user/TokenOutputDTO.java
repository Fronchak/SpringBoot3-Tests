package com.fronchak.petshopv1.dtos.user;

import java.io.Serializable;

public class TokenOutputDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String access_token;
	
	public TokenOutputDTO() {}
	
	public TokenOutputDTO(String access_token) {
		this.access_token = access_token;
	}

	public String getAccess_token() {
		return access_token;
	}

	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}
}
