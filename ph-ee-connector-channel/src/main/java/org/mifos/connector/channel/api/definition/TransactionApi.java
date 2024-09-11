package org.mifos.connector.channel.api.definition;

import static org.mifos.connector.channel.camel.config.CamelProperties.CALLBACK_URL;
import static org.mifos.connector.channel.camel.config.CamelProperties.CLIENTCORRELATIONID;
import static org.mifos.connector.channel.zeebe.ZeebeVariables.TRANSACTION_ID;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.mifos.connector.channel.examples.InvalidRequestBodyResponseDto;
import org.mifos.connector.channel.examples.SuccessfulTransferResponseDto;
import org.mifos.connector.channel.gsma_api.GsmaP2PResponseDto;
import org.mifos.connector.common.channel.dto.TransactionChannelRequestDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

public interface TransactionApi {

    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Accepted: Transaction id generated", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SuccessfulTransferResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = InvalidRequestBodyResponseDto.class))) })
    @PostMapping("/channel/transactionRequest")
    ResponseEntity<GsmaP2PResponseDto> transaction(@RequestHeader(value = "Platform-TenantId") String tenant,
            @RequestHeader(value = CLIENTCORRELATIONID, required = false) String correlationId,
            @RequestHeader(value = CALLBACK_URL, required = false) String callbackURL,
            @RequestBody TransactionChannelRequestDTO requestBody) throws JsonProcessingException;

    @PostMapping("/channel/transaction/{" + TRANSACTION_ID + "}/resolve")
    void transactionResolve(@RequestBody String requestBody) throws JsonProcessingException;
}
