package com.yuri.api_biblioteca.dto;

import java.util.List;

public record ErrorResponseDto(
	int status,
	String mensagem,
	List<CampoErroDto> campos
) {}