package org.mifos.connector.mockpaymentschema.schema;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Payer {
    private String partyIdType;
    private String partyId;
}