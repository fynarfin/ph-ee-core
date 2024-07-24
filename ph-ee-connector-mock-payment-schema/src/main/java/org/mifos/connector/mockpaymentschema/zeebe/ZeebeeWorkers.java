package org.mifos.connector.mockpaymentschema.zeebe;

import static org.mifos.connector.mockpaymentschema.zeebe.ZeebeVariables.PARTY_LOOKUP_FAILED;
import static org.mifos.connector.mockpaymentschema.zeebe.ZeebeVariables.TRANSACTION_FAILED;
import static org.mifos.connector.mockpaymentschema.zeebe.ZeebeVariables.TRANSACTION_ID;
import static org.mifos.connector.mockpaymentschema.zeebe.ZeebeVariables.TRANSFER_CODE;
import static org.mifos.connector.mockpaymentschema.zeebe.ZeebeVariables.TRANSFER_CREATE_FAILED;
import static org.mifos.connector.mockpaymentschema.zeebe.ZeebeVariables.TRANSFER_PREPARE_FAILED;
import static org.mifos.connector.mockpaymentschema.zeebe.ZeebeVariables.TRANSFER_RELEASE_FAILED;
import static org.mifos.connector.mockpaymentschema.zeebe.ZeebeVariables.TRANSFER_SETTLEMENT_FAILED;

import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.annotation.PostConstruct;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@SuppressWarnings("FutureReturnValueIgnored")
public class ZeebeeWorkers {

    public static final String WORKER_PARTY_LOOKUP_LOCAL = "party-lookup-local-";
    public static final String WORKER_PAYEE_COMMIT_TRANSFER = "payee-commit-transfer-";
    public static final String WORKER_PAYEE_QUOTE = "payee-quote-";
    public static final String WORKER_PAYER_LOCAL_QUOTE = "payer-local-quote-";
    public static final String WORKER_INTEROP_PARTY_REGISTRATION = "interop-party-registration-";
    public static final String WORKER_PAYEE_DEPOSIT_TRANSFER = "payee-deposit-transfer-";

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired(required = false)
    private ZeebeClient zeebeClient;

    @Value("${ams.local.enabled:false}")
    private boolean isAmsLocalEnabled;

    @Value("#{'${dfspids}'.split(',')}")
    private List<String> dfspids;

    @Value("${zeebe.client.evenly-allocated-max-jobs}")
    private int workerMaxJobs;

    @Value("${mockFailure.percentage}")
    private int mockFailurePercentage;

