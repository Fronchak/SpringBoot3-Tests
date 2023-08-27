package com.fronchak.petshopv1.services;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.fronchak.petshopv1.builders.ClientBuilder;
import com.fronchak.petshopv1.builders.ClientInsertDTOBuilder;
import com.fronchak.petshopv1.builders.ClientUpdateDTOBuilder;
import com.fronchak.petshopv1.dtos.client.ClientDTO;
import com.fronchak.petshopv1.dtos.client.ClientInsertDTO;
import com.fronchak.petshopv1.dtos.client.ClientUpdateDTO;
import com.fronchak.petshopv1.entities.Client;
import com.fronchak.petshopv1.exceptions.DatabaseException;
import com.fronchak.petshopv1.exceptions.ResourceNotFoundException;
import com.fronchak.petshopv1.repositories.ClientRepository;

import jakarta.persistence.EntityNotFoundException;

@ExtendWith(SpringExtension.class)
public class ClientServiceTest {

	@InjectMocks
	private ClientService clientService;
	
	@Mock
	private ClientRepository clientRepository;
	
	private Long existingId;
	private Long nonExistingId;
	private Long nonDeletableId;
	
	private Long clientId;
	private String clientName = "Maria Clara";
	private String clientEmail = "mariaclara@gmail.com";
	
	private Client client;
	
	@BeforeEach
	void setUp() {
		existingId = 10L;
		nonExistingId = 20L;
		nonDeletableId = 30L;
		
		clientId = 40L;
		clientName = "Maria Clara";
		clientEmail = "mariaclara@gmail.com";
		
		client = ClientBuilder.client()
				.withId(clientId)
				.withName(clientName)
				.withEmail(clientEmail)
				.get();
		Client otherClient = ClientBuilder.client()
				.withId(clientId)
				.withName(clientName)
				.withEmail(clientEmail)
				.get();
		
		when(clientRepository.findById(nonExistingId)).thenReturn(Optional.empty());
		when(clientRepository.findById(existingId)).thenReturn(Optional.of(client));
		when(clientRepository.getReferenceById(existingId)).thenReturn(otherClient);
		doThrow(EntityNotFoundException.class).when(clientRepository).getReferenceById(nonExistingId);
		when(clientRepository.save(any())).thenReturn(client);
		doThrow(EmptyResultDataAccessException.class).when(clientRepository).deleteById(nonExistingId);
		doThrow(DataIntegrityViolationException.class).when(clientRepository).deleteById(nonDeletableId);
		doNothing().when(clientRepository).deleteById(existingId);
	}
	
	@Test
	public void findByIdShouldThrowResouceNotFoundWhenIdDoesNotExists() {
		assertThrows(ResourceNotFoundException.class, () -> {
			clientService.findById(nonExistingId);
		});
	}	
	
	@Test
	public void findByIdShouldReturnClientDTOWhenIdExists() {
		ClientDTO result = clientService.findById(existingId);
		
		assertEquals(clientId, result.getId());
		assertEquals(clientName, result.getName());
		assertEquals(clientEmail, result.getEmail());
	}
	
	@Test
	public void saveShouldReturnClientDTOAfterSaveEntity() {
		String name = "Ana";
		String email = "ana@gmail.com";
		ClientInsertDTO clientInsertDTO = ClientInsertDTOBuilder
				.clientInsertDTO()
				.withName(name)
				.withEmail(email)
				.get();
		ArgumentCaptor<Client> argumentCaptor = ArgumentCaptor.forClass(Client.class);
		
		ClientDTO result = clientService.save(clientInsertDTO);
		
		verify(clientRepository).save(argumentCaptor.capture());
		Client clientInserted = argumentCaptor.getValue();
		assertNotNull(clientInserted);
		assertNull(clientInserted.getId());
		assertEquals(name, clientInserted.getName());
		assertEquals(email, clientInserted.getEmail());
		
		assertEquals(clientId, result.getId());
		assertEquals(clientName, result.getName());
		assertEquals(clientEmail, result.getEmail());
	}
	
	@Test
	public void updateShouldReturnClientDTOAfterUpdateEntityWhenIdExists() {
		String name = "Ana";
		String email = "ana@gmail.com";
		ClientUpdateDTO clientUpdateDTO = ClientUpdateDTOBuilder
				.clientInsertDTO()
				.withName(name)
				.withEmail(email)
				.get();
		ArgumentCaptor<Client> argumentCaptor = ArgumentCaptor.forClass(Client.class);
		
		ClientDTO result = clientService.update(clientUpdateDTO, existingId);
		
		verify(clientRepository).save(argumentCaptor.capture());
		Client clientInserted = argumentCaptor.getValue();
		assertNotNull(clientInserted);
		assertEquals(clientId, clientInserted.getId());
		assertEquals(name, clientInserted.getName());
		assertEquals(email, clientInserted.getEmail());
		
		assertEquals(clientId, result.getId());
		assertEquals(clientName, result.getName());
		assertEquals(clientEmail, result.getEmail());
	}
	
	@Test
	public void updateShouldThrowResourceNotFoundExceptionWhenIdDoesNotExists() {
		assertThrows(ResourceNotFoundException.class, () -> {
			clientService.update(new ClientUpdateDTO(), nonExistingId);
		});
		verify(clientRepository, never()).save(any());
	}
	
	@Test
	public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExists() {
		assertThrows(ResourceNotFoundException.class, () -> {
			clientService.deleteById(nonExistingId);
		});
		verify(clientRepository, times(1)).deleteById(nonExistingId);
	}
	
	@Test
	public void deleteShouldThrowDatabaseExceptionWhenIdCannotBeDeleted() {
		assertThrows(DatabaseException.class, () -> {
			clientService.deleteById(nonDeletableId);
		});
		verify(clientRepository, times(1)).deleteById(nonDeletableId);
	}
	
	@Test
	public void deleteShouldNotThrowAnExceptionWhenClientCanBeDeleted() {
		assertDoesNotThrow(() -> clientService.deleteById(existingId));
		verify(clientRepository, times(1)).deleteById(existingId);
	}
}
