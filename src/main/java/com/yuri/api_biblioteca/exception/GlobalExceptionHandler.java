package com.yuri.api_biblioteca.exception;

import com.yuri.api_biblioteca.dto.ErrorResponseDto;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
		@ExceptionHandler(MethodArgumentNotValidException.class)
		public ResponseEntity<ErrorResponseDto> handleValidationException(
				MethodArgumentNotValidException exception) {
			String mensagem = exception.getBindingResult()
					.getFieldError()
					.getDefaultMessage();

			ErrorResponseDto erro = new ErrorResponseDto(
					HttpStatus.BAD_REQUEST.value(),
					mensagem
			);
			return ResponseEntity.badRequest().body(erro);
		}

		@ExceptionHandler(IsbnDuplicadoException.class)
		public ResponseEntity<ErrorResponseDto> handleIsbnDuplicadoException(
				IsbnDuplicadoException exception) {
			String mensagem = exception.getMessage();

			ErrorResponseDto erro = new ErrorResponseDto(
					HttpStatus.CONFLICT.value(), mensagem
			);

			return ResponseEntity.status(HttpStatus.CONFLICT).body(erro);
		}




}
