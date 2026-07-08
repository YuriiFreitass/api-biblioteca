package com.yuri.api_biblioteca.dto;

public record LivroResponseDto(
		Long id,
		String titulo,
		String autor,
		String isbn,
		Integer anoPublicacao,
		Integer quantidade

) {}
