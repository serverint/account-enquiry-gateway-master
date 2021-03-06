package com.wasp.gateway.enquiry.account.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.paymentcomponents.common.log.RequestLogger
import com.wasp.gateway.enquiry.account.exceptions.WaspApiValidationException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.client.HttpClientErrorException

import javax.servlet.http.HttpServletRequest

/**
 * Created by aodunlami on 22/05/2017.
 */
@ControllerAdvice
class ExceptionController {
    RequestLogger logger = new RequestLogger(this.class.name)

    @ExceptionHandler(HttpClientErrorException.class)
    public def httpErrorHandler(HttpServletRequest req, HttpClientErrorException e) {
        logger.error("Failed Request", req, null, null, e)
        ObjectMapper objectMapper = new ObjectMapper()
        java.lang.Error error
        try {
            error = objectMapper.readValue(e.getResponseBodyAsString(), java.lang.Error.class)
        } catch (Exception ex) {
            error = new java.lang.Error("internal_error", e.getMessage())
        }
        return new ResponseEntity<String>(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(error), e.statusCode);
    }


    @ExceptionHandler(WaspApiValidationException.class)
    public def waspValidationErrorHandler(HttpServletRequest req, WaspApiValidationException e) {
        logger.error("Failed Request", req, null, null, e)
        ObjectMapper objectMapper = new ObjectMapper()
        return new ResponseEntity<String>(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(new java.lang.Error(e.errorCode, e.errorDescription)), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public def genericErrorHandler(HttpServletRequest req, Exception e) {
        logger.error("Failed Request", req, null, null, e)
        java.lang.Error error = new java.lang.Error("internal_error", e.getMessage())
        return new ResponseEntity<String>(new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(error), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}