package org.mifos.identityaccountmapper.api.definition;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.concurrent.ExecutionException;
import org.mifos.identityaccountmapper.data.RequestDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@Tag(name = "GOV")
public interface RegisterBeneficiaryApi {

    @Operation(summary = "Registering new beneficiary")
    @PostMapping("/beneficiary")

    <T> ResponseEntity<T> registerBeneficiary(@RequestHeader(value = "X-CallbackURL") String callbackURL,
            @RequestHeader(value = "X-Registering-Institution-ID") String registeringInstitutionId, @RequestBody RequestDTO requestBody)
            throws ExecutionException, InterruptedException, JsonProcessingException;

}
