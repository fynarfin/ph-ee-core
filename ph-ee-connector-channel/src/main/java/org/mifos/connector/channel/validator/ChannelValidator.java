package org.mifos.connector.channel.validator;

import static org.mifos.connector.common.exception.PaymentHubError.ExtValidationError;

import org.mifos.connector.channel.utils.ChannelValidatorsEnum;
import org.mifos.connector.common.channel.dto.PhErrorDTO;
import org.mifos.connector.common.channel.dto.TransactionChannelRequestDTO;
import org.mifos.connector.common.exception.PaymentHubErrorCategory;
import org.mifos.connector.common.exception.ValidationException;
import org.mifos.connector.common.gsma.dto.GsmaTransfer;
import org.mifos.connector.common.validation.ValidatorBuilder;

@SuppressWarnings("HideUtilityClassConstructor")
public class ChannelValidator {

    private static final String resource = "ChannelValidator";
    private static final String payer = "payer";
    private static final String partyIdInfo = "partyIdInfo";
    private static final String partyIdType = "partyIdType";
    private static final String partyIdentifier = "partyIdentifier";
    private static final String partyIdIdentifier = "partyIdIdentifier";
    private static final String payee = "payee";
    private static final String amount = "amount";
    private static final String amount_value = "amount";
    private static final String currency = "currency";
    private static final int expectedCurrencyLength = 3;
    private static final String requestingOrganisationTransactionReference = "requestingOrganisationTransactionReference";
    private static final String subType = "subType";
    private static final String type = "type";
    private static final String descriptionText = "descriptionText";
    private static final String requestDate = "requestDate";
    private static final String customData = "customData";
    private static final String key = "key";
    private static final String value = "value";

    public static void validateTransfer(TransactionChannelRequestDTO request) throws ValidationException {
        final ValidatorBuilder validatorBuilder = new ValidatorBuilder();

        // validating payer
        validatorBuilder.reset().resource(resource).parameter(payer).value(request.getPayer())
                .isNullWithFailureCode(ChannelValidatorsEnum.INVALID_PAYER);

        // validating payee
        validatorBuilder.reset().resource(resource).parameter(payee).value(request.getPayee())
                .isNullWithFailureCode(ChannelValidatorsEnum.INVALID_PAYEE);

        if (request.getPayer() != null) {
            // validating payer party id info
            validatorBuilder.reset().resource(resource).parameter(partyIdInfo).value(request.getPayer().getPartyIdInfo())
                    .isNullWithFailureCode(ChannelValidatorsEnum.INVALID_PAYER_PARTY_ID_INFO);

            if (request.getPayer().getPartyIdInfo() != null) {
                // validating payer party id type
                validatorBuilder.reset().resource(resource).parameter(partyIdType)
                        .value(request.getPayer().getPartyIdInfo().getPartyIdType())
                        .isNullWithFailureCode(ChannelValidatorsEnum.INVALID_PAYER_PARTY_ID_TYPE);

                // validating payer party identifier
                validatorBuilder.reset().resource(resource).parameter(partyIdentifier)
                        .value(request.getPayer().getPartyIdInfo().getPartyIdentifier())
                        .isNullWithFailureCode(ChannelValidatorsEnum.INVALID_PAYER_PARTY_IDENTIFIER);
            }
        }

        if (request.getPayee() != null) {
            // validating payee party id info
            validatorBuilder.reset().resource(resource).parameter(partyIdInfo).value(request.getPayee().getPartyIdInfo())
                    .isNullWithFailureCode(ChannelValidatorsEnum.INVALID_PAYEE_PARTY_ID_INFO);

            if (request.getPayee().getPartyIdInfo() != null) {
                // validating payee party id type
                validatorBuilder.reset().resource(resource).parameter(partyIdType)
                        .value(request.getPayee().getPartyIdInfo().getPartyIdType())
                        .isNullWithFailureCode(ChannelValidatorsEnum.INVALID_PAYEE_PARTY_ID_TYPE);

                // validating payee party identifier
                validatorBuilder.reset().resource(resource).parameter(partyIdentifier)
                        .value(request.getPayee().getPartyIdInfo().getPartyIdentifier())
                        .isNullWithFailureCode(ChannelValidatorsEnum.INVALID_PAYEE_PARTY_IDENTIFIER);
            }
        }

        // validating amount
        validatorBuilder.reset().resource(resource).parameter(amount).value(request.getAmount())
                .isNullWithFailureCode(ChannelValidatorsEnum.INVALID_AMOUNT);

        if (request.getAmount() != null) {
            // validating the amount field inside the amount field.
            validatorBuilder.reset().resource(resource).parameter(amount_value).value(request.getAmount().getAmount())
                    .isNullWithFailureCode(ChannelValidatorsEnum.INVALID_AMOUNT_AMOUNT)
                    .validateBigDecimalFieldNotNegativeWithFailureCode(ChannelValidatorsEnum.INVALID_NEGATIVE_AMOUNT);

            // validating amount currency
            validatorBuilder.reset().resource(resource).parameter(currency).value(request.getAmount().getCurrency())
                    .isNullWithFailureCode(ChannelValidatorsEnum.INVALID_CURRENCY).validateFieldMaxLengthWithFailureCodeAndErrorParams(
                            expectedCurrencyLength, ChannelValidatorsEnum.INVALID_CURRENCY_LENGTH);
        }

        if (validatorBuilder.hasError()) {
            validatorBuilder.errorCategory(PaymentHubErrorCategory.Validation.toString())
                    .errorCode(ChannelValidatorsEnum.TRANSFER_SCHEMA_VALIDATION_ERROR.getCode())
                    .errorDescription(ChannelValidatorsEnum.TRANSFER_SCHEMA_VALIDATION_ERROR.getMessage())
                    .developerMessage(ChannelValidatorsEnum.TRANSFER_SCHEMA_VALIDATION_ERROR.getMessage())
                    .defaultUserMessage(ChannelValidatorsEnum.TRANSFER_SCHEMA_VALIDATION_ERROR.getMessage());

            PhErrorDTO.PhErrorDTOBuilder phErrorDTOBuilder = new PhErrorDTO.PhErrorDTOBuilder(ExtValidationError.getErrorCode());
            phErrorDTOBuilder.fromValidatorBuilder(validatorBuilder);

            throw new ValidationException(phErrorDTOBuilder.build());
        }
    }

