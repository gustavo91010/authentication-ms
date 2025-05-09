package com.ajudaqui.authenticationms.exception;

import java.util.HashMap;
import java.util.Map;

import com.ajudaqui.authenticationms.response.error.ResponseError;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

@RestControllerAdvice
public class HandlerException {

  // Para tratar ausencia de parametros em objetos validado na chamada de enpoints
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ResponseError> handleValidationExceptions(MethodArgumentNotValidException ex) {
    String errorMessage = ex.getBindingResult()
        .getFieldErrors()
        .stream()
        .map(DefaultMessageSourceResolvable::getDefaultMessage)
        .findFirst()
        .orElse("Erro de validação");

    return ResponseEntity.badRequest().body(new ResponseError(errorMessage));
  }

  // Para tratar ausencia de parametros na chamada de endpoints
  @ExceptionHandler(MissingServletRequestParameterException.class)
  public ResponseEntity<Object> handleMissingRequestParam(MissingServletRequestParameterException ex) {
    String name = ex.getParameterName();
    return ResponseEntity
        .badRequest()
        .body(new ResponseError("O parâmetro obrigatório '" + name + "' está ausente."));
  }

  // Para tratar ausencia de valor no path variable
  @ExceptionHandler(MissingPathVariableException.class)
  public ResponseEntity<Map<String, String>> handleMissingPathVariable(MissingPathVariableException ex) {
    String name = ex.getVariableName();

    Map<String, String> error = new HashMap<>();
    error.put("error", "Variável de caminho ausente");
    error.put("param", name);

    return ResponseEntity.badRequest().body(error);
  }

  // Trata rota não cadastrada... lembrar de atualizar o properties
  @ExceptionHandler(NoHandlerFoundException.class)
  public ResponseEntity<Map<String, String>> handleNotFound(NoHandlerFoundException ex) {
    Map<String, String> error = new HashMap<>();
    error.put("error", "Rota não encontrada");
    error.put("path", ex.getRequestURL());
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
  }

}
