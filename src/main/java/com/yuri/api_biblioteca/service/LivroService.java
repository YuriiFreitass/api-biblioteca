package com.yuri.api_biblioteca.service;


import com.yuri.api_biblioteca.dto.LivroRequestDto;
import com.yuri.api_biblioteca.dto.LivroResponseDto;
import com.yuri.api_biblioteca.entity.LivroEntity;
import com.yuri.api_biblioteca.exception.LivroNaoEncontradoException;
import com.yuri.api_biblioteca.mapper.LivroMapper;
import com.yuri.api_biblioteca.repository.LivroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LivroService {


	private final LivroRepository livroRepository;
	private final LivroMapper livroMapper;

	public List<LivroResponseDto> findAll() {
		return livroRepository.findAll()
				.stream()
				.map(livroMapper::toResponseDto)
				.toList();
	}

	public List<LivroResponseDto> findByTitulo(String titulo) {
		return livroRepository.findByTitulo(titulo)
				.stream()
				.map(livroMapper::toResponseDto)
				.toList();
	}

	public LivroResponseDto save(LivroRequestDto livroRequestDto) {
		LivroEntity livro = livroMapper.toEntity(livroRequestDto);

		LivroEntity livroSalvo = livroRepository.save(livro);
		return livroMapper.toResponseDto(livroSalvo);

	}

	public LivroResponseDto update(Long id, LivroRequestDto livroRequestDto) {
		LivroEntity livro = livroRepository.findById(id)
				.orElseThrow(() -> new LivroNaoEncontradoException(id));

		livroMapper.updateEntityFromDto(livroRequestDto, livro);

		LivroEntity atualizado = livroRepository.save(livro);

		return livroMapper.toResponseDto(atualizado);
	}

	public void deleteById(Long id) {
		LivroEntity livro = livroRepository.findById(id)
				.orElseThrow(() -> new LivroNaoEncontradoException(id));
		livroRepository.delete(livro);
	}

}
