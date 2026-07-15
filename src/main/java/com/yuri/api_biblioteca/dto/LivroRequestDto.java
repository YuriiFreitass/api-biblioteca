package com.yuri.api_biblioteca.dto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;


public record LivroRequestDto(

		@Schema(
				description = "Titulo do livro",
				example = "Clean Code"
		)
		@NotBlank String titulo,
		@Schema(
				description = "Autor do livro",
				example = "Robert C. Martin"
		)
		@NotBlank String autor,
		@Schema(
				description = "ISBN único do livro",
				example = "9780132350884"
		)
		@NotBlank @Column(unique = true) String isbn,
		@Schema(
				description = "Ano de publicação",
				example = "2008"
		)
		@NotNull Integer anoPublicacao,
		@Schema(
				description = "Quantidade disponíveis",
				example = "10"
		)
		@NotNull @PositiveOrZero Integer quantidade

) {}

