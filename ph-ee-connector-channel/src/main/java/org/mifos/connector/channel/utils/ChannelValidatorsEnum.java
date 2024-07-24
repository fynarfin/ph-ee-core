package org.mifos.connector.channel.utils;

import org.mifos.connector.common.exception.PaymentHubErrorCategory;
import org.mifos.connector.common.validation.ValidationCodeType;

public enum ChannelValidatorsEnum implements ValidationCodeType {

    INVALID_PLATFORM_TENANT_ID_LENGTH("error.msg.schema.platform.tenant.id.length.is.invalid",
            "Platform Tenant Id length is invalid"), INVALID_PLATFORM_TENANT_ID(
                    "error.msg.schema.platform.tenant.id.cannot.be.null.or.empty", "Platform Tenant Id cannot be null or empty"),

    INVALID_AMS_NAME("error.msg.schema.ams.name.cannot.be.null.or.empty", "Ams name cannot be null or empty"), INVALID_AMS_NAME_LENGTH(
            "error.msg.schema.ams.name.length.is.invalid", "Ams name length is invalid"),

    INVALID_ACCOUNT_HOLDING_INSTITUTION_ID("error.msg.schema.account.holding.institution.id.cannot.be.null.or.empty",
            "Account holding institution id cannot be null or empty"), INVALID_ACCOUNT_HOLDING_INSTITUTION_ID_LENGTH(
                    "error.msg.schema.account.holding.institution.id.length.is.invalid",
                    "Account holding institution id length is invalid"),

    INVALID_X_CALLBACK_URL("error.msg.schema.x.callback.url.cannot.be.null.or.empty",
            "X callback url cannot be null or empty"), INVALID_X_CALLBACK_URL_LENGTH("error.msg.schema.x.callback.url.length.is.invalid",
                    "X callback url length is invalid"),

    INVALID_CLIENT_CORRELATION_ID_LENGTH("error.msg.schema.client.correlation.id.length.is.invalid",
            "Client correlation id length is invalid"),

    TRANSFER_SCHEMA_VALIDATION_ERROR("error.msg.schema.validation.errors", "The request is invalid"),

    INVALID_PAYER("error.msg.schema.payer.cannot.be.null.or.empty", "Payer cannot be null or empty"), INVALID_PAYEE(
            "error.msg.schema.payee.cannot.be.null.or.empty", "Payee cannot be null or empty"),

    INVALID_PAYER_PARTY_ID_INFO("error.msg.schema.payer.party.id.info.cannot.be.null.or.empty",
            "Payer party Id Info cannot be null or empty"), INVALID_PAYEE_PARTY_ID_INFO(
                    "error.msg.schema.payee.party.id.info.cannot.be.null.or.empty",
                    "Payee party Id Info cannot be null or empty"), INVALID_PAYER_PARTY_IDENTIFIER(
                            "error.msg.schema.payer.party.identifier.cannot.be.null.or.empty",
                            "Payer party identifier cannot be null or empty"), INVALID_PAYEE_PARTY_IDENTIFIER(
                                    "error.msg.schema.payee.party.identifier.cannot.be.null.or.empty",
                                    "Payee party identifier cannot be null or empty"), INVALID_PAYER_PARTY_ID_TYPE(
                                            "error.msg.schema.payer.party.id.type.cannot.be.null.or.empty",
                                            "Payer party Id Type cannot be null or empty"), INVALID_PAYEE_PARTY_ID_TYPE(
                                                    "error.msg.schema.payee.party.id.type.cannot.be.null.or.empty",
                                                    "Payee party Id Type cannot be null or empty"), INVALID_PAYER_PARTY_ID_IDENTIFIER(
                                                            "error.msg.schema.payer.party.id.identifier.cannot.be.null.or.empty",
                                                            "Payer party id identifier cannot be null or empty"), INVALID_PAYEE_PARTY_ID_IDENTIFIER(
                                                                    "error.msg.schema.payee.party.id.identifier.cannot.be.null.or.empty",
                                                                    "Payee party id identifier cannot be null or empty"),

    INVALID_AMOUNT("error.msg.schema.amount.cannot.be.null.or.empty", "Amount cannot be null or empty"), INVALID_AMOUNT_AMOUNT(
            "error.msg.schema.amount.amount.cannot.be.null.or.empty",
            "Amount amount cannot be null or empty"), INVALID_NEGATIVE_AMOUNT("error.msg.schema.amount.cannot.be.negative",
                    "Amount cannot be negative"), INVALID_CURRENCY("error.msg.schema.currency.cannot.be.null.or.empty",
                            "Currency cannot be null or empty"), INVALID_CURRENCY_LENGTH("error.msg.schema.currency.length.is.invalid",
                                    "Currency length is invalid"),

    INVALID_REQUEST_ORGANIZATION_TRANSACTION_REFERENCE(
            "error.msg.schema.request.organization_transaction.reference.cannot.be.null.or.empty",
            "Request organization transaction reference cannot be null or empty"), INVALID_SUB_TYPE(
                    "error.msg.schema.sub.type.cannot.be.null.or.empty", "Sub type cannot be null or empty"), INVALID_TYPE(
                            "error.msg.schema.type.cannot.be.null.or.empty", "Type cannot be null or empty"), INVALID_DESCRIPTION_TEXT(
                                    "error.msg.schema.description.text.cannot.be.null.or.empty",
                                    "Description text cannot be null or empty"), INVALID_REQUEST_DATE(
                                            "error.msg.schema.request.date.cannot.be.null.or.empty",
                                            "Request date cannot be null or empty"),

    HEADER_VALIDATION_ERROR("error.msg.header.validation.errors", "The headers are invalid");

    private final String code;
    private final String category;
    private final String message;

    ChannelValidatorsEnum(String code, String message) {
        this.code = code;
        this.category = PaymentHubErrorCategory.Validation.toString();
        this.message = message;
    }

    public String getCode() {
        return this.code;
    }

    public String getCategory() {
        return this.category;
    }

    public String getMessage() {
        return message;
    }
}
