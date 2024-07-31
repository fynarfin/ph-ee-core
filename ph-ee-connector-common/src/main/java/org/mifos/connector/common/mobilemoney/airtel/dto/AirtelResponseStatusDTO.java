package org.mifos.connector.common.mobilemoney.airtel.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AirtelResponseStatusDTO {

    private String code;
    private String message;
    private String resultCode;
    private String responseCode;
    private Boolean success;
}
