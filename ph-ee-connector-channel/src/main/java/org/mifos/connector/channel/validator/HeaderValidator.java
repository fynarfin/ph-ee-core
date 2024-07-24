package org.mifos.connector.channel.validator;

import static org.mifos.connector.common.exception.PaymentHubError.ExtValidationError;

import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import org.mifos.connector.channel.utils.ChannelValidatorsEnum;
import org.mifos.connector.channel.utils.HeaderConstants;
import org.mifos.connector.common.channel.dto.PhErrorDTO;
import org.mifos.connector.common.exception.PaymentHubErrorCategory;
import org.mifos.connector.common.validation.ValidatorBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class HeaderValidator {

    @Autowired
    private UnsupportedParameterValidation unsupportedParameterValidator;

    @Value("#{'${default_headers}'.split(',')}")
    private List<String> defaultHeader;

    private static final String resource = "channelValidator";

    public PhErrorDTO validateTransfer(Set<String> requiredHeaders, HttpServletRequest request) {
        final ValidatorBuilder validatorBuilder = new ValidatorBuilder();

        List<String> headers = getHeaderList(request);

        unsupportedParameterValidator.handleRequiredParameterValidation(headers, requiredHeaders, validatorBuilder);

        // Checks for Platform_TenantId
        validatorBuilder.validateFieldIsNullAndMaxLengthWithFailureCode(resource, HeaderConstants.PLATFORM_TENANT_ID,
                request.getHeader(HeaderConstants.PLATFORM_TENANT_ID), ChannelValidatorsEnum.INVALID_PLATFORM_TENANT_ID, 20,
                ChannelValidatorsEnum.INVALID_PLATFORM_TENANT_ID_LENGTH);

        return handleValidationErrors(validatorBuilder);
    }

    public PhErrorDTO validateTransactionRequest(Set<String> requiredHeaders, HttpServletRequest request) {
        final ValidatorBuilder validatorBuilder = new ValidatorBuilder();

        List<String> headers = getHeaderList(request);

        unsupportedParameterValidator.handleRequiredParameterValidation(headers, requiredHeaders, validatorBuilder);

        // Checks for Platform_TenantId
        validatorBuilder.validateFieldIsNullAndMaxLengthWithFailureCode(resource, HeaderConstants.PLATFORM_TENANT_ID,
                request.getHeader(HeaderConstants.PLATFORM_TENANT_ID), ChannelValidatorsEnum.INVALID_PLATFORM_TENANT_ID, 20,
                ChannelValidatorsEnum.INVALID_PLATFORM_TENANT_ID_LENGTH);

        return handleValidationErrors(validatorBuilder);
    }

    public PhErrorDTO validateGsmaTransaction(Set<String> requiredHeaders, HttpServletRequest request) {
        final ValidatorBuilder validatorBuilder = new ValidatorBuilder();

        List<String> headers = getHeaderList(request);

        unsupportedParameterValidator.handleRequiredParameterValidation(headers, requiredHeaders, validatorBuilder);

        // checks for ams name
        validatorBuilder.validateFieldIsNullAndMaxLengthWithFailureCode(resource, HeaderConstants.AMS_NAME,
                request.getHeader(HeaderConstants.AMS_NAME), ChannelValidatorsEnum.INVALID_AMS_NAME, 20,
                ChannelValidatorsEnum.INVALID_AMS_NAME_LENGTH);

        // checks for account holding institution id
        validatorBuilder.validateFieldIsNullAndMaxLengthWithFailureCode(resource, HeaderConstants.ACCOUNT_HOLDING_INSTITUTION_ID,
                request.getHeader(HeaderConstants.ACCOUNT_HOLDING_INSTITUTION_ID),
                ChannelValidatorsEnum.INVALID_ACCOUNT_HOLDING_INSTITUTION_ID, 20,
                ChannelValidatorsEnum.INVALID_ACCOUNT_HOLDING_INSTITUTION_ID_LENGTH);

        // checks for x callback url
        validatorBuilder.validateFieldIsNullAndMaxLengthWithFailureCode(resource, HeaderConstants.X_Callback_URL,
                request.getHeader(HeaderConstants.X_Callback_URL), ChannelValidatorsEnum.INVALID_X_CALLBACK_URL, 1000,
                ChannelValidatorsEnum.INVALID_X_CALLBACK_URL_LENGTH);

        // checks for x correlation id
        validatorBuilder.validateFieldIgnoreNullAndMaxLengthWithFailureCode(resource, HeaderConstants.CLIENT_CORRELATION_ID,
                request.getHeader(HeaderConstants.CLIENT_CORRELATION_ID), 100, ChannelValidatorsEnum.INVALID_CLIENT_CORRELATION_ID_LENGTH);

        return handleValidationErrors(validatorBuilder);
    }

    private PhErrorDTO handleValidationErrors(ValidatorBuilder validatorBuilder) {
        if (validatorBuilder.hasError()) {
            validatorBuilder.errorCategory(PaymentHubErrorCategory.Validation.toString())
                    .errorCode(ChannelValidatorsEnum.HEADER_VALIDATION_ERROR.getCode())
                    .errorDescription(ChannelValidatorsEnum.HEADER_VALIDATION_ERROR.getMessage())
                    .developerMessage(ChannelValidatorsEnum.HEADER_VALIDATION_ERROR.getMessage())
                    .defaultUserMessage(ChannelValidatorsEnum.HEADER_VALIDATION_ERROR.getMessage());

            PhErrorDTO.PhErrorDTOBuilder phErrorDTOBuilder = new PhErrorDTO.PhErrorDTOBuilder(ExtValidationError.getErrorCode());
            phErrorDTOBuilder.fromValidatorBuilder(validatorBuilder);
            return phErrorDTOBuilder.build();
        }
        return null;
    }

    public List<String> getHeaderList(HttpServletRequest request) {
        Enumeration<String> headers = request.getHeaderNames();
        return Collections.list(request.getHeaderNames());
    }

}
