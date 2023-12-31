package com.fronchak.petshopv1.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fronchak.petshopv1.dtos.client.ClientDTO;
import com.fronchak.petshopv1.dtos.client.ClientInsertDTO;
import com.fronchak.petshopv1.dtos.client.ClientUpdateDTO;
import com.fronchak.petshopv1.exceptions.PathParamNotANumberException;
import com.fronchak.petshopv1.services.ClientService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/clients")
public class ClientController {

	@Autowired
	private ClientService clientService;
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<ClientDTO> findById(@PathVariable String id) {
		Long entityId = parsePath(id);
		ClientDTO clientDTO = clientService.findById(entityId);
		return ResponseEntity.ok().body(clientDTO);
	}
	
	private Long parsePath(String idStr) {
		try {
			return Long.parseLong(idStr);
		}
		catch(Exception e) {
			throw new PathParamNotANumberException("Id must be a number");
		}
	}
	
	@PostMapping
	public ResponseEntity<ClientDTO> save(@Valid @RequestBody ClientInsertDTO clientInsertDTO) {
		ClientDTO clientDTO = clientService.save(clientInsertDTO);
		return ResponseEntity.ok().body(clientDTO);
	}
	
	@PutMapping(value = "/{id}")
	public ResponseEntity<ClientDTO> update(@Valid @RequestBody ClientUpdateDTO clientUpdateDTO, @PathVariable String id) {
		Long entityId = parsePath(id);
		ClientDTO clientDTO = clientService.update(clientUpdateDTO, entityId);
		return ResponseEntity.ok().body(clientDTO);
	}
	
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> delete(@PathVariable String id) {
		Long entityId = parsePath(id);
		clientService.deleteById(entityId);
		return ResponseEntity.noContent().build();
	}
}
