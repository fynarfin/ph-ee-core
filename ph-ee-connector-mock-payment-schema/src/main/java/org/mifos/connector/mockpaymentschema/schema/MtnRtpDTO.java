package org.mifos.connector.mockpaymentschema.schema;

import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MtnRtpDTO {
    private String amount;
    private String currency;
    private String externalId;
    private Payer payer;
}
