package asd.tsanyi.security.rest.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import asd.tsanyi.security.dto.InfoResponse;
import asd.tsanyi.security.enums.ResponseTypes;

@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {
	protected Logger log = LoggerFactory.getLogger(this.getClass());
	
    @ExceptionHandler(Exception.class)
    public final ResponseEntity<InfoResponse> handleAllExceptions(Exception ex, WebRequest request) {
    	log.error(ex.getMessage());
        return InfoResponse.createResponseEntity(ResponseTypes.ERROR, "Something bad happened© Admin! RTFL!", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
