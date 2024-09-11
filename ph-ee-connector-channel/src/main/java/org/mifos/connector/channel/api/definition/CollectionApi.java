package org.mifos.connector.channel.api.definition;

import static org.mifos.connector.channel.camel.config.CamelProperties.COUNTRY;
import static org.mifos.connector.channel.camel.config.CamelProperties.PAYMENT_SCHEME_HEADER;
import static org.mifos.connector.channel.camel.config.CamelProperties.X_CALLBACKURL;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.concurrent.ExecutionException;
import org.mifos.connector.channel.examples.SuccessfulTransferResponseDto;
import org.mifos.connector.channel.gsma_api.GsmaP2PResponseDto;
import org.mifos.connector.channel.model.CollectionRequestDTO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

public interface CollectionApi {

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Accepted: Transaction id generated", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SuccessfulTransferResponseDto.class))) })
    @PostMapping("/channel/collection")
    GsmaP2PResponseDto collection(@RequestHeader(value = "Platform-TenantId") String tenant,
            @RequestHeader(value = "X-CorrelationID") String correlationId,
            @RequestHeader(value = PAYMENT_SCHEME_HEADER, required = false) String paymentScheme,
            @RequestHeader(value = COUNTRY) String country, @RequestHeader(value = X_CALLBACKURL) String callbackUrl,
            @RequestBody CollectionRequestDTO requestBody) throws ExecutionException, InterruptedException, JsonProcessingException;

}