    @PostConstruct
    public void setupWorkers() {
        zeebeClient.newWorker().jobType("mockPayerBlockFunds").handler((client, job) -> {
            logger.info("Job '{}' started from process '{}' with key {}", job.getType(), job.getBpmnProcessId(), job.getKey());
            logWorkerDetails(job);
            if (isAmsLocalEnabled) {
                Map<String, Object> variables = job.getVariablesAsMap();
                variables = setSuccessOrFailure("block", variables);
                variables.put(TRANSFER_CREATE_FAILED, false);
                variables.put("payeeTenantId", job.getVariablesAsMap().get("payeeTenantId"));
                variables.put(TRANSFER_CODE, "000");
                zeebeClient.newCompleteCommand(job.getKey()).variables(variables).send();
                logger.info("Zeebe variable {}", job.getVariablesAsMap());
            } else {
                Map<String, Object> variables = new HashMap<>();
                variables.put(TRANSFER_PREPARE_FAILED, false);
                zeebeClient.newCompleteCommand(job.getKey()).variables(variables).send();
            }
        }).name("mockPayerBlockFunds").maxJobsActive(workerMaxJobs).open();

        zeebeClient.newWorker().jobType("mockBookFunds").handler((client, job) -> {
            logWorkerDetails(job);
            if (isAmsLocalEnabled) {
                Map<String, Object> variables = new HashMap<>();
                variables = setSuccessOrFailure("book", variables);
                variables.put(TRANSFER_CREATE_FAILED, false);
                variables.put("payeeTenantId", job.getVariablesAsMap().get("payeeTenantId"));
                variables.put(TRANSFER_CODE, "000");
                zeebeClient.newCompleteCommand(job.getKey()).variables(variables).send();
                logger.info("Zeebe variable {}", job.getVariablesAsMap());
            } else {
                Map<String, Object> variables = new HashMap<>();
                variables.put("transferCreateFailed", false);
                zeebeClient.newCompleteCommand(job.getKey()).variables(variables).send();
            }
        }).name("mockBookFunds").maxJobsActive(workerMaxJobs).open();

        zeebeClient.newWorker().jobType("mockReleaseBlock").handler((client, job) -> {
            logWorkerDetails(job);
            if (isAmsLocalEnabled) {
                Map<String, Object> variables = job.getVariablesAsMap();
                variables = setSuccessOrFailure("release", variables);
                variables.put(TRANSFER_CREATE_FAILED, false);
                variables.put("transferReleaseFailed", false);
                variables.put("payeeTenantId", job.getVariablesAsMap().get("payeeTenantId"));
                variables.put(TRANSFER_CODE, "000");
                zeebeClient.newCompleteCommand(job.getKey()).variables(variables).send();

                logger.info("Zeebe variable {}", job.getVariablesAsMap());
            } else {
                Map<String, Object> variables = new HashMap<>();
                variables.put("transferReleaseFailed", false);
                zeebeClient.newCompleteCommand(job.getKey()).variables(variables).send();
            }
        }).name("mockReleaseBlock").maxJobsActive(workerMaxJobs).open();

        zeebeClient.newWorker().jobType("mockPayeeLookup").handler((client, job) -> {
            logger.info("Job '{}' started from process '{}' with key {}", job.getType(), job.getBpmnProcessId(), job.getKey());
            Map<String, Object> variables = job.getVariablesAsMap();
            client.newCompleteCommand(job.getKey()).variables(variables).send().join();
        }).name("mockPayeeLookup").maxJobsActive(workerMaxJobs).open();

        zeebeClient.newWorker().jobType("mockPayeeAccountStatus").handler((client, job) -> {
            logger.info("Job '{}' started from process '{}' with key {}", job.getType(), job.getBpmnProcessId(), job.getKey());
            Map<String, Object> variables = job.getVariablesAsMap();
            variables = setSuccessOrFailure("payeeLookup", variables);
            client.newCompleteCommand(job.getKey()).variables(variables).send().join();
        }).name("mockPayeeAccountStatus").maxJobsActive(workerMaxJobs).open();

        zeebeClient.newWorker().jobType("mockInitiateTransfer").handler((client, job) -> {
            logger.info("Job '{}' started from process '{}' with key {}", job.getType(), job.getBpmnProcessId(), job.getKey());
            Map<String, Object> variables = job.getVariablesAsMap();
            variables = setSuccessOrFailure("transaction", variables);
            variables.put("externalId", UUID.randomUUID());
            // added to pass the transaction request api not found error
            logger.debug("{} {}", variables.get(TRANSACTION_FAILED), variables.get(TRANSACTION_ID));

            zeebeClient.newPublishMessageCommand().messageName("mockTransferResponse")
                    .correlationKey((String) variables.get(TRANSACTION_ID)).timeToLive(Duration.ofMillis(30000)).send().join();
            client.newCompleteCommand(job.getKey()).variables(variables).send().join();
        }).name("mockInitiateTransfer").maxJobsActive(workerMaxJobs).open();

        zeebeClient.newWorker().jobType("getMockStatus").handler((client, job) -> {
            logger.debug("Job '{}' started from process '{}' with key {}", job.getType(), job.getBpmnProcessId(), job.getKey());
            Map<String, Object> variables = job.getVariablesAsMap();
            variables = setSuccessOrFailure("transaction", variables);
            // added to pass the transaction request api not found error
            zeebeClient.newPublishMessageCommand().messageName("mockTransferResponse")
                    .correlationKey(variables.get(TRANSACTION_ID).toString()).timeToLive(Duration.ofMillis(30000)).variables(variables)
                    .send();
            client.newCompleteCommand(job.getKey()).variables(variables).send().join();
        }).name("getMockStatus").maxJobsActive(workerMaxJobs).open();

        zeebeClient.newWorker().jobType("transfer-validation-ams").handler((client, job) -> {
            logger.info("Job '{}' started from process '{}' with key {}", job.getType(), job.getBpmnProcessId(), job.getKey());
            Map<String, Object> variables;
            variables = job.getVariablesAsMap();
            variables.put(PARTY_LOOKUP_FAILED, false);
            zeebeClient.newCompleteCommand(job.getKey()).variables(variables).send().join();
        }).name("transfer-validation-ams").maxJobsActive(workerMaxJobs).open();

        zeebeClient.newWorker().jobType("transfer-clearing-ams").handler((client, job) -> {
            logger.info("Job '{}' started from process '{}' with key {}", job.getType(), job.getBpmnProcessId(), job.getKey());
            Map<String, Object> variables;
            variables = job.getVariablesAsMap();
            variables.put(TRANSFER_SETTLEMENT_FAILED, false);
            zeebeClient.newCompleteCommand(job.getKey()).variables(variables).send().join();
        }).name("transfer-clearing-ams").maxJobsActive(workerMaxJobs).open();

    }

