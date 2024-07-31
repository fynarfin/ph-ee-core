package org.mifos.connector.common.exception.mapper;

import org.mifos.connector.common.channel.dto.PhErrorDTO;
import org.mifos.connector.common.exception.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

public class GlobalExceptionMapper {
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<PhErrorDTO> handleValidationException(ValidationException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getPhErrorDTO());
    }
}
