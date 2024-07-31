package org.mifos.connector.crm.zeebe;

public class ZeebeVariables {

    private ZeebeVariables() {}

    public static final String ACCOUNT = "account";
    public static final String CHANNEL_REQUEST = "channelRequest";
    public static final String ERROR_INFORMATION = "errorInformation";
    public static final String ERROR_DESCRIPTION = "errorDescription";
    public static final String IS_AUTHORISATION_REQUIRED = "isAuthorisationRequired";
    public static final String IS_RTP_REQUEST = "isRtpRequest";
    public static final String ORIGIN_DATE = "originDate";
    public static final String PARTY_ID = "partyId";
    public static final String PARTY_ID_TYPE = "partyIdType";
    public static final String TENANT_ID = "tenantId";
    public static final String PLATFORM_TENANT = "Platform-TenantId";
    public static final String PAYMENTS_NOTIFICATION_RESPONSE = "billPaymentsResponse";
    public static final String BILL_PAYMENTS_REQ = "billPaymentsReq";

    public static final String TRANSACTION_ID = "transactionId";
    public static final String TRANSACTION_TYPE = "transactionType";
    public static final String CLIENTCORRELATIONID = "X-CorrelationID";
    public static final String CALLBACK_URL = "X-CallbackURL";
    public static final String PAYER_FSP = "X-PayerFSP-Id";
    public static final String BILL_ID = "billId";
    public static final String FIELDS = "fields";
    public static final String BILLER_ID = "billerId";
    public static final String BILLER_NAME = "billerName";
    public static final String BILLER_TYPE = "billerType";
    public static final String BILLER_ACCOUNT = "billerAccount";
    public static final String BILLER_FETCH_FAILED = "billerFetchFailed";
    public static final String BILLER_DETAILS = "billerDetails";
    public static final String BILL_INQUIRY_RESPONSE = "billInquiryResponse";
    public static final String BILL_FETCH_FAILED = "billFetchFailed";
    public static final String BILL_PAY_RESPONSE = "billPayResponse";
    public static final String BILL_PAY_FAILED = "billPayFailed";

    public static final String AMOUNT = "amount";

}
