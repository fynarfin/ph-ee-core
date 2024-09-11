package org.mifos.integrationtest.cucumber.stepdef;

import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.matchingJsonPath;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static com.google.common.truth.Truth.assertThat;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;
import static org.mifos.integrationtest.common.Utils.CONTENT_TYPE;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.json.JSONException;
import org.json.JSONObject;
import org.mifos.connector.common.mobilemoney.airtel.dto.AirtelPaymentRequestDTO;
import org.mifos.connector.common.mobilemoney.airtel.dto.AirtelPaymentRequestSubscriberDTO;
import org.mifos.connector.common.mobilemoney.airtel.dto.AirtelPaymentRequestTransactionDTO;
import org.mifos.connector.common.mobilemoney.airtel.dto.AirtelPaymentResponseDTO;
import org.mifos.integrationtest.common.CollectionHelper;
import org.mifos.integrationtest.common.Utils;
import org.mifos.integrationtest.config.AirtelConfig;
import org.springframework.beans.factory.annotation.Autowired;

public class AirtelStepDef extends BaseStepDef {

    @Autowired
    AirtelConfig airtelConfig;

    @Given("I have MSISDN as {string}")
    public void iHaveMSISDNAs(String msisdn) {
        scenarioScopeState.msisdn = msisdn;
        assertThat(scenarioScopeState.msisdn).isNotEmpty();
    }

    @Given("I have clientCorrelationId as {string}")
    public void iHaveClientCorrelationIdAs(String clientCorrelationId) {
        scenarioScopeState.clientCorrelationId = clientCorrelationId;
        assertThat(scenarioScopeState.clientCorrelationId).isNotEmpty();
    }

    @Given("I have accountId as {string}")
    public void iHaveAccountId(String accountId) {
        scenarioScopeState.accountId = accountId;
        assertThat(scenarioScopeState.accountId).isNotEmpty();
    }

    @And("I have transaction id as {string}")
    public void iHaveTransactionIdAs(String transactionId) {
        scenarioScopeState.transactionId = transactionId;
        assertThat(scenarioScopeState.transactionId).isNotEmpty();
    }

    @And("I have amount as {string}")
    public void iHaveAmountAs(String amount) {
        scenarioScopeState.amount = amount;
        assertThat(scenarioScopeState.amount).isNotEmpty();
    }

    @And("I have currency as {string}")
    public void iHaveCurrencyAs(String currency) {
        scenarioScopeState.currency = currency;
        assertThat(scenarioScopeState.currency).isNotEmpty();
    }

    @And("I have the request body with payer ams identifiers using keys MSISDN and accountId, currency {string}, and amount {string}")
    public void iHaveRequestBody(String currency, String amount) throws JSONException {
        JSONObject collectionRequestBody = CollectionHelper.getCollectionRequestBody(amount, currency, scenarioScopeState.msisdn,
                scenarioScopeState.accountId);
        scenarioScopeState.requestBody = collectionRequestBody;
        logger.info(String.valueOf(scenarioScopeState.requestBody));
    }

    @Then("I should be able to verify that {string} endpoint received {int} request with status code {string}")
    public void iShouldBeAbleToVerifyThatEndpointReceivedRequestWithStatusCode(String endpoint, int numberOfRequest, String statusCode) {
        await().atMost(awaitMost, SECONDS).pollInterval(pollInterval, SECONDS).untilAsserted(() -> {
            verify(numberOfRequest, postRequestedFor(urlEqualTo(endpoint))
                    .withRequestBody(matchingJsonPath("$.transaction.statusCode", equalTo(statusCode))));
        });
    }

    @And("I can mock USSD push payment request body")
    public void iCanMockUSSDPushPaymentRequestBody() {
        // The mock Airtel API does not process or validate the request body.
        // Adding random values for the request body.
        AirtelPaymentRequestDTO airtelPaymentRequestDTO = new AirtelPaymentRequestDTO();
        airtelPaymentRequestDTO.setReference("Testing transaction");

        AirtelPaymentRequestSubscriberDTO airtelPaymentRequestSubscriberDTO = new AirtelPaymentRequestSubscriberDTO();
        airtelPaymentRequestSubscriberDTO.setCountry("KE");
        airtelPaymentRequestSubscriberDTO.setCurrency("KES");
        airtelPaymentRequestSubscriberDTO.setMsisdn(scenarioScopeState.msisdn);
        airtelPaymentRequestDTO.setSubscriber(airtelPaymentRequestSubscriberDTO);

        AirtelPaymentRequestTransactionDTO airtelPaymentRequestTransactionDTO = new AirtelPaymentRequestTransactionDTO();
        airtelPaymentRequestTransactionDTO.setAmount(1000);
        airtelPaymentRequestTransactionDTO.setCountry("KE");
        airtelPaymentRequestTransactionDTO.setCurrency("KES");
        airtelPaymentRequestTransactionDTO.setId(scenarioScopeState.transactionId);
        airtelPaymentRequestDTO.setTransaction(airtelPaymentRequestTransactionDTO);

        scenarioScopeState.mockAirtelUSSDPaymentRequest = airtelPaymentRequestDTO;
        logger.info("mockAirtelUSSDPaymentRequest: {}", scenarioScopeState.mockAirtelUSSDPaymentRequest);
        assertThat(scenarioScopeState.mockAirtelUSSDPaymentRequest).isNotNull();
    }

    @When("I call USSD push payment API with MSISDN with expected status of {int}")
    public void iCallUSSDPushPaymentAPIWithMSISDNWithExpectedStatusOf(int expectedStatus) {
        // The mock Airtel API does not process or validate headers.
        // Adding random values for headers.
        RequestSpecification requestSpec = Utils.getDefaultSpec();
        requestSpec.header(CONTENT_TYPE, "application/json");
        requestSpec.header("X-Country", "KE");
        requestSpec.header("X-Currency", "KES");
        requestSpec.header("Authorization", "Bearer UC23y1292w");
        requestSpec.header("x-signature", "MGsp1Hs683Ag==");
        requestSpec.header("x-key", "DVZCgY91bS==");
        scenarioScopeState.response = RestAssured.given(requestSpec).baseUri(airtelConfig.airtelConnectorContactPoint)
                .body(scenarioScopeState.mockAirtelUSSDPaymentRequest).expect()
                .spec(new ResponseSpecBuilder().expectStatusCode(expectedStatus).build()).when().post(airtelConfig.airtelUssdPushEndpoint)
                .andReturn().asString();

        logger.info("USSD Push Payment Req response: {}", scenarioScopeState.response);
    }

    @And("I should get airtel USSD push payment response body in response")
    public void iShouldGetAirtelUSSDPushPaymentResponseBodyInResponse() throws JSONException {
        try {
            scenarioScopeState.airtelPaymentResponseDTO = objectMapper.readValue(scenarioScopeState.response,
                    AirtelPaymentResponseDTO.class);
        } catch (Exception e) {
            scenarioScopeState.airtelPaymentResponseDTO = null;
        }
    }

    @And("I should get response with data field null")
    public void iShouldGetResponseWithDataFieldNull() {
        assertThat(scenarioScopeState.airtelPaymentResponseDTO.getData()).isNull();
    }

}
