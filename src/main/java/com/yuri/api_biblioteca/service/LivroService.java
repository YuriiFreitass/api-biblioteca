package com.yuri.api_biblioteca.service;


import com.yuri.api_biblioteca.dto.LivroRequestDto;
import com.yuri.api_biblioteca.dto.LivroResponseDto;
import com.yuri.api_biblioteca.entity.LivroEntity;
import com.yuri.api_biblioteca.exception.LivroNaoEncontradoException;
import com.yuri.api_biblioteca.repository.LivroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LivroService {


	private final LivroRepository livroRepository;

	public List<LivroResponseDto> findAll() {
		return livroRepository.findAll()
				.stream()
				.map(this::toResponseDto)
				.toList();
	}

	public List<LivroResponseDto> findByTitulo(String titulo) {
		return livroRepository.findByTitulo(titulo)
				.stream()
				.map(this::toResponseDto)
				.toList();
	}

	public LivroResponseDto save(LivroRequestDto livroRequestDto) {
		LivroEntity livro = new LivroEntity();

		livro.setTitulo((livroRequestDto.getTitulo()));
		livro.setAutor(livroRequestDto.getAutor());
		livro.setAnoPublicacao(livroRequestDto.getAnoPublicacao());
		livro.setIsbn(livroRequestDto.getIsbn());

		LivroEntity livroSalvo = livroRepository.save(livro);

		return toResponseDto(livroSalvo);
	}

	public LivroResponseDto update(Long id, LivroRequestDto livroRequestDto) {
		LivroEntity livro = livroRepository.findById(id)
				.orElseThrow(() -> new LivroNaoEncontradoException(id));
		livro.setTitulo(livroRequestDto.getTitulo());
		livro.setAutor(livroRequestDto.getAutor());
		livro.setIsbn(livroRequestDto.getIsbn());
		livro.setAnoPublicacao(livroRequestDto.getAnoPublicacao());

		LivroEntity livroAtualizado = livroRepository.save(livro);
		return toResponseDto(livroAtualizado);
	}

	public void deleteById(Long id) {
		LivroEntity livro = livroRepository.findById(id)
				.orElseThrow(() -> new LivroNaoEncontradoException(id));
		livroRepository.delete(livro);
	}

	private LivroResponseDto toResponseDto(LivroEntity livro) {
		return new LivroResponseDto(
				livro.getId(),
				livro.getTitulo(),
				livro.getAutor(),
				livro.getIsbn(),
				livro.getAnoPublicacao()
		);
	}

}
