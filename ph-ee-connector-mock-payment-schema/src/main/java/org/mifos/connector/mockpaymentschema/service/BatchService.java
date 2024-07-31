package org.mifos.connector.mockpaymentschema.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import org.mifos.connector.mockpaymentschema.schema.AuthorizationRequest;
import org.mifos.connector.mockpaymentschema.schema.AuthorizationResponse;
import org.mifos.connector.mockpaymentschema.schema.BatchDTO;
import org.mifos.connector.mockpaymentschema.schema.BatchDetailResponse;
import org.mifos.connector.mockpaymentschema.schema.Transfer;
import org.mifos.connector.mockpaymentschema.schema.TransferStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class BatchService {

    @Value("${threshold.amount}")
    private String thresholdAmount;

    @Autowired
    private org.mifos.connector.mockpaymentschema.service.SendCallbackService sendCallbackService;

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    private String payerPartyId = "835322416";

    private String payeePartyId = "27713803912";

    private int successTxnCount = 9;

    @Async("asyncExecutor")
    public void getAuthorization(String batchId, String clientCorrelationId, AuthorizationRequest authRequest, String callbackUrl) {
        AuthorizationResponse response = new AuthorizationResponse();

        if (authRequest.getAmount().compareTo(BigDecimal.valueOf(Long.valueOf(thresholdAmount))) >= 0) {
            response.setStatus("N");
            response.setClientCorrelationId(clientCorrelationId);
            response.setReason("Error getting authorization for the request");
        } else {
            response.setClientCorrelationId(clientCorrelationId);
            response.setStatus("Y");
        }
        try {
            logger.info("Sending callback: {}", callbackUrl);
            sendCallbackService.sendCallback(new ObjectMapper().writeValueAsString(response), callbackUrl);
        } catch (JsonProcessingException e) {
            logger.error(e.toString());
        }
    }

    public BatchDTO getBatchSummary(String batchId) {
        return successfulBatchSummaryResponse(batchId);
    }

    public BatchDetailResponse getBatchDetails(String batchId, int pageNo, int pageSize) {
        List<Transfer> transactions = getTransactions(batchId);
        int toIndex = pageNo * pageSize;
        int fromIndex = toIndex - pageSize;
        toIndex = Math.min(toIndex, transactions.size());
        BatchDetailResponse batchDetailResponse = new BatchDetailResponse();
        batchDetailResponse.setContent(transactions.subList(fromIndex, toIndex));
        return batchDetailResponse;
    }

    private BatchDTO successfulBatchSummaryResponse(String batchId) {
        Long total = 10L;
        Long ongoing = 1L;
        Long failed = 1L;
        Long successful = 8L;
        BigDecimal totalAmount = BigDecimal.valueOf(100);
        BigDecimal ongoingAmount = BigDecimal.valueOf(0);
        BigDecimal failedAmount = BigDecimal.valueOf(10);
        BigDecimal successfulAmount = BigDecimal.valueOf(90);
        String status = "Pending";
        String successPercentage = "90";
        String failedPercentage = "10";

        return new BatchDTO(batchId, null, total, ongoing, failed, successful, totalAmount, successfulAmount, ongoingAmount, failedAmount,
                null, null, null, status, null, null, failedPercentage, successPercentage);
    }

    private List<Transfer> getTransactions(String batchId) {
        List<Transfer> transactionList = new ArrayList<>();

        for (int index = 0; index < 10; index++) {
            Transfer transfer;

            if (successTxnCount > 0) {
                transfer = getSingleTransaction(index, ThreadLocalRandom.current().nextLong(), UUID.randomUUID().toString(),
                        TransferStatus.COMPLETED, batchId);
            } else {
                transfer = getSingleTransaction(index, ThreadLocalRandom.current().nextLong(), UUID.randomUUID().toString(),
                        TransferStatus.IN_PROGRESS, batchId);
            }
            transactionList.add(transfer);
        }
        return transactionList;
    }

    private Transfer getSingleTransaction(int index, Long workflowInstanceKey, String requestId, TransferStatus status, String batchId) {
        String id = String.valueOf(index);
        Date startedAt = new Date(1685536200000L);
        Date completedAt = new Date(1685536268000L);
        String payerPartyIdType = "MSISDN";
        String payeePartyIdType = "MSISDN";
        BigDecimal amount = BigDecimal.valueOf(10);
        String currency = "USD";
        String direction = "OUTGOING";

        return new Transfer(id, workflowInstanceKey, requestId, startedAt, completedAt, status, null, null, payeePartyId, payeePartyIdType,
                null, null, null, null, payerPartyId, payerPartyIdType, null, null, null, amount, currency, direction, null, batchId, null);
    }

}
