package com.fronchak.petshopv1.validators.client;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.fronchak.petshopv1.dtos.client.ClientInsertDTO;
import com.fronchak.petshopv1.entities.Client;
import com.fronchak.petshopv1.exceptions.handler.FieldMessage;
import com.fronchak.petshopv1.repositories.ClientRepository;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ClientInsertValidator implements ConstraintValidator<ClientInsertValid, ClientInsertDTO> {

	@Autowired
	private ClientRepository clientRepository;
	
	@Override
	public boolean isValid(ClientInsertDTO insertDTO, ConstraintValidatorContext context) {
		
		List<FieldMessage> errors = new ArrayList<>();
		
		Client client = clientRepository.findByEmail(insertDTO.getEmail());
		if(client != null) {
			errors.add(new FieldMessage("email", "Email is already been used"));
		}
		
		for(FieldMessage e : errors) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(e.getMessage())
					.addPropertyNode(e.getFieldName())
					.addConstraintViolation();
		}
		
		return errors.isEmpty();
	}

}
