package org.mifos.connector.channel.api.implementation;

import static org.mifos.connector.channel.camel.config.CamelProperties.CLIENTCORRELATIONID;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.camunda.zeebe.client.api.command.ClientStatusException;
import io.grpc.Status;
import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.mifos.connector.channel.api.definition.TransactionApi;
import org.mifos.connector.channel.gsma_api.GsmaP2PResponseDto;
import org.mifos.connector.channel.service.ValidateHeaders;
import org.mifos.connector.channel.utils.HeaderConstants;
import org.mifos.connector.channel.utils.Headers;
import org.mifos.connector.channel.utils.SpringWrapperUtil;
import org.mifos.connector.channel.validator.ChannelValidator;
import org.mifos.connector.channel.validator.HeaderValidator;
import org.mifos.connector.common.channel.dto.TransactionChannelRequestDTO;
import org.mifos.connector.common.exception.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TransactionApiController implements TransactionApi {

    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    private ProducerTemplate producerTemplate;

    @Override
    @ValidateHeaders(requiredHeaders = { HeaderConstants.PLATFORM_TENANT_ID,
            HeaderConstants.CLIENT_CORRELATION_ID }, validatorClass = HeaderValidator.class, validationFunction = "validateTransactionRequest")
    public ResponseEntity<GsmaP2PResponseDto> transaction(String tenant, String correlationId, TransactionChannelRequestDTO requestBody)
            throws JsonProcessingException {

        try {
            ChannelValidator.validateTransfer(requestBody);
        } catch (ValidationException e) {
            throw e;
        }

        Headers headers = new Headers.HeaderBuilder().addHeader("Platform-TenantId", tenant).addHeader(CLIENTCORRELATIONID, correlationId)
                .build();
        Exchange exchange = SpringWrapperUtil.getDefaultWrappedExchange(producerTemplate.getCamelContext(), headers,
                objectMapper.writeValueAsString(requestBody));
        producerTemplate.send("direct:post-transaction-request", exchange);

        Exception cause = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Exception.class);
        if (cause instanceof ClientStatusException) {
            throw new ClientStatusException(Status.FAILED_PRECONDITION, cause);
        }

        String body = exchange.getIn().getBody(String.class);
        GsmaP2PResponseDto responseDto = objectMapper.readValue(body, GsmaP2PResponseDto.class);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(responseDto);
    }

    @Override
    public void transactionResolve(String requestBody) throws JsonProcessingException {
        Headers headers = new Headers.HeaderBuilder().build();
        Exchange exchange = SpringWrapperUtil.getDefaultWrappedExchange(producerTemplate.getCamelContext(), null, requestBody);
        producerTemplate.send("direct:post-transaction-resolve", exchange);

    }
}
