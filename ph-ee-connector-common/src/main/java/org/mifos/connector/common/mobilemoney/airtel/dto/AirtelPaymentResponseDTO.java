package org.mifos.connector.common.mobilemoney.airtel.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AirtelPaymentResponseDTO {

    private AirtelPaymentResponseDataDTO data;
    private AirtelResponseStatusDTO status;
}
