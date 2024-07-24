package org.mifos.connector.channel.examples;

import io.swagger.v3.oas.annotations.media.Schema;

public class SuccessfulTxnStateResponseDTO {

    @Schema(description = "Transaction ID", example = "c3ce2b92-85e1-42b1-a71f-08f2ee44d42b")
    public String transactionId;

    @Schema(description = "transferState", example = "RECEIVED")
    public String transferState;
}
