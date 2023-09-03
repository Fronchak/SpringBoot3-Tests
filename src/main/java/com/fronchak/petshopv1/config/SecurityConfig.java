package com.fronchak.petshopv1.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	private static final String ADMIN = "ADMIN";
	private static final String WORKER = "WORKER";
	
	private static final String CLIENTS_PATH = "/clients/**";

	@Autowired
	private JwtAuthenticationFilter jwtAuthFilter;
	
	@Autowired
	private AuthenticationProvider authenticationProvider;
	
	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.headers((headers) -> {
			headers.frameOptions((frameOptions) -> frameOptions.disable());
		});
		http.cors((cors) -> cors.disable());
		http.csrf((csrf) -> csrf.disable());
		http.authorizeHttpRequests((authorizeHttpRequests) -> {
			
			authorizeHttpRequests

					.requestMatchers(antMatcher(HttpMethod.GET, CLIENTS_PATH)).hasAnyRole(WORKER, ADMIN)
					.requestMatchers(antMatcher(HttpMethod.POST, CLIENTS_PATH)).hasAnyRole(WORKER, ADMIN)
					.requestMatchers(antMatcher(HttpMethod.DELETE, "/clients/{id}")).hasAnyRole(ADMIN)
					.anyRequest().permitAll();
		});
		http.sessionManagement((sessionConfig) -> sessionConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
		http.authenticationProvider(authenticationProvider);
		http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}

	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration().applyPermitDefaultValues();
		configuration.setAllowedMethods(Arrays.asList("POST", "GET", "PUT", "DELETE", "OPTIONS"));
		final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}
}
