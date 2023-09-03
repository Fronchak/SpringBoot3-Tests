package com.fronchak.petshopv1.utils;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fronchak.petshopv1.dtos.user.UserLoginDTO;

@Component
public class TokenUtil {
	
	@Autowired
	private ObjectMapper mapper;

	public String obtainAccessToken(MockMvc mockMvc, String username, String password) throws Exception {
	
		UserLoginDTO loginDTO = new UserLoginDTO();
		loginDTO.setEmail(username);
		loginDTO.setPassword(password);
		String body = mapper.writeValueAsString(loginDTO);
		
		ResultActions result = mockMvc
				.perform(post("/auth/login")
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON)
						.content(body));
		
		result.andExpect(status().isOk());
		String resultString = result.andReturn().getResponse().getContentAsString();
		JacksonJsonParser jsonParser = new JacksonJsonParser();
		return jsonParser.parseMap(resultString).get("access_token").toString();
	}
}
