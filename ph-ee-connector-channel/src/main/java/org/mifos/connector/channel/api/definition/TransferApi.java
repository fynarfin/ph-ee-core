package org.mifos.connector.channel.api.definition;

import static org.mifos.connector.channel.camel.config.CamelProperties.BATCH_ID_HEADER;
import static org.mifos.connector.channel.camel.config.CamelProperties.CLIENTCORRELATIONID;
import static org.mifos.connector.channel.camel.config.CamelProperties.PAYEE_DFSP_ID;
import static org.mifos.connector.channel.camel.config.CamelProperties.REGISTERING_INSTITUTION_ID;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.mifos.connector.channel.examples.InvalidRequestBodyResponseDto;
import org.mifos.connector.channel.examples.SuccessfulTransferResponseDto;
import org.mifos.connector.channel.gsma_api.GsmaP2PResponseDto;
import org.mifos.connector.common.channel.dto.TransactionChannelRequestDTO;
import org.mifos.connector.common.channel.dto.TransactionStatusResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

public interface TransferApi {

    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Accepted: Transaction id generated", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SuccessfulTransferResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = InvalidRequestBodyResponseDto.class))) })
    @PostMapping("/channel/transfer")
    ResponseEntity<GsmaP2PResponseDto> transfer(@RequestHeader(value = "Platform-TenantId") String tenant,
            @RequestHeader(value = BATCH_ID_HEADER, required = false) String batchId,
            @RequestHeader(value = CLIENTCORRELATIONID, required = false) String correlationId,
            @RequestHeader(value = REGISTERING_INSTITUTION_ID, required = false) String registeringInstitutionId,
            @RequestHeader(value = PAYEE_DFSP_ID, required = false) String payeeDfspId,
            @RequestBody TransactionChannelRequestDTO requestBody) throws JsonProcessingException;

    @GetMapping("/channel/transfer/{transactionId}")
    TransactionStatusResponseDTO transferId(@PathVariable String transactionId, @RequestHeader(value = "Platform-TenantId") String tenant)
            throws JsonProcessingException;
}
