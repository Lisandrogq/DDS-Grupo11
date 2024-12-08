package org.grupo11;

import org.slf4j.LoggerFactory;

/**
 * ## Usage:
 * 
 * Logger.info("Your message {} is {} ...", param_1, param_2, ...)
 * 
 * Note: `{}` represent a param
 */
public class Logger {
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(Logger.class);

    public static void debug(String message, Object... params) {
        LOGGER.debug(message, params);
    }

    public static void info(String message, Object... params) {
        LOGGER.info(message, params);
    }

    public static void warn(String message, Object... params) {
        LOGGER.warn(message, params);
    }

    public static void error(String message, Object... params) {
        LOGGER.error(message, params);
    }

    public static void error(String message, Throwable throwable, Object... params) {
        LOGGER.error(message, params, throwable);
    }
}
