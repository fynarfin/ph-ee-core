package org.mifos.processor.bulk.api.implementation;

import static org.mifos.processor.bulk.camel.config.CamelProperties.HEADER_PLATFORM_TENANT_ID;
import static org.mifos.processor.bulk.zeebe.ZeebeVariables.FILE_NAME;
import static org.mifos.processor.bulk.zeebe.ZeebeVariables.HEADER_CLIENT_CORRELATION_ID;
import static org.mifos.processor.bulk.zeebe.ZeebeVariables.HEADER_TYPE;
import static org.mifos.processor.bulk.zeebe.ZeebeVariables.PURPOSE;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.mifos.processor.bulk.api.definition.BulkTransfer;
import org.mifos.processor.bulk.file.FileStorageService;
import org.mifos.processor.bulk.utility.Headers;
import org.mifos.processor.bulk.utility.SpringWrapperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class BulkTransferController implements BulkTransfer {

    @Autowired
    private ProducerTemplate producerTemplate;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    FileStorageService fileStorageService;

    @Override
    public String bulkTransfer(String requestId, MultipartFile file, String fileName, String purpose, String type, String tenant)
            throws IOException {
        Headers headers = new Headers.HeaderBuilder().addHeader(HEADER_CLIENT_CORRELATION_ID, requestId).addHeader(PURPOSE, purpose)
                .addHeader(FILE_NAME, fileName).addHeader(HEADER_TYPE, type).addHeader(HEADER_PLATFORM_TENANT_ID, tenant).build();
        Exchange exchange = SpringWrapperUtil.getDefaultWrappedExchange(producerTemplate.getCamelContext(), headers);
        fileStorageService.save(file);
        producerTemplate.send("direct:post-bulk-transfer", exchange);
        return exchange.getIn().getBody(String.class);
    }
}
