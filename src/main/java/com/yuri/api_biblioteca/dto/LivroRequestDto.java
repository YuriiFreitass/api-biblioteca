package com.yuri.api_biblioteca.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class LivroRequestDto {
	@NotBlank
	private String titulo;
	@NotBlank
	private String autor;
	@NotBlank
	private String isbn;
	@NonNull
	private Integer anoPublicacao;
}
