package com.yuri.api_biblioteca.service;

import com.yuri.api_biblioteca.dto.LivroRequestDto;
import com.yuri.api_biblioteca.dto.LivroResponseDto;
import com.yuri.api_biblioteca.entity.LivroEntity;
import com.yuri.api_biblioteca.exception.IsbnDuplicadoException;
import com.yuri.api_biblioteca.exception.LivroNaoEncontradoException;
import com.yuri.api_biblioteca.mapper.LivroMapper;
import com.yuri.api_biblioteca.repository.LivroRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LivroServiceTest {

	@Mock
	private LivroRepository livroRepository;

	@Mock
	private LivroMapper livroMapper;

	@InjectMocks
	private LivroService livroService;

	@Test
	void deveSalvarLivroComSucesso() {

		LivroRequestDto request = criarLivroRequestDto();
		LivroEntity entity = criarLivroEntity();
		LivroResponseDto response = criarLivroResponseDto();

		when(livroRepository.existsByIsbn(request.isbn())).thenReturn(false);
		when(livroMapper.toEntity(request)).thenReturn(entity);
		when(livroRepository.save(entity)).thenReturn(entity);
		when(livroMapper.toResponseDto(entity)).thenReturn(response);

		LivroResponseDto resultado = livroService.save(request);

		assertNotNull(resultado);
		assertEquals(response.id(), resultado.id());
		assertEquals(response.titulo(), resultado.titulo());
		assertEquals(response.autor(), resultado.autor());
		assertEquals(response.isbn(), resultado.isbn());
		assertEquals(response.anoPublicacao(), resultado.anoPublicacao());
		assertEquals(response.quantidade(), resultado.quantidade());

		verify(livroRepository).save(entity);
	}

	@Test
	void deveLancarIsbnDuplicadoExceptionQuandoIsbnJaExistir() {

		LivroRequestDto request = criarLivroRequestDto();

		when(livroRepository.existsByIsbn(request.isbn())).thenReturn(true);

		IsbnDuplicadoException exception = assertThrows(
				IsbnDuplicadoException.class,
				() -> livroService.save(request)
		);

		assertEquals("ISBN já cadastrado", exception.getMessage());

		verify(livroRepository, never()).save(any());
	}

	@Test
	void deveRetornarPaginaDeLivros() {

		LivroEntity livro1 = criarLivroEntity();
		LivroEntity livro2 = criarSegundoLivroEntity();

		LivroResponseDto dto1 = criarLivroResponseDto();
		LivroResponseDto dto2 = criarSegundoLivroResponseDto();

		Pageable pageable = PageRequest.of(0, 10);

		Page<LivroEntity> pagina = new PageImpl<>(
				List.of(livro1, livro2),
				pageable,
				2
		);

		when(livroRepository.findAll(pageable)).thenReturn(pagina);

		when(livroMapper.toResponseDto(livro1)).thenReturn(dto1);
		when(livroMapper.toResponseDto(livro2)).thenReturn(dto2);

		Page<LivroResponseDto> resultado = livroService.findAll(pageable);

		assertNotNull(resultado);
		assertEquals(2, resultado.getContent().size());

		assertEquals("Clean Code",
				resultado.getContent().get(0).titulo());

		assertEquals("Effective Java",
				resultado.getContent().get(1).titulo());

		verify(livroRepository).findAll(pageable);
	}

	@Test
	void deveRetornarListaDeLivrosQuandoBuscarPorTitulo() {

		LivroEntity entity = criarLivroEntity();
		LivroResponseDto response = criarLivroResponseDto();

		when(livroRepository.findByTitulo("Clean Code"))
				.thenReturn(List.of(entity));

		when(livroMapper.toResponseDto(entity))
				.thenReturn(response);

		List<LivroResponseDto> resultado =
				livroService.findByTitulo("Clean Code");

		assertNotNull(resultado);
		assertEquals(1, resultado.size());
		assertEquals("Clean Code", resultado.get(0).titulo());

		verify(livroRepository).findByTitulo("Clean Code");
	}

	@Test
	void deveRetornarLivroQuandoIsbnExistir() {

		LivroEntity entity = criarLivroEntity();
		LivroResponseDto response = criarLivroResponseDto();

		when(livroRepository.findByIsbn(entity.getIsbn()))
				.thenReturn(Optional.of(entity));

		when(livroMapper.toResponseDto(entity))
				.thenReturn(response);

		LivroResponseDto resultado =
				livroService.findByIsbn(entity.getIsbn());

		assertNotNull(resultado);

		assertEquals(response.id(), resultado.id());
		assertEquals(response.titulo(), resultado.titulo());
		assertEquals(response.isbn(), resultado.isbn());

		verify(livroRepository).findByIsbn(entity.getIsbn());
	}
	@Test
	void deveLancarLivroNaoEncontradoExceptionQuandoIsbnNaoExistir() {

		when(livroRepository.findByIsbn("123"))
				.thenReturn(Optional.empty());

		LivroNaoEncontradoException exception = assertThrows(
				LivroNaoEncontradoException.class,
				() -> livroService.findByIsbn("123")
		);

		assertTrue(exception.getMessage().contains("123"));

		verify(livroRepository).findByIsbn("123");
	}

	@Test
	void deveAtualizarLivroComSucesso() {

		Long id = 1L;

		LivroRequestDto request = criarLivroRequestDto();
		LivroEntity entity = criarLivroEntity();
		LivroResponseDto response = criarLivroResponseDto();

		when(livroRepository.findById(id))
				.thenReturn(Optional.of(entity));

		when(livroRepository.save(entity))
				.thenReturn(entity);

		when(livroMapper.toResponseDto(entity))
				.thenReturn(response);

		LivroResponseDto resultado = livroService.update(id, request);

		assertNotNull(resultado);
		assertEquals(response.id(), resultado.id());
		assertEquals(response.titulo(), resultado.titulo());

		verify(livroMapper).updateEntityFromDto(request, entity);
		verify(livroRepository).save(entity);
	}

	@Test
	void deveLancarLivroNaoEncontradoExceptionAoAtualizar() {

		Long id = 100L;

		when(livroRepository.findById(id))
				.thenReturn(Optional.empty());

		LivroNaoEncontradoException exception = assertThrows(
				LivroNaoEncontradoException.class,
				() -> livroService.update(id, criarLivroRequestDto())
		);

		assertTrue(exception.getMessage().contains(id.toString()));

		verify(livroRepository, never()).save(any());
	}

	@Test
	void deveExcluirLivroComSucesso() {

		Long id = 1L;

		LivroEntity entity = criarLivroEntity();

		when(livroRepository.findById(id))
				.thenReturn(Optional.of(entity));

		assertDoesNotThrow(() -> livroService.deleteById(id));

		verify(livroRepository).delete(entity);
	}

	@Test
	void deveLancarLivroNaoEncontradoExceptionAoExcluir() {

		Long id = 99L;

		when(livroRepository.findById(id))
				.thenReturn(Optional.empty());

		LivroNaoEncontradoException exception = assertThrows(
				LivroNaoEncontradoException.class,
				() -> livroService.deleteById(id)
		);

		assertTrue(exception.getMessage().contains(id.toString()));

		verify(livroRepository, never()).delete(any());
	}

	// ===========================
	// Métodos auxiliares
	// ===========================

	private LivroRequestDto criarLivroRequestDto() {
		return new LivroRequestDto(
				"Clean Code",
				"Robert C. Martin",
				"9780132350884",
				2008,
				10
		);
	}

	private LivroEntity criarLivroEntity() {
		LivroEntity livro = new LivroEntity();
		livro.setId(1L);
		livro.setTitulo("Clean Code");
		livro.setAutor("Robert C. Martin");
		livro.setIsbn("9780132350884");
		livro.setAnoPublicacao(2008);
		livro.setQuantidade(10);
		return livro;
	}

	private LivroResponseDto criarLivroResponseDto() {
		return new LivroResponseDto(
				1L,
				"Clean Code",
				"Robert C. Martin",
				"9780132350884",
				2008,
				10
		);
	}

	private LivroEntity criarSegundoLivroEntity() {
		LivroEntity livro = new LivroEntity();
		livro.setId(2L);
		livro.setTitulo("Effective Java");
		livro.setAutor("Joshua Bloch");
		livro.setIsbn("9780134685991");
		livro.setAnoPublicacao(2018);
		livro.setQuantidade(5);
		return livro;
	}

	private LivroResponseDto criarSegundoLivroResponseDto() {
		return new LivroResponseDto(
				2L,
				"Effective Java",
				"Joshua Bloch",
				"9780134685991",
				2018,
				5
		);
	}
}