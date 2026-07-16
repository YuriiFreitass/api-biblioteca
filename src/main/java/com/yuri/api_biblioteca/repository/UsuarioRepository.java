package com.yuri.api_biblioteca.repository;

import com.yuri.api_biblioteca.entity.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Long> {

	Optional<UsuarioEntity> findByUsername(String username);
}
