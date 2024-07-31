package org.mifos.integrationtest.common;

import org.mifos.connector.common.channel.dto.TransactionChannelRequestDTO;
import org.mifos.connector.common.mojaloop.dto.MoneyData;
import org.mifos.connector.common.mojaloop.dto.Party;
import org.mifos.connector.common.mojaloop.dto.PartyIdInfo;
import org.mifos.connector.common.mojaloop.type.IdentifierType;

public class TransactionHelper {

    public TransactionChannelRequestDTO transactionChannelRequestHelper(Party payer, Party payee, MoneyData amount) {
        TransactionChannelRequestDTO requestDTO = new TransactionChannelRequestDTO();
        requestDTO.setPayer(payer);
        requestDTO.setPayee(payee);
        requestDTO.setAmount(amount);
        return requestDTO;
    }

    public Party partyHelper(IdentifierType partyIdType, String partyIdentifier) {
        PartyIdInfo partyIdInfo = new PartyIdInfo(partyIdType, partyIdentifier);
        Party party = new Party();
        party.setPartyIdInfo(partyIdInfo);
        return party;
    }

    public MoneyData amountHelper(String amount, String currency) {
        MoneyData moneyData = new MoneyData();
        moneyData.setAmount(amount);
        moneyData.setCurrency(currency);
        return moneyData;
    }
}
