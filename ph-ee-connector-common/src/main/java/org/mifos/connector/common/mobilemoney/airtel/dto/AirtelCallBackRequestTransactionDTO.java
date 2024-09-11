package org.mifos.connector.common.mobilemoney.airtel.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AirtelCallBackRequestTransactionDTO {

    private String id;
    private String message;
    private String statusCode;
    private String airtelMoneyId;
}
