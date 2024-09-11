package org.mifos.integrationtest.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "airtel-connector.mock-airtel")
@Component
public class AirtelConfig {

    @Value("${airtel-connector.contactpoint}")
    public String airtelConnectorContactPoint;

    @Value("${callback_url}")
    public String callbackURL;

    @Value("${airtel-connector.airtel-ussd-push}")
    public String airtelUssdPushEndpoint;
}
