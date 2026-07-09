package com.foodorder.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.foodorder.dto.ResponseStructure;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler{
	
	@ExceptionHandler(IdNotFoundException.class)
	public ResponseEntity<ResponseStructure<String>> handleIdNotFoundException(IdNotFoundException exp){
		
		ResponseStructure<String> response = new ResponseStructure<String>();
		response.setStatusCode(HttpStatus.NOT_FOUND.value());
		response.setMessage(exp.getMessage());
		response.setData("FAILURE");
		
		return new ResponseEntity<ResponseStructure<String>>(response, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ResponseStructure<String>> handleResourceNotFoundException(ResourceNotFoundException exp){
		
		ResponseStructure<String> response = new ResponseStructure<String>();
		response.setStatusCode(HttpStatus.NOT_FOUND.value());
		response.setMessage(exp.getMessage());
		response.setData("FAILURE");
		
		return new ResponseEntity<ResponseStructure<String>>(response, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(ResourceAlreadyExistsException.class)
	public ResponseEntity<ResponseStructure<String>> handleInvalidCredentialsException(ResourceAlreadyExistsException exp){
		
		ResponseStructure<String> response = new ResponseStructure<String>();
		response.setStatusCode(HttpStatus.CONFLICT.value());
		response.setMessage(exp.getMessage());
		response.setData("FAILURE");
		
		return new ResponseEntity<ResponseStructure<String>>(response, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(ValidationException.class)
	public ResponseEntity<ResponseStructure<String>> handleInvalidCredentialsException(ValidationException exp){
		
		ResponseStructure<String> response = new ResponseStructure<String>();
		response.setStatusCode(HttpStatus.BAD_REQUEST.value());
		response.setMessage(exp.getMessage());
		response.setData("FAILURE");
		
		return new ResponseEntity<ResponseStructure<String>>(response, HttpStatus.BAD_REQUEST);
	}

}
