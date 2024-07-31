/*
 * Copyright Camunda Services GmbH and/or licensed to Camunda Services GmbH under
 * one or more contributor license agreements. See the NOTICE file distributed
 * with this work for additional information regarding copyright ownership.
 * Licensed under the Zeebe Community License 1.0. You may not use this file
 * except in compliance with the Zeebe Community License 1.0.
 */
package hu.dpc.rt.kafkastreamer.exporter;

import io.camunda.zeebe.exporter.api.Exporter;
import io.camunda.zeebe.exporter.api.context.Context;
import io.camunda.zeebe.exporter.api.context.Controller;
import io.camunda.zeebe.protocol.record.Record;

import org.slf4j.Logger;

import java.time.Duration;
import java.util.Map;

public class KafkaExporter implements Exporter {
    private Logger logger;
    private Controller controller;

    private KafkaExporterConfiguration configuration;

    private KafkaExporterClient client;

    private long lastPosition = -1;

    @Override
    public void configure(final Context context) {
        try {
            logger = context.getLogger();
            //logger.info("This is context: " + context.toString());
            configuration = context.getConfiguration().instantiate(KafkaExporterConfiguration.class);
            /*Map<String, Object> configs = context.getConfiguration().getArguments();
            for (String key: configs.keySet()) {
                logger.info("Config arg: " + key + ":" + configs.get(key));
            }*/
            logger.info("Calling configure");
            logger.info("With context configuration :" + context.getConfiguration());
            logger.info("With context configuration :" + context.getConfiguration().getArguments());
            logger.info("DPC Kafka exporter configured with {}", configuration);

//        context.setFilter(new KafkaRecordFilter(configuration));
        } catch (Exception e) {
            logger.error("Failed to configure KafkaExporter", e);
        }
    }

    @Override
    public void open(final Controller controller) {
        logger.info("DPC Kafka exporter opening");
        this.controller = controller;
        client = createClient();
        scheduleDelayedFlush();
        logger.info("DPC Kafka exporter opened");
    }

    @Override
    public void close() {
        logger.info("Calling close function");
        try {
            flush();
        } catch (final Exception e) {
            logger.warn("Failed to flush records before closing exporter.", e);
        }

        try {
            client.close();
        } catch (final Exception e) {
            logger.warn("Failed to close elasticsearch client", e);
        }

        logger.info("DPC Kafka exporter closed");
    }

    @Override
    public void export(Record<?> record) {
        logger.trace("Exporting record " + record);
        client.index(record);
        lastPosition = record.getPosition();

        if (client.shouldFlush()) {
            flush();
        }
        logger.trace("Finish exporting record " + record);
    }

    protected KafkaExporterClient createClient() {
        return new KafkaExporterClient(configuration, logger);
    }

    private void flushAndReschedule() {
        logger.info("Calling flushAndReschedule function");
        try {
            flush();
        } catch (final Exception e) {
            logger.error("Unexpected exception occurred on periodically flushing bulk, will retry later.", e);
        }
        scheduleDelayedFlush();
    }

    private void scheduleDelayedFlush() {
        controller.scheduleCancellableTask(Duration.ofSeconds(configuration.bulk.delay), this::flushAndReschedule);
    }

    private void flush() {
        logger.info("Calling flush function");
        if (client.flush()) {
            controller.updateLastExportedRecordPosition(lastPosition);
        } else {
            logger.warn("Failed to flush bulk completely");
        }
    }

//    public static class KafkaRecordFilter implements Context.RecordFilter {
//        private final KafkaExporterConfiguration configuration;
//
//        KafkaRecordFilter(final KafkaExporterConfiguration configuration) {
//            this.configuration = configuration;
//        }
//
//        @Override
//        public boolean acceptType(final RecordType recordType) {
//            return configuration.shouldIndexRecordType(recordType);
//        }
//
//        @Override
//        public boolean acceptValue(final ValueType valueType) {
//            return configuration.shouldIndexValueType(valueType);
//        }
//    }
}
