package com.yuri.api_biblioteca.entity;

import com.yuri.api_biblioteca.enums.StatusEmprestimo;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "emprestimos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmprestimoEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String nomeCliente;

	@Column(nullable = false)
	private LocalDate dataEmprestimo;

	private LocalDate dataDevolucao;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private StatusEmprestimo status;

	@ManyToOne
	@JoinColumn(name = "livro_id", nullable = false)
	private LivroEntity livro;

	@Column(nullable = false)
	private LocalDate dataPrevistaDevolucao;
}
