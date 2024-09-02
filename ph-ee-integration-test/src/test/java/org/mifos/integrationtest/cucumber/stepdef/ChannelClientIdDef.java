package org.mifos.integrationtest.cucumber.stepdef;

import static com.google.common.truth.Truth.assertThat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.And;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.mifos.integrationtest.common.Utils;
import org.mifos.integrationtest.config.ChannelConnectorConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ChannelClientIdDef extends BaseStepDef {

    @Value("${operations-app.auth.enabled}")
    public Boolean authEnabled;

    @Autowired
    ChannelConnectorConfig channelConnectorConfig;

    private String clientCorrelationId = "123456789";

    @And("I have request type as {string}")
    public void iHaveRequestTypeAs(String requestType) {
        scenarioScopeState.requestType = requestType;
        channelConnectorConfig.setRequestType(requestType);
        assertThat(scenarioScopeState.requestType).isNotEmpty();
    }

    @And("I should have clientRefId in response")
    public void iShouldHaveClientRefIdInResponse() {
        assertThat(scenarioScopeState.response).containsMatch("clientRefId");
    }

    @When("I call the transfer API with expected status of {int}")
    public void iCallTheTransferAPIWithExpectedStatusOf(int expectedStatus) {
        RequestSpecification requestSpec = Utils.getDefaultSpec(scenarioScopeState.tenant);
        if (authEnabled) {
            requestSpec.header("Authorization", "Bearer " + scenarioScopeState.accessToken);
        }

        scenarioScopeState.response = RestAssured.given(requestSpec).baseUri(operationsAppConfig.operationAppContactPoint).expect()
                .spec(new ResponseSpecBuilder().expectStatusCode(expectedStatus).build()).when().get(operationsAppConfig.transfersEndpoint)
                .andReturn().asString();

        logger.info("Inbound transfer Response: {}", scenarioScopeState.response);
    }

    @When("I call the txn State with client correlation id as {string} expected status of {int}")
    public void iCallTheTxnStateWithClientCorrelationIdAsExpectedStatusOf(String XClientCorrelationId, int expectedStatus) {
        RequestSpecification requestSpec = Utils.getDefaultSpec(scenarioScopeState.tenant);
        if (authEnabled) {
            requestSpec.header("Authorization", "Bearer " + scenarioScopeState.accessToken);
        }
        requestSpec.header(Utils.REQUEST_TYPE_PARAM_NAME, channelConnectorConfig.getRequestType());
        scenarioScopeState.response = RestAssured.given(requestSpec).baseUri(channelConnectorConfig.channelConnectorContactPoint).expect()
                .spec(new ResponseSpecBuilder().expectStatusCode(expectedStatus).build()).when()
                .get("/channel/txnState/" + XClientCorrelationId).andReturn().asString();

        logger.info("Txn Req response: {}", scenarioScopeState.response);
    }

    @When("I call the transfer API with size {int} and page {int} expecting expected status of {int}")
    public void iCallTheTransferAPIWithSizeAndPageExpectingExpectedStatusOf(float size, float page, int expectedStatus) {
        RequestSpecification requestSpec = Utils.getDefaultSpec(scenarioScopeState.tenant);
        if (authEnabled) {
            requestSpec.header("Authorization", "Bearer " + scenarioScopeState.accessToken);
        }

        String endpoint = String.format("%s?size=%f&page=%f", operationsAppConfig.transfersEndpoint, size, page);
        logger.info("Calling endpoint with size and page: {}", endpoint);

        scenarioScopeState.response = RestAssured.given(requestSpec).baseUri(operationsAppConfig.operationAppContactPoint).expect()
                .spec(new ResponseSpecBuilder().expectStatusCode(expectedStatus).build()).when().get(endpoint).andReturn().asString();

        logger.info("Inbound transfer with size and page Response: {}", scenarioScopeState.response);
    }

    @When("I call the transfer API with end date is before the start date expecting expected status of {int}")
    public void iCallTheTransferAPIWithEndDateBeforeStartDateExpectingExpectedStatusOf(int expectedStatus) {
        RequestSpecification requestSpec = Utils.getDefaultSpec(scenarioScopeState.tenant);
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.minusDays(5);
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;

        // Set the dates with endDate before startDate
        String startfrom = startDate.format(formatter);
        String startto = endDate.format(formatter);

        String endpoint = String.format("%s?startfrom=%s&startto=%s", operationsAppConfig.transfersEndpoint, startfrom, startto);
        String fullUrl = operationsAppConfig.operationAppContactPoint + endpoint;

        logger.info("Calling endpoint: {}", fullUrl);

        scenarioScopeState.response = RestAssured.given(requestSpec).baseUri(operationsAppConfig.operationAppContactPoint).expect()
                .spec(new ResponseSpecBuilder().expectStatusCode(expectedStatus).build()).when().get(endpoint).andReturn().asString();

        logger.info("Inbound transfer with date range Response: {}", scenarioScopeState.response);
    }

    @And("I should have page and size in response")
    public void iShouldHavePageAndSizeInResponse() throws JsonProcessingException {
        String response = scenarioScopeState.response;

        // Parse the JSON response
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(response);

        // Check that 'size' and 'number' are present
        assertThat(jsonNode.has("size")).isTrue();
        assertThat(jsonNode.has("number")).isTrue();

        // Check the exact values
        int expectedSize = 4;
        int expectedNumber = 2;
        assertThat(jsonNode.get("size").asInt()).isEqualTo(expectedSize);
        assertThat(jsonNode.get("number").asInt()).isEqualTo(expectedNumber);
    }

    @When("I call the transfer API with currency {string} and amount {int} expecting expected status of {int}")
    public void iCallTheTransferAPIWithCurrencyAndAmountExpectingExpectedStatusOf(String currency, float amount, int expectedStatus) {
        RequestSpecification requestSpec = Utils.getDefaultSpec(scenarioScopeState.tenant);
        if (authEnabled) {
            requestSpec.header("Authorization", "Bearer " + scenarioScopeState.accessToken);
        }

        String endpoint = String.format("%s?currency=%s&amount=%f", operationsAppConfig.transfersEndpoint, currency, amount);
        logger.info("Calling get transfer endpoint: {}", endpoint);

        scenarioScopeState.response = RestAssured.given(requestSpec).baseUri(operationsAppConfig.operationAppContactPoint).expect()
                .spec(new ResponseSpecBuilder().expectStatusCode(expectedStatus).build()).when().get(endpoint).andReturn().asString();

        logger.info("Inbound transfer with currency and amount Response: {}", scenarioScopeState.response);
    }

    @And("I should have currency and amount in response")
    public void iShouldHaveCurrencyAndAmountInResponse() throws JsonProcessingException {
        String response = scenarioScopeState.response;

        // Parse the JSON response
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(response);

        assertThat(jsonNode.has("content")).isTrue();
        assertThat(jsonNode.get("content").isArray()).isTrue();
        assertThat(jsonNode.get("content").size()).isGreaterThan(0);

        // Extract the first object in the 'content' array
        JsonNode firstContentItem = jsonNode.get("content").get(0);

        // Check that 'currency' and 'amount' are present in the first content item
        assertThat(firstContentItem.has("currency")).isTrue();
        assertThat(firstContentItem.has("amount")).isTrue();

        // Retrieve and validate the currency
        JsonNode currencyNode = firstContentItem.get("currency");
        assertThat(currencyNode).isNotNull();
        String currency = currencyNode.asText();
        String expectedCurrency = "USD";
        assertThat(currency).isEqualTo(expectedCurrency);

        // Retrieve and validate the amount
        JsonNode amountNode = firstContentItem.get("amount");
        assertThat(amountNode).isNotNull();
        String amountAsString = amountNode.asText();
        int amount = Integer.parseInt(amountAsString);
        int expectedAmount = 1;
        assertThat(amount).isEqualTo(expectedAmount);
    }

    @When("I call the transfer API with invalid sorting order")
    public void iCallTheTransferAPIWithInvalidSortingOrder() {

    }

    @When("I call the transfer API with invalid sorting order with expected status of {int}")
    public void iCallTheTransferAPIWithInvalidSortingOrderWithExpectedStatusOf(int expectedStatus) {
        RequestSpecification requestSpec = Utils.getDefaultSpec(scenarioScopeState.tenant);

        // Add sorted order param to endpoint
        String endpoint = String.format("%s?sortedOrder=reverse", operationsAppConfig.transfersEndpoint);
        String fullUrl = operationsAppConfig.operationAppContactPoint + endpoint;

        logger.info("Calling transfer endpoint with invalid sorting order: {}", fullUrl);

        scenarioScopeState.response = RestAssured.given(requestSpec)
                .baseUri(operationsAppConfig.operationAppContactPoint)
                .expect()
                .spec(new ResponseSpecBuilder().expectStatusCode(expectedStatus).build())
                .when()
                .get(endpoint)
                .andReturn()
                .asString();

        logger.info("Inbound transfer with invalid sorting order Response: {}", scenarioScopeState.response);
    }

    @When("I call the transfer API with invalid date format expecting expected status of {int}")
    public void iCallTheTransferAPIWithInvalidDateFormatExpectingExpectedStatusOf(int expectedStatus) {
        RequestSpecification requestSpec = Utils.getDefaultSpec(scenarioScopeState.tenant);

        // Set an invalid date format for start from
        String invalidStartDate = "invalid-date-format";

        // Construct the endpoint with the invalid start from parameter
        String endpoint = String.format("%s?startfrom=%s", operationsAppConfig.transfersEndpoint, invalidStartDate);
        String fullUrl = operationsAppConfig.operationAppContactPoint + endpoint;

        logger.info("Calling endpoint with invalid date format: {}", fullUrl);

        scenarioScopeState.response = RestAssured.given(requestSpec)
                .baseUri(operationsAppConfig.operationAppContactPoint)
                .expect()
                .spec(new ResponseSpecBuilder().expectStatusCode(expectedStatus).build())
                .when()
                .get(endpoint)
                .andReturn()
                .asString();

        logger.info("Inbound transfer with invalid start date Response: {}", scenarioScopeState.response);
    }

    @When("I call the transfer API with an empty start date expecting expected status of {int}")
    public void iCallTheTransferAPIWithEmptyStartDateExpectingExpectedStatusOf(int expectedStatus) {
        RequestSpecification requestSpec = Utils.getDefaultSpec(scenarioScopeState.tenant);

        // Set an empty value for start from
        String emptyStartDate = "";

        // the endpoint with the empty start from parameter
        String endpoint = String.format("%s?startfrom=%s", operationsAppConfig.transfersEndpoint, emptyStartDate);
        String fullUrl = operationsAppConfig.operationAppContactPoint + endpoint;

        logger.info("Calling endpoint with empty date: {}", fullUrl);

        scenarioScopeState.response = RestAssured.given(requestSpec)
                .baseUri(operationsAppConfig.operationAppContactPoint)
                .expect()
                .spec(new ResponseSpecBuilder().expectStatusCode(expectedStatus).build())
                .when()
                .get(endpoint)
                .andReturn()
                .asString();

        logger.info("Inbound transfer with empty start date Response: {}", scenarioScopeState.response);
    }

    @When("I call the transfer API with status set to PROCESSING expecting expected status of {int}")
    public void iCallTheTransferAPIWithStatusSetToPROCESSINGExpectingExpectedStatusOf(int expectedStatus) {
        RequestSpecification requestSpec = Utils.getDefaultSpec(scenarioScopeState.tenant);

        // Set the status value to PROCESSING
        String status = "PROCESSING";

        // Construct the endpoint with the status parameter
        String endpoint = String.format("%s?status=%s", operationsAppConfig.transfersEndpoint, status);
        String fullUrl = operationsAppConfig.operationAppContactPoint + endpoint;

        logger.info("Calling endpoint with invalid status param: {}", fullUrl);

        scenarioScopeState.response = RestAssured.given(requestSpec)
                .baseUri(operationsAppConfig.operationAppContactPoint)
                .expect()
                .spec(new ResponseSpecBuilder().expectStatusCode(expectedStatus).build())
                .when()
                .get(endpoint)
                .andReturn()
                .asString();

        logger.info("Get transfer with status 'PROCESSING' Response: {}", scenarioScopeState.response);
    }

    @When("I call the transfer API with clientCorrelationId of exceeding max length with status of {int}")
    public void iCallTheTransferAPIWithClientCorrelationIdOfExceedingMaxLengthWithStatusOf(int expectedStatus) {
        RequestSpecification requestSpec = Utils.getDefaultSpec(scenarioScopeState.tenant);

        // Set the clientCorrelationId value to an alphanumeric string longer than 12 characters
        String clientCorrelationId = "abc1234567890"; // Example alphanumeric string with 13 characters

        // Construct the endpoint with the clientCorrelationId parameter
        String endpoint = String.format("%s?clientCorrelationId=%s", operationsAppConfig.transfersEndpoint, clientCorrelationId);
        String fullUrl = operationsAppConfig.operationAppContactPoint + endpoint;

        logger.info("Calling endpoint with exceeding max length: {}", fullUrl);

        scenarioScopeState.response = RestAssured.given(requestSpec)
                .baseUri(operationsAppConfig.operationAppContactPoint)
                .expect()
                .spec(new ResponseSpecBuilder().expectStatusCode(expectedStatus).build())
                .when()
                .get(endpoint)
                .andReturn()
                .asString();

        logger.info("Get transfer with clientCorrelationId longer than 12 characters Response: {}", scenarioScopeState.response);
    }

    @When("I call the transfer API with invalid transactionId with status of {int}")
    public void iCallTheTransferAPIWithInvalidTransactionIdWithStatusOf(int expectedStatus) {
        RequestSpecification requestSpec = Utils.getDefaultSpec(scenarioScopeState.tenant);

        // Set the transactionId value to a random invalid value
        String transactionId = "abcdefgh";

        // Construct the endpoint with the clientCorrelationId parameter
        String endpoint = String.format("%s?transactionId=%s", operationsAppConfig.transfersEndpoint, transactionId);
        String fullUrl = operationsAppConfig.operationAppContactPoint + endpoint;

        logger.info("Calling endpoint with invalid transactionId: {}", fullUrl);

        scenarioScopeState.response = RestAssured.given(requestSpec)
                .baseUri(operationsAppConfig.operationAppContactPoint)
                .expect()
                .spec(new ResponseSpecBuilder().expectStatusCode(expectedStatus).build())
                .when()
                .get(endpoint)
                .andReturn()
                .asString();

        logger.info("Get transfer with transactionId Response: {}", scenarioScopeState.response);

    }
}
