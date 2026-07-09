package com.yuri.api_biblioteca.dto;

public record ErrorResponseDto(
	int status,
	String mensagem
) {}