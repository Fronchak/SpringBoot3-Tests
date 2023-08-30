package com.fronchak.petshopv1.validators.client;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerMapping;

import com.fronchak.petshopv1.dtos.client.ClientUpdateDTO;
import com.fronchak.petshopv1.entities.Client;
import com.fronchak.petshopv1.exceptions.handler.FieldMessage;
import com.fronchak.petshopv1.repositories.ClientRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ClientUpdateValidator implements ConstraintValidator<ClientUpdateValid, ClientUpdateDTO> {

	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	private ClientRepository clientRepository;
	
	@Override
	public boolean isValid(ClientUpdateDTO insertDTO, ConstraintValidatorContext context) {
		
		List<FieldMessage> errors = new ArrayList<>();
		
		Map<String, String> uriVars = (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
		Long id = null;
		try {
			id = Long.parseLong(uriVars.get("id"));			
		}
		catch(Exception e) {}
		
		Client client = clientRepository.findByEmail(insertDTO.getEmail());
		if(client != null && !client.getId().equals(id)) {
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
