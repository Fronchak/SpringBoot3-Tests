package com.fronchak.petshopv1.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fronchak.petshopv1.builders.ClientBuilder;
import com.fronchak.petshopv1.builders.ClientDTOBuilder;
import com.fronchak.petshopv1.builders.ClientInsertDTOBuilder;
import com.fronchak.petshopv1.builders.ClientUpdateDTOBuilder;
import com.fronchak.petshopv1.dtos.client.ClientDTO;
import com.fronchak.petshopv1.dtos.client.ClientInputDTO;
import com.fronchak.petshopv1.dtos.client.ClientUpdateDTO;
import com.fronchak.petshopv1.entities.Client;
import com.fronchak.petshopv1.exceptions.ResourceNotFoundException;
import com.fronchak.petshopv1.repositories.ClientRepository;
import com.fronchak.petshopv1.services.ClientService;

@WebMvcTest(ClientController.class)
public class ClientControllerTest {

	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper mapper;

	private Long existingId;
	private Long nonExistingId;
	private Long nonDeletableId;
	
	private String clientNameInput = "Ana";
	private String clientEmailInput = "ana@gmail.com";
	
	private Long clientIdOutput = 40L;
	private String clientNameOutput = "Maria";
	private String clientEmailOutput = "maria@gmail.com";
	
	private ClientInputDTO clientInsertDTO;
	private ClientUpdateDTO clientUpdateDTO;
	private ClientDTO clientDTO;
	
	private String existingEmail;
	
	@MockBean
	private ClientService clientService;
	
	@MockBean
	private ClientRepository clientRepository;
	
	@BeforeEach
	void setUp() {
		existingId = 10L;
		nonExistingId = 20L;
		nonDeletableId = 30L;
		
		existingEmail = "jose@gmail.com";
		
		clientInsertDTO = ClientInsertDTOBuilder
				.clientInsertDTO()
				.withName(clientNameInput)
				.withEmail(clientEmailInput)
				.get();
		clientUpdateDTO = ClientUpdateDTOBuilder
				.clientInsertDTO()
				.withName(clientNameInput)
				.withEmail(clientEmailInput)
				.get();
		clientDTO = ClientDTOBuilder
				.clientDTO()
				.withId(clientIdOutput)
				.withName(clientNameOutput)
				.withEmail(clientEmailOutput)
				.get();
				
		doThrow(ResourceNotFoundException.class).when(clientService).findById(nonExistingId);
		when(clientService.findById(existingId)).thenReturn(clientDTO);
		when(clientService.save(any())).thenReturn(clientDTO);
	}
	
	@Test
	public void findByIdShouldReturnNotFoundWhenIdDoesNotExists() throws Exception {
		ResultActions result = mockMvc.perform(get("/clients/{id}", nonExistingId)
				.accept(MediaType.APPLICATION_JSON));
		result.andExpect(status().isNotFound());
	}
	
	@Test
	public void findByIdShouldReturnClientDTOWhenIdExists() throws Exception {
		ResultActions result = mockMvc.perform(get("/clients/{id}", existingId)
				.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.id").value(clientIdOutput));
		result.andExpect(jsonPath("$.name").value(clientNameOutput));
		result.andExpect(jsonPath("$.email").value(clientEmailOutput));
	}
	
	@Test
	public void saveShouldReturnUnprocessableWhenNameIsNull() throws Exception {
		clientInsertDTO.setName(null);
		String body = mapper.writeValueAsString(clientInsertDTO);
	
		ResultActions result = mockMvc.perform(post("/clients")
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(body));
		
		result.andExpect(status().isUnprocessableEntity());
		result.andExpect(jsonPath("$.errors[0].fieldName").value("name"));
		result.andExpect(jsonPath("$.errors[0].message").value("Name is required"));
	}
	
	@Test
	public void saveShouldReturnUnprocessableWhenNameIsBlank() throws Exception {
		clientInsertDTO.setName("         ");
		String body = mapper.writeValueAsString(clientInsertDTO);
	
		ResultActions result = mockMvc.perform(post("/clients")
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(body));
		
		result.andExpect(status().isUnprocessableEntity());
		result.andExpect(jsonPath("$.errors[0].fieldName").value("name"));
		result.andExpect(jsonPath("$.errors[0].message").value("Name is required"));
	}
	
	@Test
	public void saveShouldReturnUnprocessableWhenEmailIsNull() throws Exception {
		clientInsertDTO.setEmail(null);
		String body = mapper.writeValueAsString(clientInsertDTO);
		
		ResultActions result = mockMvc.perform(post("/clients")
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(body));
		
		result.andExpect(status().isUnprocessableEntity());
		result.andExpect(jsonPath("$.errors[0].fieldName").value("email"));
		result.andExpect(jsonPath("$.errors[0].message").value("Email is required"));
	}
	
	@Test
	public void saveShouldReturnUnprocessableWhenEmailIsBlank() throws Exception {
		clientInsertDTO.setEmail("   ");
		String body = mapper.writeValueAsString(clientInsertDTO);
		
		ResultActions result = mockMvc.perform(post("/clients")
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(body));
		
		result.andExpect(status().isUnprocessableEntity());
		result.andExpect(jsonPath("$.errors[0].fieldName").value("email"));
		result.andExpect(jsonPath("$.errors[0].message").value("Email is required"));
	}
	
	@Test
	public void saveShouldReturnUnprocessableWhenEmailIsNotValid() throws Exception {
		clientInsertDTO.setEmail("ana@gmail.");
		String body = mapper.writeValueAsString(clientInsertDTO);
		
		ResultActions result = mockMvc.perform(post("/clients")
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(body));
		
		result.andExpect(status().isUnprocessableEntity());
		result.andExpect(jsonPath("$.errors[0].fieldName").value("email"));
		result.andExpect(jsonPath("$.errors[0].message").value("Invalid email"));
	}
	
	@Test
	public void saveShouldReturnUnprocessableWhenEmailAlreadyExists() throws Exception {
		Client client = ClientBuilder.client().get();
		when(clientRepository.findByEmail(existingEmail)).thenReturn(client);
		clientInsertDTO.setEmail(existingEmail);
		String body = mapper.writeValueAsString(clientInsertDTO);
		
		ResultActions result = mockMvc.perform(post("/clients")
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(body));
		
		result.andExpect(status().isUnprocessableEntity());
		result.andExpect(jsonPath("$.errors[0].fieldName").value("email"));
		result.andExpect(jsonPath("$.errors[0].message").value("Email is already been used"));
	}
}
