package com.fronchak.petshopv1.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import com.fronchak.petshopv1.dtos.client.ClientDTO;
import com.fronchak.petshopv1.dtos.client.ClientInputDTO;
import com.fronchak.petshopv1.dtos.client.ClientInsertDTO;
import com.fronchak.petshopv1.dtos.client.ClientUpdateDTO;
import com.fronchak.petshopv1.entities.Client;
import com.fronchak.petshopv1.exceptions.DatabaseException;
import com.fronchak.petshopv1.exceptions.ResourceNotFoundException;
import com.fronchak.petshopv1.repositories.ClientRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ClientService {

	@Autowired
	private ClientRepository clientRepository;
	
	@Transactional(readOnly = true)
	public ClientDTO findById(Long id) {
		Client client = clientRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Client not found"));
		return mapToClientDTO(client);
	}
	
	private static ClientDTO mapToClientDTO(Client client) {
		return new ClientDTO(client.getId(), 
				client.getName(), 
				client.getEmail());
	}
	
	@Transactional
	public ClientDTO save(ClientInsertDTO clientInsertDTO) {
		Client client = new Client();
		copyToClient(clientInsertDTO, client);
		client = clientRepository.save(client);
		return mapToClientDTO(client);
	}
	
	private static void copyToClient(ClientInputDTO clientInputDTO, Client client) {
		client.setName(clientInputDTO.getName());
		client.setEmail(clientInputDTO.getEmail());
	}
	
	@Transactional
	public ClientDTO update(ClientUpdateDTO clientUpdateDTO, Long id) {
		try {
			Client client = clientRepository.getReferenceById(id);
			copyToClient(clientUpdateDTO, client);
			Client client2 = clientRepository.save(client);
			return mapToClientDTO(client2);			
		}
		catch(EntityNotFoundException e) {
			throw new ResourceNotFoundException("Client not found");
		}
	}
	
	public void deleteById(Long id) {
		try {
			clientRepository.deleteById(id);
		}
		catch(EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException("Client not found");
		}
		catch(DataIntegrityViolationException e) {
			throw new DatabaseException("Client cannot be deleted");
		}
	}
}
