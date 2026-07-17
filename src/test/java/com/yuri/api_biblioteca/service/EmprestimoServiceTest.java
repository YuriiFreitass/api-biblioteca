package com.yuri.api_biblioteca.service;

import com.yuri.api_biblioteca.dto.EmprestimoReponseDto;
import com.yuri.api_biblioteca.dto.EmprestimoRequestDto;
import com.yuri.api_biblioteca.entity.EmprestimoEntity;
import com.yuri.api_biblioteca.entity.LivroEntity;
import com.yuri.api_biblioteca.enums.StatusEmprestimo;
import com.yuri.api_biblioteca.exception.EmprestimoJaDevolvidoException;
import com.yuri.api_biblioteca.exception.EmprestimoNaoEncontradoException;
import com.yuri.api_biblioteca.exception.LivroIndisponivelException;
import com.yuri.api_biblioteca.exception.LivroNaoEncontradoException;
import com.yuri.api_biblioteca.mapper.EmprestimoMapper;
import com.yuri.api_biblioteca.repository.EmprestimoRepository;
import com.yuri.api_biblioteca.repository.LivroRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmprestimoServiceTest {

	@Mock
	private EmprestimoRepository emprestimoRepository;

	@Mock
	private LivroRepository livroRepository;

	@Mock
	private EmprestimoMapper emprestimoMapper;

	@InjectMocks
	private EmprestimoService emprestimoService;

	@Test
	void deveRealizarEmprestimoComSucesso() {

		Long livroId = 1L;

		EmprestimoRequestDto requestDto = new EmprestimoRequestDto(
				livroId,
				"Yuri",
				LocalDate.now().plusDays(7)
		);

		LivroEntity livro = criarLivro(livroId, 3);

		EmprestimoEntity emprestimo = new EmprestimoEntity();
		emprestimo.setNomeCliente("Yuri");

		EmprestimoEntity emprestimoSalvo =
				criarEmprestimoAtivo(1L, livro);

		EmprestimoReponseDto responseDto =
				criarResponseAtivo(1L, livroId);

		when(livroRepository.findById(livroId))
				.thenReturn(Optional.of(livro));

		when(emprestimoMapper.toEntity(requestDto))
				.thenReturn(emprestimo);

		when(emprestimoRepository.save(emprestimo))
				.thenReturn(emprestimoSalvo);

		when(emprestimoMapper.toResponseDto(emprestimoSalvo))
				.thenReturn(responseDto);

		EmprestimoReponseDto resultado =
				emprestimoService.realizarEmprestimo(requestDto);

		assertNotNull(resultado);
		assertEquals(1L, resultado.id());
		assertEquals("Yuri", resultado.nomeCliente());
		assertEquals(StatusEmprestimo.ATIVO, resultado.status());
		assertEquals(livroId, resultado.livroId());

		assertEquals(2, livro.getQuantidade());
		assertEquals(livro, emprestimo.getLivro());
		assertEquals(StatusEmprestimo.ATIVO, emprestimo.getStatus());
		assertNotNull(emprestimo.getDataEmprestimo());

		assertEquals(
				requestDto.dataPrevistaDevolucao(),
				emprestimo.getDataPrevistaDevolucao()
		);

		verify(livroRepository).findById(livroId);
		verify(emprestimoMapper).toEntity(requestDto);
		verify(emprestimoRepository).save(emprestimo);
		verify(emprestimoMapper).toResponseDto(emprestimoSalvo);
	}

	@Test
	void deveLancarExcecaoQuandoLivroNaoExistir() {

		Long livroId = 1L;

		EmprestimoRequestDto requestDto =
				new EmprestimoRequestDto(
						livroId,
						"Yuri",
						LocalDate.now().plusDays(7)
				);

		when(livroRepository.findById(livroId))
				.thenReturn(Optional.empty());

		LivroNaoEncontradoException exception =
				assertThrows(
						LivroNaoEncontradoException.class,
						() -> emprestimoService.realizarEmprestimo(requestDto)
				);

		assertEquals(
				"Livro não encontrado",
				exception.getMessage()
		);

		verify(livroRepository).findById(livroId);
		verifyNoInteractions(emprestimoMapper);
		verifyNoInteractions(emprestimoRepository);
	}

	@Test
	void deveLancarExcecaoQuandoLivroEstiverSemEstoque() {

		Long livroId = 1L;

		EmprestimoRequestDto requestDto =
				new EmprestimoRequestDto(
						livroId,
						"Yuri",
						LocalDate.now().plusDays(7)
				);

		LivroEntity livro = criarLivro(livroId, 0);

		when(livroRepository.findById(livroId))
				.thenReturn(Optional.of(livro));

		LivroIndisponivelException exception =
				assertThrows(
						LivroIndisponivelException.class,
						() -> emprestimoService.realizarEmprestimo(requestDto)
				);

		assertEquals(
				"Livro indisponível",
				exception.getMessage()
		);

		verify(livroRepository).findById(livroId);
		verifyNoInteractions(emprestimoMapper);
		verifyNoInteractions(emprestimoRepository);
	}    @Test
	void deveDevolverLivroComSucesso() {

		Long emprestimoId = 1L;
		Long livroId = 1L;

		LivroEntity livro = criarLivro(livroId, 2);

		EmprestimoEntity emprestimo =
				criarEmprestimoAtivo(emprestimoId, livro);

		EmprestimoEntity emprestimoSalvo = new EmprestimoEntity();
		emprestimoSalvo.setId(emprestimoId);
		emprestimoSalvo.setNomeCliente("Yuri");
		emprestimoSalvo.setLivro(livro);
		emprestimoSalvo.setDataEmprestimo(
				emprestimo.getDataEmprestimo()
		);
		emprestimoSalvo.setDataPrevistaDevolucao(
				emprestimo.getDataPrevistaDevolucao()
		);
		emprestimoSalvo.setDataDevolucao(LocalDate.now());
		emprestimoSalvo.setStatus(StatusEmprestimo.DEVOLVIDO);

		EmprestimoReponseDto responseDto =
				new EmprestimoReponseDto(
						emprestimoId,
						"Yuri",
						emprestimoSalvo.getDataEmprestimo(),
						emprestimoSalvo.getDataPrevistaDevolucao(),
						emprestimoSalvo.getDataDevolucao(),
						StatusEmprestimo.DEVOLVIDO,
						livroId,
						"O Pequeno Príncipe"
				);

		when(emprestimoRepository.findById(emprestimoId))
				.thenReturn(Optional.of(emprestimo));

		when(emprestimoRepository.save(emprestimo))
				.thenReturn(emprestimoSalvo);

		when(emprestimoMapper.toResponseDto(emprestimoSalvo))
				.thenReturn(responseDto);

		EmprestimoReponseDto resultado =
				emprestimoService.devolverLivro(emprestimoId);

		assertNotNull(resultado);
		assertEquals(emprestimoId, resultado.id());
		assertEquals(
				StatusEmprestimo.DEVOLVIDO,
				resultado.status()
		);
		assertNotNull(resultado.dataDevolucao());

		assertEquals(
				StatusEmprestimo.DEVOLVIDO,
				emprestimo.getStatus()
		);
		assertNotNull(emprestimo.getDataDevolucao());
		assertEquals(3, livro.getQuantidade());

		verify(emprestimoRepository).findById(emprestimoId);
		verify(emprestimoRepository).save(emprestimo);
		verify(emprestimoMapper).toResponseDto(emprestimoSalvo);
	}

	@Test
	void deveLancarExcecaoQuandoEmprestimoNaoExistir() {

		Long emprestimoId = 1L;

		when(emprestimoRepository.findById(emprestimoId))
				.thenReturn(Optional.empty());

		EmprestimoNaoEncontradoException exception =
				assertThrows(
						EmprestimoNaoEncontradoException.class,
						() -> emprestimoService.devolverLivro(emprestimoId)
				);

		assertEquals(
				"Empréstimo não encontrado",
				exception.getMessage()
		);

		verify(emprestimoRepository).findById(emprestimoId);
		verify(
				emprestimoRepository,
				never()
		).save(any(EmprestimoEntity.class));

		verifyNoInteractions(emprestimoMapper);
	}

	@Test
	void deveLancarExcecaoQuandoEmprestimoJaEstiverDevolvido() {

		Long emprestimoId = 1L;

		LivroEntity livro = criarLivro(1L, 3);

		EmprestimoEntity emprestimo = new EmprestimoEntity();
		emprestimo.setId(emprestimoId);
		emprestimo.setNomeCliente("Yuri");
		emprestimo.setLivro(livro);
		emprestimo.setStatus(StatusEmprestimo.DEVOLVIDO);
		emprestimo.setDataDevolucao(LocalDate.now());

		when(emprestimoRepository.findById(emprestimoId))
				.thenReturn(Optional.of(emprestimo));

		EmprestimoJaDevolvidoException exception =
				assertThrows(
						EmprestimoJaDevolvidoException.class,
						() -> emprestimoService.devolverLivro(emprestimoId)
				);

		assertEquals(
				"Este empréstimo já foi devolvido",
				exception.getMessage()
		);

		assertEquals(3, livro.getQuantidade());

		verify(emprestimoRepository).findById(emprestimoId);
		verify(
				emprestimoRepository,
				never()
		).save(any(EmprestimoEntity.class));

		verifyNoInteractions(emprestimoMapper);
	}

	private LivroEntity criarLivro(
			Long id,
			Integer quantidade
	) {
		LivroEntity livro = new LivroEntity();
		livro.setId(id);
		livro.setTitulo("O Pequeno Príncipe");
		livro.setQuantidade(quantidade);

		return livro;
	}

	private EmprestimoEntity criarEmprestimoAtivo(
			Long id,
			LivroEntity livro
	) {
		EmprestimoEntity emprestimo = new EmprestimoEntity();
		emprestimo.setId(id);
		emprestimo.setNomeCliente("Yuri");
		emprestimo.setLivro(livro);
		emprestimo.setDataEmprestimo(LocalDate.now());
		emprestimo.setDataPrevistaDevolucao(
				LocalDate.now().plusDays(7)
		);
		emprestimo.setStatus(StatusEmprestimo.ATIVO);

		return emprestimo;
	}

	private EmprestimoReponseDto criarResponseAtivo(
			Long emprestimoId,
			Long livroId
	) {
		return new EmprestimoReponseDto(
				emprestimoId,
				"Yuri",
				LocalDate.now(),
				LocalDate.now().plusDays(7),
				null,
				StatusEmprestimo.ATIVO,
				livroId,
				"O Pequeno Príncipe"
		);
	}
}

