/**
 * NOTE: This class is auto generated by the swagger code generator program (3.0.34).
 * https://github.com/swagger-api/swagger-codegen
 * Do not edit the class manually.
 */
package org.mifos.connector.gsmastub.api;

import org.mifos.connector.gsmastub.model.ErrorObject;
import org.mifos.connector.gsmastub.model.RequestGenericPatch;
import org.mifos.connector.gsmastub.model.RequestStateObject;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.threeten.bp.OffsetDateTime;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import java.util.List;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2022-09-27T11:46:46.417Z[GMT]")
@Validated
public interface RequeststatesApi extends BaseGsmaApi  {

    @Operation(summary = "View A Request State", description = "This endpoint returns a specific request state", tags={ "Supporting" })
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Represents an Asynchronous response", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RequestStateObject.class))),
        
        @ApiResponse(responseCode = "400", description = "Represents an Error Caused by the Violation of a Business Rule", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorObject.class))),
        
        @ApiResponse(responseCode = "401", description = "Represents an Error Caused by an Authorisation Failure", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorObject.class))),
        
        @ApiResponse(responseCode = "404", description = "Represents an Error Caused by a Failure to Identify the Target Resource", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorObject.class))),
        
        @ApiResponse(responseCode = "500", description = "Represents an Error Caused by a General Server-Side Issue", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorObject.class))),
        
        @ApiResponse(responseCode = "503", description = "Represents an Error Caused by System Unavailability", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorObject.class))) })
    @RequestMapping(value = "/requeststates/{serverCorrelationId}",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    ResponseEntity<RequestStateObject> requeststatesServerCorrelationIdGET(@Pattern(regexp="^[0-9A-Fa-f]{8}-[0-9A-Fa-f]{4}-[0-9A-Fa-f]{4}-[0-9A-Fa-f]{4}-[0-9A-Fa-f]{12}$") @Parameter(in = ParameterIn.PATH, description = "Path variable to uniquely identify a request state. Must be supplied as a UUID.", required=true, schema=@Schema()) @PathVariable("serverCorrelationId") String serverCorrelationId, @Parameter(in = ParameterIn.HEADER, description = "Header parameter to indicate the date and time that the message was originated. It is used for basic message integrity checks, to ensure the request is not stale. Note that the header was previously referenced as 'Date' in version 1.0 of the Mobile Money API." ,schema=@Schema()) @RequestHeader(value="X-Date", required=false) OffsetDateTime xDate, @Parameter(in = ParameterIn.HEADER, description = "Used to pass pre-shared client's API key to the server." ,schema=@Schema()) @RequestHeader(value="X-API-Key", required=false) String xAPIKey, @Parameter(in = ParameterIn.HEADER, description = "Used to pass pre-shared client's identifier to the server." ,schema=@Schema()) @RequestHeader(value="X-Client-Id", required=false) String xClientId, @Parameter(in = ParameterIn.HEADER, description = "SHA-256 hex digest of the request content (encrypted or plain). Applicable only if basic data integrity checking is to be performed." ,schema=@Schema()) @RequestHeader(value="X-Content-Hash", required=false) String xContentHash, @Parameter(in = ParameterIn.HEADER, description = "The end-users encrypted security credential. Should only be used when OAuth 2.0/OIDC authorisation framework has not been implemented by the API Provider." ,schema=@Schema()) @RequestHeader(value="X-User-Credential-1", required=false) String xUserCredential1, @Parameter(in = ParameterIn.HEADER, description = "The end-users encrypted security credential Should only be used when OAuth 2.0/OIDC authorisation framework has not been implemented by the API Provider." ,schema=@Schema()) @RequestHeader(value="X-User-Credential-2", required=false) String xUserCredential2, @Parameter(in = ParameterIn.HEADER, description = "String containing the channel that was used to originate the request. For example USSD, Web, App." ,schema=@Schema()) @RequestHeader(value="X-Channel", required=false) String xChannel);


    @Operation(summary = "Update A Request State", description = "This endpoint updates a specific request state. This operation is to be deprecated. Please refer to Callback definitions for the revised approach to implementing asynchronous callbacks.", tags={ "Supporting" })
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "204", description = "An empty response is returned for a synchronous successful patch."),
        
        @ApiResponse(responseCode = "400", description = "Represents an Error Caused by the Violation of a Business Rule", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorObject.class))),
        
        @ApiResponse(responseCode = "401", description = "Represents an Error Caused by an Authorisation Failure", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorObject.class))),
        
        @ApiResponse(responseCode = "404", description = "Represents an Error Caused by a Failure to Identify the Target Resource", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorObject.class))),
        
        @ApiResponse(responseCode = "500", description = "Represents an Error Caused by a General Server-Side Issue", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorObject.class))),
        
        @ApiResponse(responseCode = "503", description = "Represents an Error Caused by System Unavailability", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorObject.class))) })
    @RequestMapping(value = "/requeststates/{serverCorrelationId}",
        produces = { "application/json" }, 
        consumes = { "application/json" }, 
        method = RequestMethod.PATCH)
    ResponseEntity<Void> requeststatesServerCorrelationIdPATCH(@Pattern(regexp="^[0-9A-Fa-f]{8}-[0-9A-Fa-f]{4}-[0-9A-Fa-f]{4}-[0-9A-Fa-f]{4}-[0-9A-Fa-f]{12}$") @Parameter(in = ParameterIn.PATH, description = "Path variable to uniquely identify a request state. Must be supplied as a UUID.", required=true, schema=@Schema()) @PathVariable("serverCorrelationId") String serverCorrelationId, @Parameter(in = ParameterIn.DEFAULT, description = "Represents the request body of a batch of generic Patch operation.", required=true, schema=@Schema()) @Valid @RequestBody List<RequestGenericPatch> body, @Parameter(in = ParameterIn.HEADER, description = "Header parameter to indicate the date and time that the message was originated. It is used for basic message integrity checks, to ensure the request is not stale. Note that the header was previously referenced as 'Date' in version 1.0 of the Mobile Money API." ,schema=@Schema()) @RequestHeader(value="X-Date", required=false) OffsetDateTime xDate, @Parameter(in = ParameterIn.HEADER, description = "Header parameter to uniquely identify the request. Must be supplied as a UUID." ,schema=@Schema()) @RequestHeader(value="X-CorrelationID", required=false) String xCorrelationID, @Parameter(in = ParameterIn.HEADER, description = "Used to pass pre-shared client's API key to the server." ,schema=@Schema()) @RequestHeader(value="X-API-Key", required=false) String xAPIKey, @Parameter(in = ParameterIn.HEADER, description = "Used to pass pre-shared client's identifier to the server." ,schema=@Schema()) @RequestHeader(value="X-Client-Id", required=false) String xClientId, @Parameter(in = ParameterIn.HEADER, description = "SHA-256 hex digest of the request content (encrypted or plain). Applicable only if basic data integrity checking is to be performed." ,schema=@Schema()) @RequestHeader(value="X-Content-Hash", required=false) String xContentHash, @Parameter(in = ParameterIn.HEADER, description = "The end-users encrypted security credential. Should only be used when OAuth 2.0/OIDC authorisation framework has not been implemented by the API Provider." ,schema=@Schema()) @RequestHeader(value="X-User-Credential-1", required=false) String xUserCredential1, @Parameter(in = ParameterIn.HEADER, description = "The end-users encrypted security credential Should only be used when OAuth 2.0/OIDC authorisation framework has not been implemented by the API Provider." ,schema=@Schema()) @RequestHeader(value="X-User-Credential-2", required=false) String xUserCredential2, @Parameter(in = ParameterIn.HEADER, description = "String containing the channel that was used to originate the request. For example USSD, Web, App." ,schema=@Schema()) @RequestHeader(value="X-Channel", required=false) String xChannel);

}

