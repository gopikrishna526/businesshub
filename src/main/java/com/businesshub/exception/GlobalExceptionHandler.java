package com.businesshub.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

//	@ExceptionHandler(BusinessNotFoundException.class)
//    @ResponseStatus(HttpStatus.NOT_FOUND)
//    public String handleBusinessNotFound(BusinessNotFoundException ex) {
//        return ex.getMessage();
//    }

	@ExceptionHandler(BusinessNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ErrorResponse handleBusinessNotFound(BusinessNotFoundException ex) {

		return new ErrorResponse(LocalDateTime.now(), HttpStatus.NOT_FOUND.value(), "Business Not Found",
				ex.getMessage());
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ValidationErrorResponse handleValidationExceptions(MethodArgumentNotValidException ex) {

		Map<String, String> errors = new HashMap<>();

		ex.getBindingResult().getFieldErrors().forEach(error -> {

			String field = error.getField();

			if (!errors.containsKey(field)) {
				errors.put(field, error.getDefaultMessage());
			}
		});

		return new ValidationErrorResponse(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(), "Validation Failed",
				errors);
	}

	@ExceptionHandler(CustomerNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ErrorResponse handleCustomerNotFound(CustomerNotFoundException ex) {

		return new ErrorResponse(LocalDateTime.now(), HttpStatus.NOT_FOUND.value(), "Customer Not Found",
				ex.getMessage());
	}

	@ExceptionHandler(UserAlreadyExistsException.class)
	public ResponseEntity<ErrorResponse> handleUserAlreadyExists(UserAlreadyExistsException ex) {

		ErrorResponse errorResponse = new ErrorResponse(LocalDateTime.now(), HttpStatus.CONFLICT.value(),
				ex.getMessage());

		return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
	}

	@ExceptionHandler(InvalidCredentialsException.class)
	public ResponseEntity<ErrorResponse> handleInvalidCredentials(InvalidCredentialsException ex) {

		ErrorResponse errorResponse = new ErrorResponse(LocalDateTime.now(), HttpStatus.UNAUTHORIZED.value(),
				ex.getMessage());

		return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
	}
}