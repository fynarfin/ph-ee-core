package org.mifos.connector.mockpaymentschema.zeebe;

import java.util.HashMap;
import java.util.Map;
import org.mifos.connector.common.ams.dto.TransferActionType;

public final class ZeebeVariables {

    public static final Map<String, String> ACTION_FAILURE_MAP = new HashMap<>();

    public static final String ACCOUNT = "account";
    public static final String ACCOUNT_CURRENCY = "accountCurrency";
    public static final String ACCOUNT_ID = "accountId";
    public static final String CHANNEL_REQUEST = "channelRequest";
    public static final String ERROR_INFORMATION = "errorInformation";
    public static final String EXTERNAL_ACCOUNT_ID = "externalAccountId";
    public static final String INTEROP_REGISTRATION_FAILED = "interopRegistrationFailed";
    public static final String LOCAL_QUOTE_FAILED = "localQuoteFailed";
    public static final String LOCAL_QUOTE_RESPONSE = "localQuoteResponse";
    public static final String PARTY_ID = "partyId";
    public static final String PARTY_ID_TYPE = "partyIdType";
    public static final String PAYEE_PARTY_RESPONSE = "payeePartyResponse";
    public static final String QUOTE_FAILED = "quoteFailed";
    public static final String QUOTE_SWITCH_REQUEST = "quoteSwitchRequest";
    public static final String QUOTE_SWITCH_REQUEST_AMOUNT = "quoteSwitchRequestAmount";
    public static final String TENANT_ID = "tenantId";
    public static final String BOOK_TRANSACTION_ID = "bookTransactionId";
    public static final String TRANSACTION_ID = "transactionId";
    public static final String TRANSFER_CODE = "transferCode";
    public static final String TRANSFER_CREATE_FAILED = "transferCreateFailed";
    public static final String TRANSFER_PREPARE_FAILED = "transferPrepareFailed";
    public static final String TRANSFER_RELEASE_FAILED = "transferReleaseFailed";
    public static final String TRANSFER_RESPONSE_PREFIX = "transferResponse";

    public static final String PARTY_LOOKUP_FAILED = "partyLookupFailed";

    public static final String TRANSFER_SETTLEMENT_FAILED = "transferSettlementFailed";
    public static final String PAYEE_LOOKUP_RETRY_COUNT = "payeeAccountStatusRetry";
    public static final String TRANSFER_RETRY_COUNT = "paymentTransferRetry";
    public static final String TRANSACTION_FAILED = "transactionFailed";
    public static final String TRANSFER_STATE = "transferState";
    public static final String LINK_CREATE_FAILED = "linkCreationFailed";
    public static final String LINK_CREATION_COUNT = "linkCreationRetryCount";
    public static final String NUMBER_OF_PAYMENTS = "numberOfPayments";
    public static final String BILLS_STATUS_ERROR = "billStatusError";
    public static final String QUOTE_RETRY_COUNT = "quoteRetryCount";

    static {
        ACTION_FAILURE_MAP.put(TransferActionType.PREPARE.name(), TRANSFER_PREPARE_FAILED);
        ACTION_FAILURE_MAP.put(TransferActionType.CREATE.name(), TRANSFER_CREATE_FAILED);
        ACTION_FAILURE_MAP.put(TransferActionType.RELEASE.name(), TRANSFER_RELEASE_FAILED);
    }

    private ZeebeVariables() {}
}
