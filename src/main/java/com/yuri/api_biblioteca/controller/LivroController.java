package com.yuri.api_biblioteca.controller;

import com.yuri.api_biblioteca.dto.LivroRequestDto;
import com.yuri.api_biblioteca.dto.LivroResponseDto;
import com.yuri.api_biblioteca.service.LivroService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Tag(name = "Livros", description = "Endpoints para gerenciamento de livros")
@RestController
@RequestMapping("/v1/livros")
@RequiredArgsConstructor
public class LivroController {

	private final LivroService livroService;

	@Operation(summary = "Listar livros")
	@ApiResponse(responseCode = "200", description = "Livros retornados com sucesso")
	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public Page<LivroResponseDto> findAll(@ParameterObject @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
		return livroService.findAll(pageable);
	}

	@Operation(summary = "Listar livros")
	@ApiResponse(responseCode = "200", description = "Busca realizada com sucesso")
	@ResponseStatus(HttpStatus.OK)
	@GetMapping("/titulo")
	public List<LivroResponseDto> findByTitulo(@RequestParam String titulo) {
		return livroService.findByTitulo(titulo);
	}

	@Operation(summary = "Cadastrar livro")
	@ApiResponse(responseCode = "201", description = "Livro  cadastrado com sucesso")
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public LivroResponseDto save(@Valid @RequestBody LivroRequestDto livroRequestDto) {
		return livroService.save(livroRequestDto);
	}

	@Operation(summary = "Atualizar livro")
	@ApiResponse(responseCode = "200", description = "Livro atualizado com sucesso")
	@PutMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	public LivroResponseDto update(@PathVariable Long id, @Valid @RequestBody LivroRequestDto livroRequestDto) {
		return livroService.update(id, livroRequestDto);
	}

	@Operation(summary = "Buscar livro por ISBN")
	@ApiResponse(responseCode = "200", description = "Livro encontrado")
	@GetMapping("/isbn/{isbn}")
	@ResponseStatus(HttpStatus.OK)
	public LivroResponseDto findByIsbn(@PathVariable String isbn) {
		return livroService.findByIsbn(isbn);
	}

	@Operation(summary = "Excluir livro")
	@ApiResponse(responseCode = "204", description = "Livro removido com sucesso")
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable Long id) {
		livroService.deleteById(id);
	}


}
