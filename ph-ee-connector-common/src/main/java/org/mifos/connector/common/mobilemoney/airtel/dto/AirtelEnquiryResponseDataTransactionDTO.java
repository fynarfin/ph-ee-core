package org.mifos.connector.common.mobilemoney.airtel.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AirtelEnquiryResponseDataTransactionDTO {

    private String airtelMoneyId;
    private String id;
    private String message;
    private String status;
}
