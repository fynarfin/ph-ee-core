package hu.dpc.phee.operator.entity.analytics;

import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "event_timestamps")
@Getter
@Setter
public class EventTimestamps {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "WORKFLOW_INSTANCE_KEY")
    private Long workflowInstanceKey;

    @Column(name = "TRANSACTION_ID")
    private String transactionId;

    @Column(name = "EXPORTED_TIME")
    private String exportedTime;

    @Column(name = "IMPORTED_TIME")
    private String importedTime;

    @Column(name = "ZEEBE_TIME")
    private String ZeebeTime;

}
