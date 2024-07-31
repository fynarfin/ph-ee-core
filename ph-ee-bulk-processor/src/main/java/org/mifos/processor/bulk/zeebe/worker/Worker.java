package org.mifos.processor.bulk.zeebe.worker;

public enum Worker {

    PARTY_LOOKUP("partyLookup"), APPROVAL("approval"), ORDERING("ordering"), SPLITTING("splitting"), FORMATTING("formatting"), BATCH_STATUS(
            "batchStatus"), SEND_CALLBACK("sendCallback"), MERGE_BACK("mergeSubBatch"), INIT_SUB_BATCH("initSubBatch"), ACCOUNT_LOOKUP(
                    "accountLookup"), ACCOUNT_LOOKUP_CALLBACK("accountLookupCallback"), BATCH_AGGREGATE("batchAggregate"), AUTHORIZATION(
                            "authorization"), DE_DEPLICATION("deduplicate"), BATCH_ACCOUNT_LOOKUP(
                                    "batchAccountLookup"), BATCH_ACCOUNT_LOOKUP_CALLBACK("batchAccountLookupCallback");

    private final String value;

    Worker(String s) {
        value = s;
    }

    public String getValue() {
        return value;
    }

}
