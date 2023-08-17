package com.festago.presentation;

import java.util.EnumMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

public class ErrorLogger {

    private static final Logger errorLogger = LoggerFactory.getLogger("ErrorLogger");

    private final EnumMap<Level, LogFunction> logFunctions = new EnumMap<>(Level.class);

    public ErrorLogger() {
        this.logFunctions.put(Level.INFO, errorLogger::info);
        this.logFunctions.put(Level.WARN, errorLogger::warn);
        this.logFunctions.put(Level.ERROR, errorLogger::error);
    }

    public LogFunction get(Level level) {
        return logFunctions.get(level);
    }

    public boolean isEnabledForLevel(Level level) {
        return errorLogger.isEnabledForLevel(level);
    }

    @FunctionalInterface
    public interface LogFunction {

        void log(String format, Object... arguments);
    }
}
