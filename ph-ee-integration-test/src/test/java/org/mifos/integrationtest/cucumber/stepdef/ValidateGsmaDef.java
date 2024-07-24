package org.mifos.integrationtest.cucumber.stepdef;

import io.cucumber.core.internal.com.fasterxml.jackson.core.JsonProcessingException;
import io.cucumber.core.internal.com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.RequestSpecification;
import java.util.ArrayList;
import java.util.List;
import org.mifos.connector.common.gsma.dto.CustomData;
import org.mifos.connector.common.gsma.dto.GsmaTransfer;
import org.mifos.connector.common.gsma.dto.Party;
import org.mifos.integrationtest.common.GsmaTransactionHelper;
import org.mifos.integrationtest.common.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

public class ValidateGsmaDef extends BaseStepDef {

    @Autowired
    ScenarioScopeState scenarioScopeState;

    Logger logger = LoggerFactory.getLogger(VoucherManagementStepDef.class);

    @Value("${callback_url}")
    private String callbackUrl;

    @Value("${amsName}")
    private String amsName;

    @Value("${tenantconfig.tenants.payerfsp}")
    private String accountHoldingInstitutionId;

    private String accountHoldingInstitutionIdHeader = "accountHoldingInstitutionId";

    private String amsNameHeader = "amsName";

    private String callbackUrlHeader = "X-CallbackURL";

    @Given("I can create a GsmaTransfer DTO with no payee list")
    public void iCanCreateANegativeGsmaTransferDTO() {
        GsmaTransactionHelper gsmaTransactionHelper = new GsmaTransactionHelper();
        List<CustomData> customDataList = new ArrayList<>();
        customDataList = gsmaTransactionHelper.customDataListHelper(customDataList, "string", "string");
        List<Party> payerList = gsmaTransactionHelper.partyListHelper("MSISDN", "+44999911");
        GsmaTransfer gsmaTransferDTO = gsmaTransactionHelper.gsmaTransferHelper("string", "inbound", "transfer", "100", "SNR", "string",
                "2022-09-28T12:51:19.260+00:00", customDataList, payerList, null);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            scenarioScopeState.createGsmaTransferRequestBody = objectMapper.writeValueAsString(gsmaTransferDTO);
        } catch (JsonProcessingException e) {
            logger.error("Unable to convert the DTO : {}", e);
        }
    }

    @Given("I can create a GsmaTransfer DTO")
    public void iCanCreateAGsmaTransferDTO() {
        GsmaTransactionHelper gsmaTransactionHelper = new GsmaTransactionHelper();
        List<CustomData> customDataList = new ArrayList<>();
        customDataList = gsmaTransactionHelper.customDataListHelper(customDataList, "string", "string");
        List<Party> payerList = gsmaTransactionHelper.partyListHelper("MSISDN", "+44999911");
        List<Party> payeeList = gsmaTransactionHelper.partyListHelper("MSISDN", "+44999911");
        GsmaTransfer gsmaTransferDTO = gsmaTransactionHelper.gsmaTransferHelper("string", "inbound", "transfer", "100", "SNR", "string",
                "2022-09-28T12:51:19.260+00:00", customDataList, payerList, payeeList);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            scenarioScopeState.createGsmaTransferRequestBody = objectMapper.writeValueAsString(gsmaTransferDTO);
        } catch (JsonProcessingException e) {
            logger.error("Unable to convert the DTO : {}", e);
        }
    }

    @When("I call the GSMA transaction API with expected status of {int}")
    public void iCallTheGsmaTransactionAPIWithExpectedStatusOf(int expectedStatus) {
        RequestSpecification requestSpec = Utils.getDefaultSpec();
        scenarioScopeState.callbackUrl = callbackUrl;
        scenarioScopeState.amsName = amsName;
        scenarioScopeState.accountHoldingInstitutionId = accountHoldingInstitutionId;
        scenarioScopeState.response = RestAssured.given(requestSpec).header("Content-Type", "application/json")
                .header(accountHoldingInstitutionIdHeader, scenarioScopeState.accountHoldingInstitutionId)
                .header(amsNameHeader, scenarioScopeState.amsName).header(callbackUrlHeader, scenarioScopeState.callbackUrl)
                .baseUri(channelConnectorConfig.channelConnectorContactPoint).body(scenarioScopeState.createGsmaTransferRequestBody)
                .expect().spec(new ResponseSpecBuilder().expectStatusCode(expectedStatus).build()).when()
                .post(channelConnectorConfig.getGsmaTransactionEndpoint()).andReturn().asString();

        logger.info("Gsma transaction Response: {}", scenarioScopeState.response);
    }

    @When("I call the gsma transaction API having unsupported header with expected status of {int}")
    public void iCallTheGsmaTransactionAPIHavingUnsupportedHeaderWithExpectedStatusOf(int expectedStatus) {
        RequestSpecification requestSpec = Utils.getDefaultSpec();
        scenarioScopeState.callbackUrl = callbackUrl;
        scenarioScopeState.amsName = amsName;
        scenarioScopeState.accountHoldingInstitutionId = accountHoldingInstitutionId;
        scenarioScopeState.response = RestAssured.given(requestSpec).header("Content-Type", "application/json")
                .header(accountHoldingInstitutionIdHeader, scenarioScopeState.accountHoldingInstitutionId)
                .header(amsNameHeader, scenarioScopeState.amsName).header(callbackUrlHeader, scenarioScopeState.callbackUrl)
                .header("invalid-header", "test").baseUri(channelConnectorConfig.channelConnectorContactPoint)
                .body(scenarioScopeState.createGsmaTransferRequestBody).expect()
                .spec(new ResponseSpecBuilder().expectStatusCode(expectedStatus).build()).when()
                .post(channelConnectorConfig.getGsmaTransactionEndpoint()).andReturn().asString();

        logger.info("Transaction Request Response: {}", scenarioScopeState.response);
    }

    @When("I call the gsma transaction API without required header accountHoldingInstitutionId with expected status of {int}")
    public void iCallTheGsmaTransactionAPINotHavingRequiredHeaderAccountHoldingInstitutionIdWithExpectedStatusOf(int expectedStatus) {
        RequestSpecification requestSpec = Utils.getDefaultSpec();
        scenarioScopeState.callbackUrl = callbackUrl;
        scenarioScopeState.amsName = amsName;
        scenarioScopeState.response = RestAssured.given(requestSpec).header("Content-Type", "application/json")
                .header(amsNameHeader, scenarioScopeState.amsName).header(callbackUrlHeader, scenarioScopeState.callbackUrl)
                .baseUri(channelConnectorConfig.channelConnectorContactPoint).body(scenarioScopeState.createGsmaTransferRequestBody)
                .expect().spec(new ResponseSpecBuilder().expectStatusCode(expectedStatus).build()).when()
                .post(channelConnectorConfig.getGsmaTransactionEndpoint()).andReturn().asString();

        logger.info("GSMA Transaction Response: {}", scenarioScopeState.response);
    }

}
