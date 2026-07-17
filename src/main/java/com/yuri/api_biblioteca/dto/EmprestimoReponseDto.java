package com.yuri.api_biblioteca.dto;

import com.yuri.api_biblioteca.enums.StatusEmprestimo;

import java.time.LocalDate;

public record EmprestimoReponseDto(
		Long id,
		String nomeCliente,
		LocalDate dataEmprestimo,
		LocalDate dataPrevistaDevolucao,
		LocalDate dataDevolucao,
		StatusEmprestimo status,
		Long livroId,
		String tituloLivro
) {}
