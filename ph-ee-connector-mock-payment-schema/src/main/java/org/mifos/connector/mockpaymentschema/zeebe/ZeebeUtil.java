package org.mifos.connector.mockpaymentschema.zeebe;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ZeebeUtil {

    private ZeebeUtil() {}

    private static Logger logger = LoggerFactory.getLogger(ZeebeUtil.class);
    private static ObjectMapper objectMapper = new ObjectMapper();

}
