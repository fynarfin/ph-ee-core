package org.mifos.connector.mockpaymentschema.schema;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestToPaySuccessResponse {
    private String financialTransactionId;
    private String externalId;
    private String amount;
    private String currency;
    private Payer payer;
    private String status;
}