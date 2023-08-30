package com.fronchak.petshopv1.exceptions;

public class PathParamNotANumberException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public PathParamNotANumberException(String msg) {
		super(msg);
	}
}
