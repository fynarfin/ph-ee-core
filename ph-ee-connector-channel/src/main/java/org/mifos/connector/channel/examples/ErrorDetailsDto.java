package org.mifos.connector.channel.examples;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Error details")
public class ErrorDetailsDto {

    @Schema(description = "Error category", example = "Validation")
    public String errorCategory;

    @Schema(description = "Error code", example = "error.msg.schema.payee.party.id.type.cannot.be.null.or.empty")
    public String errorCode;

    @Schema(description = "Error description", example = "Payee party Id Type cannot be null or empty")
    public String errorDescription;

    @Schema(description = "Error parameters", example = "null")
    public Object errorParameters;
}
