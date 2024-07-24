package org.mifos.integrationtest.cucumber.stepdef;

import static com.google.common.truth.Truth.assertThat;

import io.cucumber.core.internal.com.fasterxml.jackson.databind.JsonNode;
import io.cucumber.core.internal.com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.mifos.connector.common.channel.dto.TransactionChannelRequestDTO;
import org.mifos.connector.common.mojaloop.dto.MoneyData;
import org.mifos.connector.common.mojaloop.dto.Party;
import org.mifos.connector.common.mojaloop.type.IdentifierType;
import org.mifos.integrationtest.common.TransactionHelper;
import org.mifos.integrationtest.common.Utils;
import org.mifos.integrationtest.common.dto.ErrorDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

public class ValidateChannelDef extends BaseStepDef {

    @Autowired
    ScenarioScopeState scenarioScopeState;

    Logger logger = LoggerFactory.getLogger(VoucherManagementStepDef.class);

    @Value("${tenantconfig.tenants.paymentbb2}")
    private String tenant;

    private String tenantHeader = "Platform-TenantId";

    @Given("I can create a TransactionChannelRequestDTO with no payee")
    public void iCanCreateATransactionChannelRequestDTOWithNoPayee() {
        TransactionHelper transactionHelper = new TransactionHelper();
        Party payer = transactionHelper.partyHelper(IdentifierType.MSISDN, "27710101999");
        MoneyData amount = transactionHelper.amountHelper("100", "SNR");
        TransactionChannelRequestDTO requestDTO = transactionHelper.transactionChannelRequestHelper(payer, null, amount);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            scenarioScopeState.createTransactionChannelRequestBody = objectMapper.writeValueAsString(requestDTO);
        } catch (Exception e) {
            logger.error("An Exception occurred", e);
        }
    }

    @When("I call the post transfer API with expected status of {int}")
    public void iCallThePostTransferAPIWithExpectedStatusOf(int expectedStatus) {
        RequestSpecification requestSpec = Utils.getDefaultSpec();
        scenarioScopeState.tenant = tenant;
        scenarioScopeState.response = RestAssured.given(requestSpec).header("Content-Type", "application/json")
                .header(tenantHeader, scenarioScopeState.tenant).baseUri(channelConnectorConfig.channelConnectorContactPoint)
                .body(scenarioScopeState.createTransactionChannelRequestBody).expect()
                .spec(new ResponseSpecBuilder().expectStatusCode(expectedStatus).build()).when()
                .post(channelConnectorConfig.transferEndpoint).andReturn().asString();

        logger.info("Post Transfer Response: {}", scenarioScopeState.response);
    }

    @When("I call the transaction request API with expected status of {int}")
    public void iCallTheTransactionRequestAPIWithExpectedStatusOf(int expectedStatus) {
        RequestSpecification requestSpec = Utils.getDefaultSpec();

        scenarioScopeState.tenant = tenant;
        scenarioScopeState.response = RestAssured.given(requestSpec).header("Content-Type", "application/json")
                .header(tenantHeader, scenarioScopeState.tenant).baseUri(channelConnectorConfig.channelConnectorContactPoint)
                .body(scenarioScopeState.createTransactionChannelRequestBody).expect()
                .spec(new ResponseSpecBuilder().expectStatusCode(expectedStatus).build()).when()
                .post(channelConnectorConfig.transferReqEndpoint).andReturn().asString();

        logger.info("Transaction Request Response: {}", scenarioScopeState.response);
    }

    @And("I should be able to assert the api validation for schema validation error response")
    public void iWillAssertTheFieldsFromValidationResponseForSchemaValidation() {
        try {
            JsonNode rootNode = objectMapper.readTree(scenarioScopeState.response);

            ErrorDetails errorDetails = objectMapper.treeToValue(rootNode, ErrorDetails.class);

            assertThat(errorDetails.getErrorCode()).isEqualTo("error.msg.schema.validation.errors");
            assertThat(errorDetails.getErrorDescription()).isEqualTo("The request is invalid");

        } catch (Exception e) {
            logger.info("An error occurred : {}", e);
        }
    }

    @And("I should be able to assert the api validation for header validation error response")
    public void iWillAssertTheFieldsFromValidationResponseForHeaderValidation() {
        try {
            JsonNode rootNode = objectMapper.readTree(scenarioScopeState.response);

            ErrorDetails errorDetails = objectMapper.treeToValue(rootNode, ErrorDetails.class);

            assertThat(errorDetails.getErrorCode()).isEqualTo("error.msg.header.validation.errors");
            assertThat(errorDetails.getErrorDescription()).isEqualTo("The headers are invalid");

        } catch (Exception e) {
            logger.info("An error occurred : {}", e);
        }
    }

    @Given("I can create a TransactionChannelRequestDTO")
    public void iCreateATransactionChannelRequestDTO() {
        TransactionHelper transactionHelper = new TransactionHelper();
        Party payer = transactionHelper.partyHelper(IdentifierType.MSISDN, "27710101999");
        Party payee = transactionHelper.partyHelper(IdentifierType.MSISDN, "27710101999");
        MoneyData amount = transactionHelper.amountHelper("100", "SNR");
        TransactionChannelRequestDTO requestDTO = transactionHelper.transactionChannelRequestHelper(payer, payee, amount);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            scenarioScopeState.createTransactionChannelRequestBody = objectMapper.writeValueAsString(requestDTO);
        } catch (Exception e) {
            logger.error("An Exception occurred", e);
        }
    }

    @When("I call the post transfer API having unsupported header with expected status of {int}")
    public void iCallThePostTransferAPIHavingUnsupportedHeaderWithExpectedStatusOf(int expectedStatus) {
        RequestSpecification requestSpec = Utils.getDefaultSpec();
        scenarioScopeState.tenant = tenant;
        scenarioScopeState.response = RestAssured.given(requestSpec).header("Content-Type", "application/json")
                .header(tenantHeader, scenarioScopeState.tenant).header("invalid-header", "test")
                .baseUri(channelConnectorConfig.channelConnectorContactPoint).body(scenarioScopeState.createTransactionChannelRequestBody)
                .expect().spec(new ResponseSpecBuilder().expectStatusCode(expectedStatus).build()).when()
                .post(channelConnectorConfig.transferEndpoint).andReturn().asString();

        logger.info("Post Transfer Response: {}", scenarioScopeState.response);
    }

    @When("I call the transaction request API having unsupported header with expected status of {int}")
    public void iCallTheTransactionRequestAPIHavingUnsupportedHeaderWithExpectedStatusOf(int expectedStatus) {
        RequestSpecification requestSpec = Utils.getDefaultSpec();
        scenarioScopeState.tenant = tenant;
        scenarioScopeState.response = RestAssured.given(requestSpec).header("Content-Type", "application/json")
                .header(tenantHeader, scenarioScopeState.tenant).header("invalid-header", "test")
                .baseUri(channelConnectorConfig.channelConnectorContactPoint).body(scenarioScopeState.createTransactionChannelRequestBody)
                .expect().spec(new ResponseSpecBuilder().expectStatusCode(expectedStatus).build()).when()
                .post(channelConnectorConfig.transferReqEndpoint).andReturn().asString();

        logger.info("Transaction Request Response: {}", scenarioScopeState.response);
    }

    @When("I call the post transfer API without required header Platform-TenantId with expected status of {int}")
    public void iCallThePostTransferAPINotHavingRequiredHeaderPlatformTenantIdWithExpectedStatusOf(int expectedStatus) {
        RequestSpecification requestSpec = Utils.getDefaultSpec();
        scenarioScopeState.response = RestAssured.given(requestSpec).header("Content-Type", "application/json")
                .baseUri(channelConnectorConfig.channelConnectorContactPoint).body(scenarioScopeState.createTransactionChannelRequestBody)
                .expect().spec(new ResponseSpecBuilder().expectStatusCode(expectedStatus).build()).when()
                .post(channelConnectorConfig.transferEndpoint).andReturn().asString();

        logger.info("Post Transfer Response: {}", scenarioScopeState.response);
    }

    @When("I call the transaction request API without required header Platform-TenantId with expected status of {int}")
    public void iCallTheTransactionRequestAPINotHavingRequiredHeaderPlatformTenantIdWithExpectedStatusOf(int expectedStatus) {
        RequestSpecification requestSpec = Utils.getDefaultSpec();
        scenarioScopeState.response = RestAssured.given(requestSpec).header("Content-Type", "application/json")
                .baseUri(channelConnectorConfig.channelConnectorContactPoint).body(scenarioScopeState.createTransactionChannelRequestBody)
                .expect().spec(new ResponseSpecBuilder().expectStatusCode(expectedStatus).build()).when()
                .post(channelConnectorConfig.transferReqEndpoint).andReturn().asString();

        logger.info("Transaction Request Response: {}", scenarioScopeState.response);
    }
}
