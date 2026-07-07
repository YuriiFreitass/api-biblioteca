package com.yuri.api_biblioteca.controller;

import com.yuri.api_biblioteca.dto.LivroRequestDto;
import com.yuri.api_biblioteca.dto.LivroResponseDto;
import com.yuri.api_biblioteca.entity.LivroEntity;
import com.yuri.api_biblioteca.service.LivroService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/livros")
@RequiredArgsConstructor
@Validated
public class LivroController {

	private final LivroService livroService;

	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public List<LivroResponseDto> findAll() {
		return livroService.findAll();
	}

	@ResponseStatus(HttpStatus.OK)
	@GetMapping("/buscar")
	public List<LivroResponseDto> findByTitulo(@RequestParam String titulo) {
		return livroService.findByTitulo(titulo);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public LivroResponseDto save(@Valid @RequestBody LivroRequestDto livroRequestDto) {
		return livroService.save(livroRequestDto);
	}


}
