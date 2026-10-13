package rroyo.jge.utils;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Utility class for event logging with console style support.
 * <p>
 * This class allows printing formatted messages with timestamps and severity levels
 * visually differentiated through colors and styles defined in {@link TStyle}.
 * </p>
 *
 * @author rroyo
 */
public final class Log {

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private Log() {}

    /**
     * Enumerates the severity levels supported by the logger.
     */
    private enum LogType {INFO, WARNING, ERROR, DEBUG}

    /**
     * Represents the directory on the 'LogFiles' that is going to be created.
     */
    private static File logDirectory;

    /**
     * Text string where message logs are saved if a 'LogFile' does not exist.
     */
    private static final StringBuilder logs = new StringBuilder();

    /**
     * Indicates whether the 'debug logs' will be printed in the console.
     */
    private static boolean debugEnabled = true;

    /**
     * Indicates whether the logs will be displayed in the console.
     */
    private static boolean consoleOutputEnabled = true;

    /** Standard date and time formatter (Year-Month-Day Hour:Minute:Second). */
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    /**
     * Enables or disables debug level messages in the console.
     * @param enabled True to show debug logs, false to hide them.
     */
    public static void setDebugEnabled(boolean enabled) {
        debugEnabled = enabled;
    }

    /**
     * Enables or disables all log output to the system console.
     * Note: This does not affect file logging.
     * @param enabled True to show logs in console, false to silence them.
     */
    public static void setConsoleOutputEnabled(boolean enabled) {
        consoleOutputEnabled = enabled;
    }

    /**
     * Allows you to configure a directory to save 'LogFiles'
     * @param path The directory path
     */
    public static void setLogDirectory(String path) {
        Validator.notBlank(path, "Path cannot be blank");
        setLogDirectory(new File(path));
    }

    /**
     * Allows you to configure a directory to save 'LogFiles'
     * @param directory The directory where log files will be saved.
     *                  This method validates that the provided File object is not null
     *                  and is indeed a directory before setting it as the log directory.
     *                  If the validation fails, an IllegalArgumentException is thrown with an appropriate message.
     */
    public static void setLogDirectory(File directory) {
        Validator.notNull(directory, "Directory cannot be null");
        Validator.assertTrue(directory.isDirectory(), "Require an Directory");

        logDirectory = directory;

        String logsStr = logs.toString();
        logs.delete(0, logs.length());

        logs.append("\n----------------------------------------------------------------\n").append(logsStr);

        info("LogDirectory setted [" + directory.getAbsolutePath() + "]");
    }

    /**
     * Return the configured 'Log Directory'
     * @return Log Directory
     */
    public static File getLogDirectory() {
        return logDirectory;
    }

    /**
     * Indicates whether the 'debug logs' will be printed in the console.
     * @return True if console debug is enabled
     */
    public static boolean isDebugEnabled() {
        return debugEnabled;
    }

    /**
     * Indicates whether the logs will be displayed in the console.
     * @return True if console logging is enabled
     */
    public static boolean isConsoleOutputEnabled() {
        return consoleOutputEnabled;
    }

    /**
     * Logs an information message to the console.
     *
     * @param msg The informative message to display.
     * @return The log message
     */
    public static String info(String msg) {
        return log(LogType.INFO, msg);
    }

    /**
     * Logs a warning visualized in yellow color.
     *
     * @param msg The warning message.
     * @return The log message
     */
    public static String warn(String msg) {
        return log(LogType.WARNING, msg);
    }

    /**
     * Logs a debug message visualized in cyan color.
     * Only displayed if debug is enabled.
     *
     * @param msg The technical message for debugging.
     * @return The log message
     */
    public static String debug(String msg) {
        return log(LogType.DEBUG, msg);
    }

    /**
     * Logs a critical error visualized in red color.
     *
     * @param msg The descriptive error message.
     * @return The log message
     */
    public static String error(String msg) {
        return log(LogType.ERROR, msg);
    }

    /**
     * Logs a critical error including an exception description.
     *
     * @param msg       Personalized error message.
     * @param exception Captured exception from which the technical message will be extracted.
     * @return The log message
     */
    public static String error(String msg, Exception exception) {
        Validator.notNull(exception, "Exception cannot be null");
        return log(LogType.ERROR, String.format("%s%n%s", msg,
                (exception.getMessage() != null)
                        ? exception.getMessage() : exception.toString()
        ));
    }

    /**
     * Logs a critical error by extracting information directly from an Exception object.
     * <p>
     * This method validates that the exception is not null using {@link Validator}
     * before processing. It attempts to retrieve the specific error message;
     * if unavailable, it defaults to the exception's string representation.
     * </p>
     *
     * @param exception The exception to be logged.
     * @return The formatted error message string as processed by the internal log system.
     * @throws IllegalArgumentException if the provided exception is null.
     */
    public static String error(Exception exception) {
        Validator.notNull(exception, "Exception cannot be null");
        return log(LogType.ERROR, exception.toString());
    }

    /**
     * Internal method that centralizes formatting and printing of logs.
     * <p>
     * Applies italic styles and colors to both the level tag and the content
     * based on the provided log type.
     * </p>
     *
     * @param logType Severity level.
     * @param msg     Processed message with its corresponding styles.
     * @return The log message
     */
    private static String log(LogType logType, String msg) {
        Validator.notNull(logType, "LogType cannot be null");
        Validator.notBlank(msg, "Message is blank");
        String log = String.format(
                "[%s]\t[%s]  \t%s%n",
                LocalDateTime.now().format(formatter),
                switch (logType) {
                    case INFO -> TStyle.italic(logType);
                    case WARNING -> TStyle.italic(TStyle.yellow(logType));
                    case ERROR -> TStyle.italic(TStyle.red(logType));
                    case DEBUG -> TStyle.italic(TStyle.cyan(logType));
                },
                switch (logType) {
                    case WARNING -> TStyle.yellow(msg);
                    case ERROR -> TStyle.red(msg);
                    case DEBUG -> TStyle.cyan(msg);
                    default -> msg;
                }
        );
        if (consoleOutputEnabled) {
            if (logType != LogType.DEBUG || debugEnabled) {
                System.out.print(log);
            }
        }
        logs.append(TStyle.clearStyle(log));
        if (logDirectory != null) {
            File logFile = new File(String.format("%s%sLog_%s.log",
                    logDirectory.getAbsolutePath(),
                    File.separator,
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
            ));

            try (java.io.FileWriter fw = new java.io.FileWriter(logFile, true)) {
                fw.write(logs.toString());
                logs.delete(0, logs.length());
            } catch (IOException ignored) {
                System.err.println("Critical: Could not write to log file.");
            }

        }
        return log;
    }
}