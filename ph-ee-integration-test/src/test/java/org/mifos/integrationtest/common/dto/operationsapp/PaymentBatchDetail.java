package org.mifos.integrationtest.common.dto.operationsapp;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentBatchDetail {

    private String batchId;
    private String payerFsp;
    private String reportGeneratedBy;
    private String reportGeneratedAt;
    private Date startedAt;
    private Date completedAt;
    private String registeringInstitutionId;
    private String status;
    private List<SubBatchSummary> subBatchList;
    private List<Instruction> instructionList;
    private Long total;
    private Long ongoing;
    private Long successful;
    private Long failed;
    private BigDecimal totalAmount;
    private BigDecimal pendingAmount;
    private BigDecimal successfulAmount;
    private BigDecimal failedAmount;
    private Long totalInstruction;
    private Long totalBatchAmount;
    private String clientCorrelationId;
}
