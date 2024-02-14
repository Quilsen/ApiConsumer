package com.apiconsumer.apiconsumer.infractructure;

import com.apiconsumer.apiconsumer.github.response.CustomResponse;
import feign.FeignException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GithubApiClientExceptionHandler {
    @ExceptionHandler(FeignException.class)
    public ResponseEntity<CustomResponse> handleFeignStatusException(FeignException e){
        return new ResponseEntity<>(new CustomResponse(e.status(), e.getMessage()),HttpStatus.resolve(e.status()));
    }
}
