package org.mifos.connector.channel.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransactionType {

    private String scenario;
    private String subScenario;
    private String initiator;
    private String initiatorType;
}
