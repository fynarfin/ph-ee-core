package org.mifos.connector.mockpaymentschema.service;


import org.mifos.connector.mockpaymentschema.schema.MtnRtpDTO;
import org.mifos.connector.mockpaymentschema.schema.Payer;
import org.mifos.connector.mockpaymentschema.schema.RequestToPayFailureResponse;
import org.mifos.connector.mockpaymentschema.schema.RequestToPaySuccessResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class MtnCollectionService {

    @Value("${mtn.payerIdentifier.reject}")
    private String rejectPayerId;

    @Value("${mtn.payerIdentifier.delay}")
    private String callbackDelayPayerId;

    public <T> ResponseEntity<T> requestToPay(MtnRtpDTO requestBody) {
        String partyId = requestBody.getPayer().getPartyIdType();
        if (partyId.equals(rejectPayerId)) {
            RequestToPayFailureResponse failureResponse = converterForFailureResponse(requestBody);
           return ResponseEntity.status(HttpStatus.ACCEPTED).body((T) failureResponse);
        }
        RequestToPaySuccessResponse successResponse = converterForSuccessfulResponse(requestBody);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body((T) successResponse);
    }

    public <T> ResponseEntity<T> getRequestToPayStatus(String referenceId) {
        Payer payer = Payer.builder()
                .partyIdType("MSISDN")
                .partyId(callbackDelayPayerId)
                .build();

        RequestToPaySuccessResponse response = RequestToPaySuccessResponse.builder().payer(payer).build();
        return ResponseEntity.status(HttpStatus.ACCEPTED).body((T) response);
    }


    private RequestToPayFailureResponse converterForFailureResponse(MtnRtpDTO requestToPayDTO) {

        return RequestToPayFailureResponse.builder()
                .externalId(requestToPayDTO.getExternalId())
                .amount(requestToPayDTO.getAmount())
                .currency(requestToPayDTO.getCurrency())
                .payer(requestToPayDTO.getPayer())
                .status("FAILED")
                .reason("APPROVAL_REJECTED")
                .build();
    }

    private RequestToPaySuccessResponse converterForSuccessfulResponse(MtnRtpDTO requestToPayDTO) {

        String financialTransactionId = UUID.randomUUID().toString().replace("-", "");
        return RequestToPaySuccessResponse.builder()
                .financialTransactionId(financialTransactionId)
                .externalId(requestToPayDTO.getExternalId())
                .amount(requestToPayDTO.getAmount())
                .currency(requestToPayDTO.getCurrency())
                .payer(requestToPayDTO.getPayer())
                .status("SUCCESSFUL")
                .build();
    }
}