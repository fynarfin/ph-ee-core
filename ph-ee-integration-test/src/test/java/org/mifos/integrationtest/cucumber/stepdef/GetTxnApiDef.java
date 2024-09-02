package org.mifos.integrationtest.cucumber.stepdef;

import static com.google.common.truth.Truth.assertThat;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;

import com.google.gson.Gson;
import io.cucumber.java.en.And;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.RequestSpecification;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.json.JSONException;
import org.json.JSONObject;
import org.mifos.integrationtest.common.CollectionHelper;
import org.mifos.integrationtest.common.Utils;
import org.mifos.integrationtest.common.dto.CollectionResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

public class GetTxnApiDef extends BaseStepDef {

    @Autowired
    ScenarioScopeState scenarioScopeState;
    private String clientCorrelationId = "123456789";

    @Value("${operations-app.auth.enabled}")
    public Boolean authEnabled;

    @When("I call the get txn API with expected status of {int}")
    public void callTxnReqApi(int expectedStatus) {
        await().atMost(awaitMost, SECONDS).pollDelay(pollDelay, SECONDS).pollInterval(pollInterval, SECONDS).untilAsserted(() -> {
            RequestSpecification requestSpec = Utils.getDefaultSpec(scenarioScopeState.tenant);
            if (authEnabled) {
                requestSpec.header("Authorization", "Bearer " + scenarioScopeState.accessToken);
            }

            scenarioScopeState.response = RestAssured.given(requestSpec).baseUri(operationsAppConfig.operationAppContactPoint).expect()
                    .spec(new ResponseSpecBuilder().expectStatusCode(expectedStatus).build()).when()
                    .get(operationsAppConfig.transactionRequestsEndpoint).andReturn().asString();
            assertThat(scenarioScopeState.response).isNotNull();
            logger.info("GetTxn Request Response: " + scenarioScopeState.response);
        });
    }

    @And("I should have clientCorrelationId in response")
    public void checkClientCorrelationId() {
        assertThat(scenarioScopeState.response).containsMatch("clientCorrelationId");
    }

    @When("I call the get txn API with date today minus {int} day and {string} with expected status of {int}")
    public void callTxnReqApiwithParams(int daydiff, String endDate, int expectedStatus) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");
        if ("current date".equals(endDate)) {
            endDate = formatter.format(LocalDateTime.now().plusDays(1));
        }
        String startDate = formatter.format(LocalDateTime.now().minusDays(daydiff));
        RequestSpecification requestSpec = Utils.getDefaultSpec(scenarioScopeState.tenant);
        if (authEnabled) {
            requestSpec.header("Authorization", "Bearer " + scenarioScopeState.accessToken);
        }
        requestSpec.queryParam("startFrom", startDate);
        requestSpec.queryParam("startTo", endDate);
        scenarioScopeState.response = RestAssured.given(requestSpec).baseUri(operationsAppConfig.operationAppContactPoint).expect()
                .spec(new ResponseSpecBuilder().expectStatusCode(expectedStatus).build()).when()
                .get(operationsAppConfig.transactionRequestsEndpoint).andReturn().asString();

