package com.fronchak.petshopv1.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fronchak.petshopv1.builders.ClientInsertDTOBuilder;
import com.fronchak.petshopv1.dtos.client.ClientInputDTO;
import com.fronchak.petshopv1.repositories.ClientRepository;
import com.fronchak.petshopv1.utils.TokenUtil;

@SpringBootTest
@AutoConfigureMockMvc	
@Transactional
public class ClientControllerIT {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private TokenUtil tokenUtil;
	
	@Autowired
	private ObjectMapper mapper;
	
	@Autowired
	private ClientRepository clientRepository;
	
	private String adminEmail;
	private String adminPassword;
	private String workerEmail;
	private String workerPassword;
	private String userEmail;
	private String userPassword;
	
	private Long existingId;
	private Long nonExistingId;
	
	private String clientName;
	private String clientEmail;
	private int totalClients;
	
	private String clientNameInput;
	private String clientEmailInput;
	
	private ClientInputDTO clientInsertDTO;
	
	@BeforeEach
	void setUp() {
		adminEmail = "admin@gmail.com";
		adminPassword = "admin";
		workerEmail = "worker@gmail.com";
		workerPassword = "worker";
		userEmail = "user@gmail.com";
		userPassword = "user";
		
		existingId = 2L;
		nonExistingId = 20L;
		
		clientName = "client2";
		clientEmail = "client2@gmail.com";
		totalClients = 5;
		
		clientNameInput = "Ana";
		clientEmailInput = "ana@gmail.com";
		
		clientInsertDTO = ClientInsertDTOBuilder
				.clientInsertDTO()
				.withName(clientNameInput)
				.withEmail(clientEmailInput)
				.get();
	}
	
	private String getAdminToken() throws Exception {
		return tokenUtil.obtainAccessToken(mockMvc, adminEmail, adminPassword);
	}
	
	private String getWorkerToken() throws Exception {
		return tokenUtil.obtainAccessToken(mockMvc, workerEmail, workerPassword);
	}
	
	private String getUserToken() throws Exception {
		return tokenUtil.obtainAccessToken(mockMvc, userEmail, userPassword);
	}
	
	@Test
	public void findByIdShouldReturnForbiddenWhenUserIsNotLogged() throws Exception {
		ResultActions result = mockMvc.perform(get("/clients/{id}", nonExistingId)
				.accept(MediaType.APPLICATION_JSON));
				
		result.andExpect(status().isForbidden());	
	}
	
	@Test
	public void findByIdShouldReturnForbiddenWhenANormalUserIsLogged() throws Exception {
		String token = getUserToken();
		
		ResultActions result = mockMvc.perform(get("/clients/{id}", nonExistingId)
				.header("Authorization", "Bearer " + token)
				.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isForbidden());
	}
	