    private Map<String, Object> setSuccessOrFailure(String scenario, Map<String, Object> variables) {
        int successProbability = (int) (Math.random() * 100);
        logger.info("Success probability {}", successProbability);
        switch (scenario) {
            case "block":
                if (successProbability > 0 && successProbability <= mockFailurePercentage) {
                    variables.put(TRANSFER_PREPARE_FAILED, true);
                } else {
                    variables.put(TRANSFER_PREPARE_FAILED, false);
                }
            break;
            case "book":
                if (successProbability > 0 && successProbability <= mockFailurePercentage) {
                    variables.put(TRANSFER_CREATE_FAILED, true);
                } else {
                    variables.put(TRANSFER_CREATE_FAILED, false);
                }
            break;
            case "release":
                if (successProbability > 0 && successProbability <= mockFailurePercentage) {
                    variables.put(TRANSFER_RELEASE_FAILED, true);
                } else {
                    variables.put(TRANSFER_RELEASE_FAILED, false);
                }
            break;
            case "payeeLookup":
                if (successProbability > 0 && successProbability <= mockFailurePercentage) {
                    variables.put(PARTY_LOOKUP_FAILED, true);
                } else {
                    variables.put(PARTY_LOOKUP_FAILED, false);
                }
            break;
            case "transaction":
                if (successProbability > 0 && successProbability <= mockFailurePercentage) {
                    variables.put(TRANSACTION_FAILED, true);
                } else {
                    variables.put(TRANSACTION_FAILED, false);
                }
            break;

        }
        return variables;
    }

    private void logWorkerDetails(ActivatedJob job) {
        JSONObject jsonJob = new JSONObject();
        jsonJob.put("bpmnProcessId", job.getBpmnProcessId());
        jsonJob.put("elementInstanceKey", job.getElementInstanceKey());
        jsonJob.put("jobKey", job.getKey());
        jsonJob.put("jobType", job.getType());
        jsonJob.put("workflowElementId", job.getElementId());
        jsonJob.put("workflowDefinitionVersion", job.getProcessDefinitionVersion());
        jsonJob.put("workflowKey", job.getProcessDefinitionKey());
        jsonJob.put("workflowInstanceKey", job.getProcessInstanceKey());
        logger.info("Job started: {}", jsonJob.toString(4));
    }

}