        logger.info("GetTxn Request Response: " + scenarioScopeState.response);

    }

    @And("I should have startedAt and completedAt in response")
    public void checkDate() {
        assertThat(scenarioScopeState.response).containsMatch("startedAt");
        assertThat(scenarioScopeState.response).containsMatch("completedAt");
    }

    @And("I call collection api with expected status {int}")
    public void iCallCollectionApiWithExpectedStatus(int expectedStatus) throws JSONException {
        RequestSpecification requestSpec = Utils.getDefaultSpec(scenarioScopeState.tenant);
        requestSpec.header(Utils.X_CORRELATIONID, clientCorrelationId);
        JSONObject collectionRequestBody = CollectionHelper.getCollectionRequestBody("1", "254708374149", "24450523");
        logger.info(String.valueOf(collectionRequestBody));
        String json = RestAssured.given(requestSpec).baseUri(channelConnectorConfig.channelConnectorContactPoint)
                .body(collectionRequestBody.toString()).expect().spec(new ResponseSpecBuilder().expectStatusCode(expectedStatus).build())
                .when().post("/channel/collection").andReturn().asString();
        CollectionResponse response = (new Gson()).fromJson(json, CollectionResponse.class);
        assertThat(response.getTransactionId()).isNotEmpty();
    }
    @When("I call the get txn API with end date before start date expecting status of {int}")
    public void iCallTheGetTxnAPIWithEndDateBeforeStartDateExpectingStatusOf(int expectedStatus) throws JSONException {
        RequestSpecification requestSpec = Utils.getDefaultSpec(scenarioScopeState.tenant);
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.minusDays(5);
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;

        // Set the dates with endDate before startDate
        String startfrom = startDate.format(formatter);
        String startto = endDate.format(formatter);

        String endpoint = String.format("%s?startfrom=%s&startto=%s", operationsAppConfig.transactionRequestsEndpoint, startfrom, startto);
        String fullUrl = operationsAppConfig.operationAppContactPoint + endpoint;

        logger.info("Calling endpoint: {}", fullUrl);

        scenarioScopeState.response = RestAssured.given(requestSpec).baseUri(operationsAppConfig.operationAppContactPoint).expect()
                .spec(new ResponseSpecBuilder().expectStatusCode(expectedStatus).build()).when().get(endpoint).andReturn().asString();

        logger.info("Calling Transaction request with future date range Response: {}", scenarioScopeState.response);
    }

    @When("I call the get txn API with empty query params expecting status of {int}")
    public void iCallTheGetTxnAPIWithEmptyQueryParamsExpectingStatusOf(int expectedStatus) {
        RequestSpecification requestSpec = Utils.getDefaultSpec(scenarioScopeState.tenant);

        // Set an empty value for start from
        String emptyStartDate = "";

        // the endpoint with the empty start from parameter
        String endpoint = String.format("%s?startfrom=%s", operationsAppConfig.transactionRequestsEndpoint, emptyStartDate);
        String fullUrl = operationsAppConfig.operationAppContactPoint + endpoint;

        logger.info("Calling endpoint with empty date: {}", fullUrl);

        scenarioScopeState.response = RestAssured.given(requestSpec).baseUri(operationsAppConfig.operationAppContactPoint).expect()
                .spec(new ResponseSpecBuilder().expectStatusCode(expectedStatus).build()).when().get(endpoint).andReturn().asString();

        logger.info(" transaction request with empty start date Response: {}", scenarioScopeState.response);
    }

    @When("I call the get txn API with invalid transactionId expecting status of {int}")
    public void iCallTheGetTxnAPIWithInvalidTransactionIdExpectingStatusOf(int expectedStatus) {
        RequestSpecification requestSpec = Utils.getDefaultSpec(scenarioScopeState.tenant);

        // Set the transactionId value to a random invalid value
        String transactionId = "abcdefgh";

        // Construct the endpoint with the clientCorrelationId parameter
        String endpoint = String.format("%s?transactionId=%s", operationsAppConfig.transactionRequestsEndpoint, transactionId);
        String fullUrl = operationsAppConfig.operationAppContactPoint + endpoint;

        logger.info("Calling endpoint with invalid transactionId: {}", fullUrl);

        scenarioScopeState.response = RestAssured.given(requestSpec).baseUri(operationsAppConfig.operationAppContactPoint).expect()
                .spec(new ResponseSpecBuilder().expectStatusCode(expectedStatus).build()).when().get(endpoint).andReturn().asString();

        logger.info("Get transaction request with invalid transactionId Response: {}", scenarioScopeState.response);

    }

    @When("I call the get txn API with size {int} and page {int} expecting status of {int}")
    public void iCallTheGetTxnAPIWithSizeAndPageExpectingStatusOf(float size, float page, int expectedStatus) {
        RequestSpecification requestSpec = Utils.getDefaultSpec(scenarioScopeState.tenant);
        if (authEnabled) {
            requestSpec.header("Authorization", "Bearer " + scenarioScopeState.accessToken);
        }

        String endpoint = String.format("%s?size=%f&page=%f", operationsAppConfig.transactionRequestsEndpoint, size, page);
        logger.info("Calling Txn request endpoint with size and page: {}", endpoint);

        scenarioScopeState.response = RestAssured.given(requestSpec).baseUri(operationsAppConfig.operationAppContactPoint).expect()
                .spec(new ResponseSpecBuilder().expectStatusCode(expectedStatus).build()).when().get(endpoint).andReturn().asString();

        logger.info("Txn request with size and page Response: {}", scenarioScopeState.response);
    }

    @When("I call the get txn API with currency {string} and amount {int} expecting expected status of {int}")
    public void iCallTheGetTxnAPIWithCurrencyAndAmountExpectingExpectedStatusOf(String currency, float amount, int expectedStatus) {
        RequestSpecification requestSpec = Utils.getDefaultSpec(scenarioScopeState.tenant);
        if (authEnabled) {
            requestSpec.header("Authorization", "Bearer " + scenarioScopeState.accessToken);
        }

        String endpoint = String.format("%s?currency=%s&amount=%f", operationsAppConfig.transactionRequestsEndpoint, currency, amount);
        logger.info("Calling get txn request endpoint: {}", endpoint);

        scenarioScopeState.response = RestAssured.given(requestSpec).baseUri(operationsAppConfig.operationAppContactPoint).expect()
                .spec(new ResponseSpecBuilder().expectStatusCode(expectedStatus).build()).when().get(endpoint).andReturn().asString();

        logger.info("Transaction request with invalid currency and amount Response: {}", scenarioScopeState.response);
    }

    @When("I call the get txn API with invalid startFrom and startTo format expecting status of {int}")
    public void iCallTheGetTxnAPIWithInvalidStartFromAndStartToFormatExpectingStatusOf(int expectedStatus) {
        RequestSpecification requestSpec = Utils.getDefaultSpec(scenarioScopeState.tenant);

        // Set an invalid date format for start from and start to
        String invalidStartDate = "1111-11-1111";
        String invalidEndDate = "2222-22-2222";

        // Construct the endpoint with the invalid start from parameter
        String endpoint = String.format("%s?startfrom=%s&startto=%s", operationsAppConfig.transactionRequestsEndpoint, invalidStartDate,
                invalidEndDate);
        String fullUrl = operationsAppConfig.operationAppContactPoint + endpoint;

        logger.info("Calling endpoint with invalid date format: {}", fullUrl);

        scenarioScopeState.response = RestAssured.given(requestSpec).baseUri(operationsAppConfig.operationAppContactPoint).expect()
                .spec(new ResponseSpecBuilder().expectStatusCode(expectedStatus).build()).when().get(endpoint).andReturn().asString();

        logger.info("Txn request with invalid startFrom and startTo Response: {}", scenarioScopeState.response);
    }

    @When("I call the get txn API with invalid sorting order expecting status of {int}")
    public void iCallTheGetTxnAPIWithInvalidSortingOrderExpectingStatusOf(int expectedStatus) {
        RequestSpecification requestSpec = Utils.getDefaultSpec(scenarioScopeState.tenant);

        String order = "reverse";

        String endpoint = String.format("%s?sortedOrder=%s", operationsAppConfig.transactionRequestsEndpoint, order);
        String fullUrl = operationsAppConfig.operationAppContactPoint + endpoint;

        logger.info("Calling endpoint with invalid sorting order: {}", fullUrl);
        scenarioScopeState.response = RestAssured.given(requestSpec).baseUri(operationsAppConfig.operationAppContactPoint).expect()
                .spec(new ResponseSpecBuilder().expectStatusCode(expectedStatus).build()).when().get(endpoint).andReturn().asString();

        logger.info("Txn request with invalid sorting order Response: {}", scenarioScopeState.response);

    }

    @When("I call the get txn API with invalid sortedBy expecting status of {int}")
    public void iCallTheGetTxnAPIWithInvalidSortedByExpectingStatusOf(int expectedStatus) {
        RequestSpecification requestSpec = Utils.getDefaultSpec(scenarioScopeState.tenant);

        String sortedBy = "jupiter";

        String endpoint = String.format("%s?sortedBy=%s", operationsAppConfig.transactionRequestsEndpoint, sortedBy);
        String fullUrl = operationsAppConfig.operationAppContactPoint + endpoint;
        logger.info("Calling endpoint with invalid sortingBy: {}", fullUrl);

        scenarioScopeState.response = RestAssured.given(requestSpec).baseUri(operationsAppConfig.operationAppContactPoint).expect()
                .spec(new ResponseSpecBuilder().expectStatusCode(expectedStatus).build()).when().get(endpoint).andReturn().asString();

        logger.info("Txn request with invalid sortingBy Response: {}", scenarioScopeState.response);
    }
}
