package com.yuri.api_biblioteca.dto;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;


public record LivroRequestDto(
		@NotBlank String titulo,
		@NotBlank String autor,
		@NotBlank @Column(unique = true) String isbn,
		@NotNull Integer anoPublicacao,
		@NotNull @PositiveOrZero Integer quantidade

) {}

