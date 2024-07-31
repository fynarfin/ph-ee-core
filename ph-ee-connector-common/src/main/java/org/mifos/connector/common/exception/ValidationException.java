package org.mifos.connector.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.mifos.connector.common.channel.dto.PhErrorDTO;

@Getter
@RequiredArgsConstructor
public class ValidationException extends RuntimeException {

    private final PhErrorDTO phErrorDTO;

}
