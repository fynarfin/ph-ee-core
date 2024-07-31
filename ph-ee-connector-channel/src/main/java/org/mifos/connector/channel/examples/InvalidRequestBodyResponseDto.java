package org.mifos.connector.channel.examples;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "Example response for invalid request body")
public class InvalidRequestBodyResponseDto {

    @Schema(description = "Error category", example = "Validation")
    public String errorCategory;

    @Schema(description = "Error code", example = "error.msg.schema.validation.errors")
    public String errorCode;

    @Schema(description = "Error description", example = "The request is invalid")
    public String errorDescription;

    @Schema(description = "Developer message", example = "The request is invalid")
    public String developerMessage;

    @Schema(description = "User message", example = "The request is invalid")
    public String defaultUserMessage;

    @Schema(description = "Error parameters", example = "null")
    public Object errorParameters;

    @Schema(description = "List of errors")
    public List<ErrorDetailsDto> errors;
}
