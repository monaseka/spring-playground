package com.playground.java.draft.logging;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.classic.spi.ThrowableProxyUtil;
import org.springframework.boot.logging.structured.StructuredLogFormatter;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.ObjectWriter;

import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Custom structured log formatter that prints formatted, indented (pretty) JSON.
 */
public class PrettyJsonLoggingFormatter implements StructuredLogFormatter<ILoggingEvent> {

    private final ObjectWriter objectWriter = new ObjectMapper().writerWithDefaultPrettyPrinter();

    @Override
    public String format(ILoggingEvent event) {
        try {
            Map<String, Object> log = new LinkedHashMap<>();
            log.put("timestamp", DateTimeFormatter.ISO_INSTANT.format(event.getInstant()));
            log.put("level", event.getLevel().toString());
            log.put("thread", event.getThreadName());
            log.put("logger", event.getLoggerName());
            log.put("message", event.getFormattedMessage());

            // SLF4J fluent API key-value pairs
            if (event.getKeyValuePairs() != null && !event.getKeyValuePairs().isEmpty()) {
                Map<String, Object> context = new LinkedHashMap<>();
                event.getKeyValuePairs().forEach(kv -> context.put(kv.key, kv.value));
                log.put("context", context);
            }

            // MDC properties
            if (event.getMDCPropertyMap() != null && !event.getMDCPropertyMap().isEmpty()) {
                log.put("mdc", event.getMDCPropertyMap());
            }

            // Exceptions / Stack traces
            IThrowableProxy throwableProxy = event.getThrowableProxy();
            if (throwableProxy != null) {
                log.put("stack_trace", ThrowableProxyUtil.asString(throwableProxy));
            }

            return objectWriter.writeValueAsString(log) + System.lineSeparator();
        } catch (Exception ex) {
            return event.getFormattedMessage() + System.lineSeparator();
        }
    }
}
