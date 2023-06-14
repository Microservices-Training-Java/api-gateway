package com.example;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandle {
  @ExceptionHandler(BadRequestException.class)
  public ResponseEntity<?> resolveException(BadRequestException e){
    return new ResponseEntity<>(getBody(HttpStatus.BAD_REQUEST,e, e.getMessage()),new HttpHeaders(),HttpStatus.BAD_REQUEST);
  }
  public Map<String, Object> getBody(HttpStatus httpStatus, Exception ex, String message){
    Map<String, Object> body = new LinkedHashMap<>();
    body.put("message", message);
    body.put("timestamp", new Date());
    body.put("status",httpStatus.value());
    body.put("error", httpStatus.getReasonPhrase());
    body.put("exception", ex.toString());
    Throwable cause = ex.getCause();
    if(cause != null){
      body.put("exceptionCause", ex.getCause().toString());
    }
    return body;
  }
}
