package com.yuri.api_biblioteca.mapper;

import com.yuri.api_biblioteca.dto.LivroRequestDto;
import com.yuri.api_biblioteca.dto.LivroResponseDto;
import com.yuri.api_biblioteca.entity.LivroEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;


@Mapper(componentModel = "spring")
public interface LivroMapper {
	LivroEntity toEntity(LivroRequestDto livroRequestDto);

	LivroResponseDto toResponseDto(LivroEntity livroEntity);

	void updateEntityFromDto(
			LivroRequestDto livroRequestDto,
			@MappingTarget LivroEntity livroEntity
	);
}




