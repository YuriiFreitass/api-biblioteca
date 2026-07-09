package com.yuri.api_biblioteca.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;


public record LivroRequestDto(
		@NotBlank String titulo,
		@NotBlank String autor,
		@NotBlank String isbn,
		@NotNull Integer anoPublicacao,
		@NotNull @PositiveOrZero Integer quantidade

) {}

