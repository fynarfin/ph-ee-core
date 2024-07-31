package org.mifos.connector.mockpaymentschema;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("org.mifos.connector.mockpaymentschema")
@SuppressWarnings("checkstyle:HideUtilityClassConstructor")
public class MockPaymentSchemaApplication {

    public static void main(String[] args) {
        SpringApplication.run(MockPaymentSchemaApplication.class, args);
    }

}
