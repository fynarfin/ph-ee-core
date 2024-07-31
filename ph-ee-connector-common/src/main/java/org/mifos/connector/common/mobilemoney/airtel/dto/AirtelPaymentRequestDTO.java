package org.mifos.connector.common.mobilemoney.airtel.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AirtelPaymentRequestDTO {

    private String reference;
    private AirtelPaymentRequestSubscriberDTO subscriber;
    private AirtelPaymentRequestTransactionDTO transaction;
}
