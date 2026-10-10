package rroyo.jge.utils;

import rroyo.JUtils.Utils.Logging.LoggerAux;

/**
 * The {@code Log} class is a simplified wrapper for an external logging utility.
 * It provides static methods to easily output informational messages, warnings, and errors
 * to the console or log file, helping in debugging and application monitoring.
 *
 * @see LoggerAux
 */
public final class Log {

    /**
     * Logs an informational message. Used for standard execution flow data.
     *
     * @param message The message to log.
     */
    public static void info(String message) {
        LoggerAux.info(message);
    }

    /**
     * Logs a warning message. Used for non-fatal issues or unexpected behaviors
     * that do not stop the execution of the game.
     *
     * @param message The warning message to log.
     */
    public static void warn(String message) {
        LoggerAux.warn(message);
    }

    /**
     * Logs an error message. Used for severe problems or exceptions that
     * disrupt the normal execution of the game.
     *
     * @param message The error message to log.
     */
    public static void error(String message) {
        LoggerAux.error(message);
    }

}
