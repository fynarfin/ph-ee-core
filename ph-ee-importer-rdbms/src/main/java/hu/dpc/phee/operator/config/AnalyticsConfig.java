package hu.dpc.phee.operator.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AnalyticsConfig {

    @Value("${reliability.events-timestamps-dump-enabled}")
    public String enableEventsTimestampsDump;
}
