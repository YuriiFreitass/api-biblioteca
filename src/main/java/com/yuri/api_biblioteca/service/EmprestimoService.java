package com.yuri.api_biblioteca.service;

import com.yuri.api_biblioteca.dto.EmprestimoReponseDto;
import com.yuri.api_biblioteca.dto.EmprestimoRequestDto;
import com.yuri.api_biblioteca.entity.EmprestimoEntity;
import com.yuri.api_biblioteca.entity.LivroEntity;
import com.yuri.api_biblioteca.enums.StatusEmprestimo;
import com.yuri.api_biblioteca.exception.LivroNaoEncontradoException;
import com.yuri.api_biblioteca.mapper.EmprestimoMapper;
import com.yuri.api_biblioteca.repository.EmprestimoRepository;
import com.yuri.api_biblioteca.repository.LivroRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class EmprestimoService {

	private final EmprestimoRepository emprestimoRepository;
	private final LivroRepository livroRepository;
	private final EmprestimoMapper emprestimoMapper;

	@Transactional
	public EmprestimoReponseDto realizarEmprestimo(
			EmprestimoRequestDto emprestimoRequestDto
	) {
		LivroEntity livro = livroRepository
				.findById(emprestimoRequestDto.livroId())
				.orElseThrow(() -> new LivroNaoEncontradoException("Livro não encontrado"));

		if (livro.getQuantidade() <= 0) {
			throw  new LivroNaoEncontradoException("Livro indisponível");
		}

		EmprestimoEntity emprestimo = emprestimoMapper.toEntity(emprestimoRequestDto);

		emprestimo.setLivro(livro);
		emprestimo.setDataEmprestimo(LocalDate.now());
		emprestimo.setDataPrevistaDevolucao(
				emprestimoRequestDto.dataPrevistaDevolucao()
		);
		emprestimo.setStatus(StatusEmprestimo.ATIVO);

		livro.setQuantidade(livro.getQuantidade() - 1);

		EmprestimoEntity emprestimoSalvo = emprestimoRepository.save(emprestimo);

		return emprestimoMapper.toResponseDto(emprestimoSalvo);
	}

	@Transactional
	public EmprestimoReponseDto devolverLivro(Long emprestimoId) {

		EmprestimoEntity emprestimo = emprestimoRepository
				.findById(emprestimoId)
				.orElseThrow(() -> new RuntimeException("Empréstimo não encontrado"));

		if (emprestimo.getStatus() == StatusEmprestimo.DEVOLVIDO) {
			throw new RuntimeException("Este empréstimo já foi devolvido");
		}

		emprestimo.setDataDevolucao(LocalDate.now());
		emprestimo.setStatus(StatusEmprestimo.DEVOLVIDO);

		LivroEntity livro = emprestimo.getLivro();
		livro.setQuantidade(livro.getQuantidade() + 1);

		EmprestimoEntity emprestimoSalvo =
				emprestimoRepository.save(emprestimo);

		return emprestimoMapper.toResponseDto(emprestimoSalvo);
	}

}
