package com.yuri.api_biblioteca.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.csrf(csrf -> csrf.disable())
				.authorizeHttpRequests(auth -> auth.requestMatchers(
										"/swagger-ui/**",
										"/v3/api-docs/**"
								).authenticated()
								.requestMatchers(HttpMethod.GET, "/v1/livros/**")
								.hasAnyRole("USER", "ADMIN")
								.requestMatchers(HttpMethod.POST, "/v1/livros/**")
								.hasRole("ADMIN")
								.requestMatchers(HttpMethod.DELETE, "/v1/livros/**")
								.hasRole("ADMIN")

								.anyRequest()
								.authenticated()
				).httpBasic(Customizer.withDefaults());
		return http.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

}
