package org.mifos.identityaccountmapper.api.definition;

import java.util.concurrent.ExecutionException;
import org.mifos.identityaccountmapper.data.RequestDTO;
import org.mifos.identityaccountmapper.data.ResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

public interface BatchAccountLookupApi {

    @PostMapping("/accountLookup")
    ResponseEntity<ResponseDTO> batchAccountLookup(@RequestHeader(value = "X-CallbackURL") String callbackURL,
            @RequestBody RequestDTO requestBody, @RequestHeader(value = "X-Registering-Institution-ID") String registeringInstitutionId)
            throws ExecutionException, InterruptedException;
}
