package org.mifos.connector.channel.examples;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Example response for a successful transfer")
public class SuccessfulTransferResponseDto {

    @Schema(description = "Transaction ID", example = "c3ce2b92-85e1-42b1-a71f-08f2ee44d42b")
    public String transactionId;
}