	@Test
	public void findByIdShouldReturnClientDTOWhenAWorkerIsLoggedAndIdExists() throws Exception {
		String token = getWorkerToken();
		
		ResultActions result = mockMvc.perform(get("/clients/{id}", existingId)
				.header("Authorization", "Bearer " + token)
				.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.id").value(existingId));
		result.andExpect(jsonPath("$.email").value(clientEmail));
		result.andExpect(jsonPath("$.name").value(clientName));
	}
	
	@Test
	public void findByIdShouldReturnClientDTOWhenAnAdminIsLoggedAndIdExists() throws Exception {
		String token = getAdminToken();
		
		ResultActions result = mockMvc.perform(get("/clients/{id}", existingId)
				.header("Authorization", "Bearer " + token)
				.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.id").value(existingId));
		result.andExpect(jsonPath("$.email").value(clientEmail));
		result.andExpect(jsonPath("$.name").value(clientName));
	}
	
	@Test
	public void findByIdShouldReturnNotFoundWhenAdminIsLoggedButIdDoesNotExists() throws Exception {
		String token = getAdminToken();
		
		ResultActions result = mockMvc.perform(get("/clients/{id}", nonExistingId)
				.header("Authorization", "Bearer " + token)
				.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isNotFound());
	}
	
	@Test
	public void saveShouldReturnForbiddenWhenUserIsNotLogged() throws Exception {
		String body = mapper.writeValueAsString(clientInsertDTO);
		
		ResultActions result = mockMvc.perform(post("/clients")
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(body));
		
		result.andExpect(status().isForbidden());
		assertEquals(totalClients, clientRepository.count());
	}
	
	@Test
	public void saveShouldReturnForbiddenWhenANormalUserIsLogged() throws Exception {
		String token = getUserToken();
		String body = mapper.writeValueAsString(clientInsertDTO);
		
		ResultActions result = mockMvc.perform(post("/clients")
				.header("Authorization", "Bearer " + token)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(body));
		
		result.andExpect(status().isForbidden());
		assertEquals(totalClients, clientRepository.count());
	}
	
	@Test
	public void saveShouldReturnClientDTOWhenAWorkerIsLogged() throws Exception {
		String token = getWorkerToken();
		String body = mapper.writeValueAsString(clientInsertDTO);
		
		ResultActions result = mockMvc.perform(post("/clients")
				.header("Authorization", "Bearer " + token)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(body));
		
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.id").exists());
		result.andExpect(jsonPath("$.email").value(clientEmailInput));
		result.andExpect(jsonPath("$.name").value(clientNameInput));
		assertEquals(totalClients + 1, clientRepository.count());
	}
	
	@Test
	public void saveShouldReturnClientDTOWhenAnAdminIsLogged() throws Exception {
		String token = getAdminToken();
		String body = mapper.writeValueAsString(clientInsertDTO);
		
		ResultActions result = mockMvc.perform(post("/clients")
				.header("Authorization", "Bearer " + token)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(body));
		
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.id").exists());
		result.andExpect(jsonPath("$.email").value(clientEmailInput));
		result.andExpect(jsonPath("$.name").value(clientNameInput));
		assertEquals(totalClients + 1, clientRepository.count());
	}
	
	@Test
	public void saveShouldReturnUnprocessableWhenEmailIsAlreadyUsed() throws Exception {
		String token = getWorkerToken();
		clientInsertDTO.setEmail(clientEmail);
		String body = mapper.writeValueAsString(clientInsertDTO);
		
		ResultActions result = mockMvc.perform(post("/clients")
				.header("Authorization", "Bearer " + token)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(body));
		
		result.andExpect(status().isUnprocessableEntity());
		assertEquals(totalClients, clientRepository.count());
	}
	
	@Test
	public void saveShouldReturnUnprocessableWhenEmailIsBlank() throws Exception {
		String token = getWorkerToken();
		clientInsertDTO.setEmail("  ");
		String body = mapper.writeValueAsString(clientInsertDTO);
		
		ResultActions result = mockMvc.perform(post("/clients")
				.header("Authorization", "Bearer " + token)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(body));
		
		result.andExpect(status().isUnprocessableEntity());
		assertEquals(totalClients, clientRepository.count());
	}
	
	@Test
	public void deleteShouldReturnForbiddenWhenUserIsNoLogged() throws Exception {
		ResultActions result = mockMvc.perform(delete("/clients/{id}", existingId)
				.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isForbidden());
		assertEquals(totalClients, clientRepository.count());
	}
	
	@Test
	public void deleteShouldReturnForbiddenWhenANormalUserIsLogged() throws Exception {
		String token = getUserToken();
		
		ResultActions result = mockMvc.perform(delete("/clients/{id}", existingId)
				.header("Authorization", "Bearer " + token)
				.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isForbidden());
		assertEquals(totalClients, clientRepository.count());
	}
	
	@Test
	public void deleteShouldReturnForbiddenWhenAWorkerUserIsLogged() throws Exception {
		String token = getWorkerToken();
		
		ResultActions result = mockMvc.perform(delete("/clients/{id}", existingId)
				.header("Authorization", "Bearer " + token)
				.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isForbidden());
		assertEquals(totalClients, clientRepository.count());
	}
	
	@Test
	public void deleteShouldReturnNoContentWhenAnAdminUserIsLogged() throws Exception {
		String token = getAdminToken();
		
		ResultActions result = mockMvc.perform(delete("/clients/{id}", existingId)
				.header("Authorization", "Bearer " + token)
				.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isNoContent());
		assertEquals(totalClients - 1, clientRepository.count());
		assertTrue(clientRepository.findById(existingId).isEmpty());
	}
	
	@Test
	public void deleteShouldReturnNotFoundWhenIdDoesNotExists() throws Exception {
		String token = getAdminToken();
		
		ResultActions result = mockMvc.perform(delete("/clients/{id}", nonExistingId)
				.header("Authorization", "Bearer " + token)
				.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isNotFound());
		assertEquals(totalClients, clientRepository.count());
	}
}
