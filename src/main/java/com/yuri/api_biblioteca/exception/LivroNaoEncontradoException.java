package com.yuri.api_biblioteca.exception;

public class LivroNaoEncontradoException extends RuntimeException {

	public LivroNaoEncontradoException(Long id) {
		super("Livro com id " + id + " não encontrado.");
	}
}

