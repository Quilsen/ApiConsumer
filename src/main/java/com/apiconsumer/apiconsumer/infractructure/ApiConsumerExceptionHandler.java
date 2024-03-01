package com.apiconsumer.apiconsumer.infractructure;

import com.apiconsumer.apiconsumer.github.response.CustomResponse;
import feign.FeignException;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver;

@Log4j2
@RestControllerAdvice
public class ApiConsumerExceptionHandler{
    private static final String MSG_NOT_FOUND = "User not found";
    private static final String MSG_FORBIDDEN = "Access forbiden";
    private static final String MSG_NOT_ACCEPTABLE = "Provided format not acceptable";
    @ExceptionHandler(FeignException.NotFound.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<CustomResponse> handleFeignStatusException(FeignException.NotFound e){
        log.warn(e.getMessage());
        return new ResponseEntity<>(new CustomResponse(e.status(), MSG_NOT_FOUND), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(FeignException.Forbidden.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<CustomResponse> handleFeignStatusException(FeignException.Forbidden e){
        log.warn(e.getMessage());
        return new ResponseEntity<>(new CustomResponse(e.status(), MSG_FORBIDDEN), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(HttpClientErrorException.NotFound.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<CustomResponse> handleRestClientStatusException(HttpClientErrorException.NotFound e){
        log.warn(e.getMessage());
        return new ResponseEntity<>(new CustomResponse(e.getStatusCode().value(), MSG_NOT_FOUND), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(HttpClientErrorException.Forbidden.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<CustomResponse> handleRestClientStatusException(HttpClientErrorException.Forbidden e){
        log.warn(e.getMessage());
        return new ResponseEntity<>(new CustomResponse(e.getStatusCode().value(), MSG_FORBIDDEN), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public ResponseEntity<String> handleHttpMediaNotAcceptableException(HttpMediaTypeNotAcceptableException e){
        log.warn(e.getMessage());
        CustomResponse customResponse = new CustomResponse(e.getStatusCode().value(), MSG_NOT_ACCEPTABLE);
        return new ResponseEntity<>(customResponse.toString(), HttpStatus.NOT_ACCEPTABLE);
    }

}