    public static void validateTransaction(GsmaTransfer request) throws ValidationException {
        final ValidatorBuilder validatorBuilder = new ValidatorBuilder();

        // validate request organisation transaction reference
        validatorBuilder.reset().resource(resource).parameter(requestingOrganisationTransactionReference)
                .value(request.getRequestingOrganisationTransactionReference())
                .isNullWithFailureCode(ChannelValidatorsEnum.INVALID_REQUEST_ORGANIZATION_TRANSACTION_REFERENCE);

        // validate subtype
        validatorBuilder.reset().resource(resource).parameter(subType).value(request.getSubType())
                .isNullWithFailureCode(ChannelValidatorsEnum.INVALID_SUB_TYPE);

        // validate type
        validatorBuilder.reset().resource(resource).parameter(type).value(request.getType())
                .isNullWithFailureCode(ChannelValidatorsEnum.INVALID_TYPE);

        // validate amount
        validatorBuilder.reset().resource(resource).parameter(amount).value(request.getAmount())
                .isNullWithFailureCode(ChannelValidatorsEnum.INVALID_AMOUNT)
                .validateBigDecimalFieldNotNegativeWithFailureCode(ChannelValidatorsEnum.INVALID_NEGATIVE_AMOUNT);

        // validate currency
        validatorBuilder.reset().resource(resource).parameter(currency).value(request.getCurrency())
                .isNullWithFailureCode(ChannelValidatorsEnum.INVALID_CURRENCY)
                .validateFieldMaxLengthWithFailureCodeAndErrorParams(expectedCurrencyLength, ChannelValidatorsEnum.INVALID_CURRENCY_LENGTH);

        // validate description text
        validatorBuilder.reset().resource(resource).parameter(descriptionText).value(request.getDescriptionText())
                .isNullWithFailureCode(ChannelValidatorsEnum.INVALID_DESCRIPTION_TEXT);

        // validate request date
        validatorBuilder.reset().resource(resource).parameter(requestDate).value(request.getRequestDate())
                .isNullWithFailureCode(ChannelValidatorsEnum.INVALID_REQUEST_DATE);

        // validate payer
        validatorBuilder.reset().resource(resource).parameter(payer).value(request.getPayer())
                .isNullWithFailureCode(ChannelValidatorsEnum.INVALID_PAYER);

        // validate payee
        validatorBuilder.reset().resource(resource).parameter(payee).value(request.getPayee())
                .isNullWithFailureCode(ChannelValidatorsEnum.INVALID_PAYEE);

        if (request.getPayer() != null) {
            // validate party id type
            validatorBuilder.reset().resource(resource).parameter(partyIdType).value(request.getPayer().get(0).getPartyIdType())
                    .isNullWithFailureCode(ChannelValidatorsEnum.INVALID_PAYER_PARTY_ID_TYPE);

            // validate party id identifier
            validatorBuilder.reset().resource(resource).parameter(partyIdIdentifier).value(request.getPayer().get(0).getPartyIdIdentifier())
                    .isNullWithFailureCode(ChannelValidatorsEnum.INVALID_PAYER_PARTY_ID_IDENTIFIER);
        }

        if (request.getPayee() != null) {
            // validate party id type
            validatorBuilder.reset().resource(resource).parameter(partyIdType).value(request.getPayee().get(0).getPartyIdType())
                    .isNullWithFailureCode(ChannelValidatorsEnum.INVALID_PAYER_PARTY_ID_TYPE);

            // validate party id identifier
            validatorBuilder.reset().resource(resource).parameter(partyIdIdentifier).value(request.getPayee().get(0).getPartyIdIdentifier())
                    .isNullWithFailureCode(ChannelValidatorsEnum.INVALID_PAYEE_PARTY_ID_IDENTIFIER);
        }

        if (validatorBuilder.hasError()) {
            validatorBuilder.errorCategory(PaymentHubErrorCategory.Validation.toString())
                    .errorCode(ChannelValidatorsEnum.TRANSFER_SCHEMA_VALIDATION_ERROR.getCode())
                    .errorDescription(ChannelValidatorsEnum.TRANSFER_SCHEMA_VALIDATION_ERROR.getMessage())
                    .developerMessage(ChannelValidatorsEnum.TRANSFER_SCHEMA_VALIDATION_ERROR.getMessage())
                    .defaultUserMessage(ChannelValidatorsEnum.TRANSFER_SCHEMA_VALIDATION_ERROR.getMessage());

            PhErrorDTO.PhErrorDTOBuilder phErrorDTOBuilder = new PhErrorDTO.PhErrorDTOBuilder(ExtValidationError.getErrorCode());
            phErrorDTOBuilder.fromValidatorBuilder(validatorBuilder);
            throw new ValidationException(phErrorDTOBuilder.build());
        }
    }

}
