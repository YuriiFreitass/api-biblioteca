package com.yuri.api_biblioteca.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;


public record LivroRequestDto(
		@NotBlank String titulo,
		@NotBlank String autor,
		@NotBlank String isbn,
		@NonNull Integer anoPublicacao,
		@NonNull Integer quantidade

) {}

