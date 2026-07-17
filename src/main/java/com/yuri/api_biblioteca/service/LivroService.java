package com.yuri.api_biblioteca.service;


import com.yuri.api_biblioteca.dto.LivroRequestDto;
import com.yuri.api_biblioteca.dto.LivroResponseDto;
import com.yuri.api_biblioteca.entity.LivroEntity;
import com.yuri.api_biblioteca.exception.IsbnDuplicadoException;
import com.yuri.api_biblioteca.exception.LivroNaoEncontradoException;
import com.yuri.api_biblioteca.mapper.LivroMapper;
import com.yuri.api_biblioteca.repository.LivroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import java.util.List;

@Service
@RequiredArgsConstructor
public class LivroService {


	private final LivroRepository livroRepository;
	private final LivroMapper livroMapper;

	public Page<LivroResponseDto> findAll(Pageable pageable) {
		return livroRepository.findAll(pageable)
				.map(livroMapper::toResponseDto);

	}

	public List<LivroResponseDto> findByTitulo(String titulo) {
		return livroRepository.findByTitulo(titulo)
				.stream()
				.map(livroMapper::toResponseDto)
				.toList();
	}

	public LivroResponseDto findByIsbn(String isbn) {
		LivroEntity livro = livroRepository.findByIsbn(isbn)
				.orElseThrow(() -> new LivroNaoEncontradoException("Livro não encontrado com ISBN" + isbn + " não foi encontrado"));
		return livroMapper.toResponseDto(livro);
	}

	public LivroResponseDto findById(Long id) {
		LivroEntity livro = livroRepository.findById(id)
				.orElseThrow(() -> new LivroNaoEncontradoException("Livro não encontado com o ID " + id));
		return livroMapper.toResponseDto(livro);
	}

	public LivroResponseDto save(LivroRequestDto livroRequestDto) {
		if (livroRepository.existsByIsbn(livroRequestDto.isbn())) {
			throw new IsbnDuplicadoException("ISBN já cadastrado");
		}
		LivroEntity livro = livroMapper.toEntity(livroRequestDto);
		LivroEntity livroSalvo = livroRepository.save(livro);
		return livroMapper.toResponseDto(livroSalvo);
	}

	public LivroResponseDto update(Long id, LivroRequestDto livroRequestDto) {
		if (livroRepository.existsByIsbnAndIdNot((livroRequestDto.isbn()), id)) {
			throw  new IsbnDuplicadoException("ISBN já cadastrado");
		}
		LivroEntity livroExistente = livroRepository.findById(id)
				.orElseThrow(() -> new LivroNaoEncontradoException("Livro com o ID " + id + " não foi encontrado"));

		livroMapper.updateEntityFromDto(livroRequestDto, livroExistente);

		LivroEntity livroAtualizado = livroRepository.save(livroExistente);

		return livroMapper.toResponseDto(livroAtualizado);
	}

	public void deleteById(Long id) {
		LivroEntity livro = livroRepository.findById(id)
				.orElseThrow(() -> new LivroNaoEncontradoException("Livro com o ID " + id + " não foi encontrado"));
		livroRepository.delete(livro);
	}

}
