package org.mifos.integrationtest.common;

import java.util.ArrayList;
import java.util.List;
import org.mifos.connector.common.gsma.dto.CustomData;
import org.mifos.connector.common.gsma.dto.GsmaTransfer;
import org.mifos.connector.common.gsma.dto.Party;

public class GsmaTransactionHelper {

    public GsmaTransfer gsmaTransferHelper(String requestingOrganisationTransactionReference, String subType, String type, String amount,
            String currency, String descriptionText, String requestDate, List<CustomData> customData, List<Party> payer,
            List<Party> payee) {
        GsmaTransfer requestDTO = new GsmaTransfer(requestingOrganisationTransactionReference, subType, type, amount, currency,
                descriptionText, requestDate, customData, payer, payee);
        return requestDTO;
    }

    public List<CustomData> customDataListHelper(List<CustomData> customDataList, String key, String value) {
        CustomData customData = new CustomData();
        customData.setKey(key);
        customData.setValue(value);
        customDataList.add(customData);
        return customDataList;
    }

    public List<Party> partyListHelper(String partyIdType, String partyIdIdentifier) {
        List<Party> partyList = new ArrayList<>();
        Party party = new Party();
        party.setPartyIdType(partyIdType);
        party.setPartyIdIdentifier(partyIdIdentifier);
        partyList.add(party);
        return partyList;
    }
}
