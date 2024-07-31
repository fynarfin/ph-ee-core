package org.mifos.connector.channel.api.implementation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.camunda.zeebe.client.api.command.ClientStatusException;
import io.grpc.Status;
import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.mifos.connector.channel.api.definition.GSMATransactionApi;
import org.mifos.connector.channel.gsma_api.GsmaP2PResponseDto;
import org.mifos.connector.channel.service.ValidateHeaders;
import org.mifos.connector.channel.utils.HeaderConstants;
import org.mifos.connector.channel.utils.Headers;
import org.mifos.connector.channel.utils.SpringWrapperUtil;
import org.mifos.connector.channel.validator.ChannelValidator;
import org.mifos.connector.channel.validator.HeaderValidator;
import org.mifos.connector.common.exception.ValidationException;
import org.mifos.connector.common.gsma.dto.GsmaTransfer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GSMATransactionApiController implements GSMATransactionApi {

    @Autowired
    private ProducerTemplate producerTemplate;

    @Autowired
    ObjectMapper objectMapper;
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    @ValidateHeaders(requiredHeaders = { HeaderConstants.AMS_NAME, HeaderConstants.ACCOUNT_HOLDING_INSTITUTION_ID,
            HeaderConstants.X_Callback_URL,
            HeaderConstants.CLIENT_CORRELATION_ID }, validatorClass = HeaderValidator.class, validationFunction = "validateGsmaTransaction")
    public ResponseEntity<GsmaP2PResponseDto> gsmatransaction(GsmaTransfer requestBody, String correlationId, String amsName,
            String accountHoldId, String callbackURL) throws JsonProcessingException {

        try {
            ChannelValidator.validateTransaction(requestBody);
        } catch (ValidationException e) {
            throw e;
        }

        Headers headers = new Headers.HeaderBuilder().addHeader("X-CorrelationID", correlationId).addHeader("amsName", amsName)
                .addHeader("accountHoldingInstitutionId", accountHoldId).addHeader("X-CallbackURL", callbackURL).build();
        Exchange exchange = SpringWrapperUtil.getDefaultWrappedExchange(producerTemplate.getCamelContext(), headers,
                objectMapper.writeValueAsString(requestBody));
        producerTemplate.send("direct:post-gsma-transaction", exchange);

        Exception cause = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Exception.class);
        if (cause instanceof ClientStatusException) {
            throw new ClientStatusException(Status.FAILED_PRECONDITION, cause);
        }

        String body = exchange.getIn().getBody(String.class);
        GsmaP2PResponseDto responseDto = objectMapper.readValue(body, GsmaP2PResponseDto.class);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(responseDto);
    }
}
