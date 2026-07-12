package com.yuri.api_biblioteca.repository;

import com.yuri.api_biblioteca.dto.LivroResponseDto;
import com.yuri.api_biblioteca.entity.LivroEntity;
import jakarta.persistence.Id;
import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LivroRepository extends JpaRepository<LivroEntity, Long> {

	List<LivroEntity> findByTitulo(String titulo);

	boolean existsByIsbn(String isbn);

	Optional<LivroEntity> findByIsbn(String isbn);
}
