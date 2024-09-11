package org.mifos.integrationtest.cucumber.stepdef;

import static com.github.tomakehurst.wiremock.client.WireMock.getAllServeEvents;
import static com.google.common.truth.Truth.assertThat;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;

import com.github.tomakehurst.wiremock.stubbing.ServeEvent;
import io.cucumber.core.internal.com.fasterxml.jackson.databind.JsonNode;
import io.cucumber.core.internal.com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.RequestSpecification;
import java.io.IOException;
import java.util.List;
import org.mifos.connector.common.channel.dto.TransactionChannelRequestDTO;
import org.mifos.connector.common.mojaloop.dto.MoneyData;
import org.mifos.connector.common.mojaloop.dto.Party;
import org.mifos.connector.common.mojaloop.type.IdentifierType;
import org.mifos.integrationtest.common.TransactionHelper;
import org.mifos.integrationtest.common.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

public class MtnStepDef extends BaseStepDef {

    @Autowired
    MockServerStepDef mockServerStepDef;

    @Autowired
    ScenarioScopeState scenarioScopeState;

    Logger logger = LoggerFactory.getLogger(MtnStepDef.class);

    @Value("${tenantconfig.tenants.paymentbb1}")
    private String tenant;

    private String tenantHeader = "Platform-TenantId";

    @Given("I can create a TransactionChannelRequestDTO for MTN Transaction Request with PayerId {string}")
    public void iCreateATransactionChannelRequestDTOForMtn(String payerId) {
        TransactionHelper transactionHelper = new TransactionHelper();
        Party payer = transactionHelper.partyHelper(IdentifierType.MSISDN, payerId);
        Party payee = transactionHelper.partyHelper(IdentifierType.MSISDN, "27710101999");
        MoneyData amount = transactionHelper.amountHelper("100", "SNR");
        TransactionChannelRequestDTO requestDTO = transactionHelper.transactionChannelRequestHelper(payer, payee, amount);
        requestDTO.setClientRefId("123");
        ObjectMapper objectMapper = new ObjectMapper();

        scenarioScopeState.payerIdentifier = payerId;
        try {
            scenarioScopeState.createTransactionChannelRequestBody = objectMapper.writeValueAsString(requestDTO);
        } catch (Exception e) {
            logger.error("An Exception occurred", e);
        }
    }

    @When("I call the transaction request API for MTN with expected status of {int}")
    public void iCallTheTransactionRequestAPIForMtnWithExpectedStatusOf(int expectedStatus) {
        RequestSpecification requestSpec = Utils.getDefaultSpec();

        scenarioScopeState.tenant = tenant;
        scenarioScopeState.response = RestAssured.given(requestSpec).header("Content-Type", "application/json")
                .header(tenantHeader, scenarioScopeState.tenant).header("X-CallbackURL", identityMapperConfig.callbackURL)
                .baseUri(channelConnectorConfig.channelConnectorContactPoint).body(scenarioScopeState.createTransactionChannelRequestBody)
                .expect().spec(new ResponseSpecBuilder().expectStatusCode(expectedStatus).build()).when()
                .post(channelConnectorConfig.transferReqEndpoint).andReturn().asString();

        logger.info("Transaction Request Response: {}", scenarioScopeState.response);
    }

    @When("I call the transaction request API for MTN with expected status of {int} and {string}")
    public void iCallTheTransactionRequestAPIForMtnWithExpectedStatusOf(int expectedStatus, String stub) {
        RequestSpecification requestSpec = Utils.getDefaultSpec();

        scenarioScopeState.tenant = tenant;
        scenarioScopeState.response = RestAssured.given(requestSpec).header("Content-Type", "application/json")
                .header(tenantHeader, scenarioScopeState.tenant).header("X-CallbackURL", identityMapperConfig.callbackURL + stub)
                .baseUri(channelConnectorConfig.channelConnectorContactPoint).body(scenarioScopeState.createTransactionChannelRequestBody)
                .expect().spec(new ResponseSpecBuilder().expectStatusCode(expectedStatus).build()).when()
                .post(channelConnectorConfig.transferReqEndpoint).andReturn().asString();

        logger.info("Transaction Request Response: {}", scenarioScopeState.response);
    }

    @Then("I should be able to extract response body from callback for mtn")
    public void iShouldBeAbleToExtractResponseBodyFromCallbackForMtn() {
        await().atMost(awaitMost, SECONDS).pollDelay(pollDelay, SECONDS).pollInterval(pollInterval, SECONDS).untilAsserted(() -> {
            boolean flag = false;
            JsonNode rootNode = null;

            List<ServeEvent> allServeEvents = getAllServeEvents();
            for (int i = allServeEvents.size() - 1; i >= 0; i--) {
                ServeEvent request = allServeEvents.get(i);
                if (!(request.getRequest().getBodyAsString()).isEmpty()) {
                    flag = true;
                    try {
                        rootNode = objectMapper.readTree(request.getRequest().getBody());
                        logger.info("Rootnode value:" + rootNode);
                        assertThat(rootNode).isNotNull();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                }
                assertThat(flag).isTrue();

                if (rootNode.has("financialTransactionId")) {
                    scenarioScopeState.callbackBody = request.getRequest().getBodyAsString();
                    assertThat(scenarioScopeState.callbackBody.contains(scenarioScopeState.payerIdentifier));
                }
            }
        });
    }

    @And("I should have {string} and {string} in mtn callback response")
    public void iShouldHaveAndInResponse(String status, String statusValue) {
        assertThat(scenarioScopeState.callbackBody).contains(status);
        assertThat(scenarioScopeState.callbackBody).contains(statusValue);

    }

    @Then("I set the header as invalid for mtn")
    public void setTenantAsNull() {
        scenarioScopeState.tenant = null;
    }

}
