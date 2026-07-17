package com.yuri.api_biblioteca.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record EmprestimoRequestDto(
		@NotNull
		Long livroId,
		@NotBlank
		String nomeCliente,
		@NotNull
		@FutureOrPresent
		LocalDate dataPrevistaDevolucao

) {
}
