package com.yuri.api_biblioteca.service;

import com.yuri.api_biblioteca.entity.UsuarioEntity;
import com.yuri.api_biblioteca.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

	private final UsuarioRepository usuarioRepository;

	@Override
	public UserDetails loadUserByUsername(String username)
		throws UsernameNotFoundException {

		UsuarioEntity usuario = usuarioRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));

		return User.builder()
				.username(usuario.getUsername())
				.password(usuario.getPassword())
				.authorities(List.of(new SimpleGrantedAuthority(usuario.getRole())))
				.build();
	}
}
