package com.yuri.api_biblioteca.mapper;

import com.yuri.api_biblioteca.dto.EmprestimoReponseDto;
import com.yuri.api_biblioteca.dto.EmprestimoRequestDto;
import com.yuri.api_biblioteca.entity.EmprestimoEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface EmprestimoMapper {
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "dataEmprestimo", ignore = true)
	@Mapping(target = "dataDevolucao", ignore = true)
	@Mapping(target = "dataPrevistaDevolucao", ignore = true)
	@Mapping(target = "status", ignore = true)
	@Mapping(target = "livro", ignore = true)
	EmprestimoEntity toEntity(EmprestimoRequestDto emprestimoRequestDto);

	@Mapping(target = "livroId", source = "livro.id")
	@Mapping(target = "tituloLivro", source = "livro.titulo")
	EmprestimoReponseDto toResponseDto(EmprestimoEntity entity);

}
