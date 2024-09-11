package org.mifos.connector.mockpaymentschema.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.mifos.connector.mockpaymentschema.schema.MtnRtpDTO;
import org.mifos.connector.mockpaymentschema.service.MtnCollectionService;
import org.mifos.connector.mockpaymentschema.service.SendCallbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@Slf4j
@RestController
@RequestMapping("/mtn/collection")
public class MtnCollectionApi {

    @Autowired
    private MtnCollectionService mtnService;

    @Autowired
    private SendCallbackService callbackService;

    @Value("${mtn.contactpoint}")
    private String mtnContactpoint;

    @Value("${mtn.endpoints.callback}")
    private String callbackEndpoint;

    @Autowired
    private ObjectMapper objectMapper;


    @PostMapping("/v1_0/requesttopay")
    public <T> ResponseEntity<T>  requestToPay(@RequestBody MtnRtpDTO request) throws JsonProcessingException {
       ResponseEntity<T> response =  mtnService.requestToPay(request);

        String callbackUrl = mtnContactpoint + callbackEndpoint;
        callbackService.sendCallback(objectMapper.writeValueAsString(response.getBody()), callbackUrl);


       return response;
    }

    @GetMapping("/v1_0/requesttopay/{referenceId}")
    public <T> ResponseEntity<T>  getRequestToPayStatus(@PathVariable(name = "referenceId") String referenceId) throws
            JsonProcessingException {
        ResponseEntity<T> response =  mtnService.getRequestToPayStatus(referenceId);

        String callbackUrl = mtnContactpoint + callbackEndpoint;
        callbackService.sendCallback(objectMapper.writeValueAsString(response.getBody()), callbackUrl);

        return response;
    }
}
