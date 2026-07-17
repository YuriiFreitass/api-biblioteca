package com.yuri.api_biblioteca.exception;

import com.yuri.api_biblioteca.dto.CampoErroDto;
import com.yuri.api_biblioteca.dto.ErrorResponseDto;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;


@RestControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponseDto> handleValidationException(MethodArgumentNotValidException exception
	) {
		List<CampoErroDto> campos = exception
				.getBindingResult()
				.getFieldErrors()
				.stream()
				.map(fieldError -> new CampoErroDto(
						fieldError.getField(),
						fieldError.getDefaultMessage()
				))
				.toList();

		ErrorResponseDto erro = new ErrorResponseDto(
				HttpStatus.BAD_REQUEST.value(), "Erro de validação",
				campos
		);
		return ResponseEntity.badRequest().body(erro);
	};

	@ExceptionHandler(LivroNaoEncontradoException.class)
	public ResponseEntity<ErrorResponseDto> handleLivroNaoEncontradoException(
			LivroNaoEncontradoException exception) {

		ErrorResponseDto erro = new ErrorResponseDto(
				HttpStatus.NOT_FOUND.value(),
				exception.getMessage(),
				List.of()
		);
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erro);
	}

	@ExceptionHandler(IsbnDuplicadoException.class)
	public ResponseEntity<ErrorResponseDto> handleIsbnDuplicadoException(
			IsbnDuplicadoException exception) {

		ErrorResponseDto erro = new ErrorResponseDto(
				HttpStatus.CONFLICT.value(),
				exception.getMessage(),
				List.of()
		);

		return ResponseEntity
				.status(HttpStatus.CONFLICT)
				.body(erro);
	}

	@ExceptionHandler(LivroIndisponivelException.class)
	public ResponseEntity<ErrorResponseDto> handleLivroIndisponivelException(
			LivroIndisponivelException exception) {
				ErrorResponseDto erro = new ErrorResponseDto(
						HttpStatus.CONFLICT.value(),
						exception.getMessage(),
						List.of()
				);
				return ResponseEntity
						.status(HttpStatus.CONFLICT)
						.body(erro);
	}

	@ExceptionHandler(EmprestimoNaoEncontradoException.class)
	public ResponseEntity<ErrorResponseDto> handleEmprestimoNaoEncontradoException(
			EmprestimoNaoEncontradoException exception) {
		ErrorResponseDto erro = new ErrorResponseDto(
				HttpStatus.NOT_FOUND.value(),
				exception.getMessage(),
				List.of()
		);
		return ResponseEntity
				.status(HttpStatus.NOT_FOUND)
				.body(erro);
	}

	@ExceptionHandler(EmprestimoJaDevolvidoException.class)
	public ResponseEntity<ErrorResponseDto> handleEmprestimoJaDevolvidoException(
			EmprestimoJaDevolvidoException exception) {
		ErrorResponseDto erro = new ErrorResponseDto(
				HttpStatus.CONFLICT.value(),
				exception.getMessage(),
				List.of()
		);
		return ResponseEntity
				.status(HttpStatus.CONFLICT)
				.body(erro);
	}

}
