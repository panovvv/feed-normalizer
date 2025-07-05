package com.sportygroup.feednormalizer.adapters.inbound;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Global exception handler - any unhandled exceptions land here. Per-controller overrides can be
 * defined using similar @ExceptionHandler methods in controller classes. It may be useful in cases
 * where we want custom response code per provider, e.g. some might expect 400 for malformed message
 * while others 422.
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
  public static final ApiError ERROR_BODY_400 =
      new ApiError("Malformed Request (syntax error). Please check the API specification");
  public static final ApiError ERROR_BODY_422_GENERIC =
      new ApiError("Malformed Request (logical error). Please check the API specification");
  public static final ApiError ERROR_BODY_500 =
      new ApiError("An unexpected error occurred, we will look into it");

  @ExceptionHandler({
    HttpMessageNotReadableException.class,
    JsonProcessingException.class,
    HttpMediaTypeNotSupportedException.class,
    HttpRequestMethodNotSupportedException.class
  })
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ApiError handle400BadRequest(Exception ex) {
    return ERROR_BODY_400;
  }

  @ExceptionHandler({MessageUnprocessableException.class})
  @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
  public ApiError handle422UnprocessableEntity(Exception ex) {
    if (ex instanceof MessageUnprocessableException) {
      return new ApiError(ex.getMessage());
    }
    return ERROR_BODY_422_GENERIC;
  }

  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ApiError handleAllOtherErrors(Exception ex) {
    log.error("Unhandled exception caught", ex);
    return ERROR_BODY_500;
  }

  public record ApiError(String detail) {}
}
