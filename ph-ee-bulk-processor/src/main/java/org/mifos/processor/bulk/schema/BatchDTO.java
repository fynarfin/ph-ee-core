package org.mifos.processor.bulk.schema;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BatchDTO {

    private String batchId;

    private String requestId;

    private Long total;

    private Long ongoing;

    private Long failed;

    private Long successful;

    private BigDecimal totalAmount;

    private BigDecimal successfulAmount;

    private BigDecimal pendingAmount;

    private BigDecimal failedAmount;

    private String file;

    private String notes;

    private String createdAt;

    private String status;

    private String modes;

    private String purpose;

    private String failPercentage;

    private String successPercentage;

    private String registeringInstitutionId;

    private String payerFsp;

    private String correlationId;

}
