package com.yuri.api_biblioteca.controller;

import com.yuri.api_biblioteca.dto.EmprestimoReponseDto;
import com.yuri.api_biblioteca.dto.EmprestimoRequestDto;
import com.yuri.api_biblioteca.service.EmprestimoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/emprestimos")
@RequiredArgsConstructor
public class EmprestimoController {

	private final EmprestimoService emprestimoService;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public EmprestimoReponseDto realizarEmprestimo(@Valid @RequestBody EmprestimoRequestDto emprestimoRequestDto) {
		return emprestimoService.realizarEmprestimo(emprestimoRequestDto);
	}

	@PutMapping("/{id}/devolucao")
	public EmprestimoReponseDto devolverLivro(@PathVariable Long id) {
		return emprestimoService.devolverLivro(id);
	}
}
