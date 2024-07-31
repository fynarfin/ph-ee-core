package org.mifos.integrationtest.common.dto.operationsapp;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BatchAndSubBatchSummaryResponse {

    private String batchId;

    private String requestId;

    private Date startedAt;

    private Date completedAt;

    private String registeringInstitutionId;

    private Long total;

    private Long ongoing;

    private Long successful;

    private Long failed;

    private BigDecimal totalAmount;

    private BigDecimal pendingAmount;

    private BigDecimal successfulAmount;

    private BigDecimal failedAmount;

    private String file;

    private String notes;

    private String createdAt;

    private String status;

    private String modes;

    private String purpose;

    private String failedPercentage;

    private String successPercentage;

    private String payerFsp;
    private Set<String> payeeFsp;

    private String generatedBy;

    private String generatedAt;

    private Long totalSubBatches;

    private Long approvedTransactionCount;

    private Long approvedAmount;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<SubBatchSummary> subBatchSummaryList;
    private Long totalInstructionCount;
}
