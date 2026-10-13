package rroyo.jge.utils;

/**
 * Utility class for data validation and constraint enforcement.
 * <p>
 * This class provides static methods to verify that variables meet specific requirements,
 * throwing {@link IllegalArgumentException} with formatted and styled messages if
 * a validation fails. It is designed to be used as guard clauses across the library.
 * </p>
 *
 * @author rroyo
 * @version 1.0
 */
public final class Validator {

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private Validator() {}

    /**
     * Validates that the provided object is not null.
     *
     * @param obj The object to check.
     * @param msg The error message to display if the object is null.
     * @throws IllegalArgumentException if the object is null.
     */
    public static void notNull(Object obj, String msg) {
        IllegalArgumentException iae = new IllegalArgumentException(msg);
        if (obj == null) {
            Log.error(iae);
            throw iae;
        }
    }

    /**
     * Validates that a string is neither null nor empty (including whitespace).
     *
     * @param str The string to check.
     * @param msg The error message to display if validation fails.
     * @throws IllegalArgumentException if the string is null, empty, or only whitespace.
     */
    public static void notBlank(String str, String msg) {
        IllegalArgumentException iae = new IllegalArgumentException(msg);
        if (str == null || str.trim().isEmpty()) {
            Log.error(iae);
            throw iae;
        }
    }

    /**
     * Validates that an integer value is within a specific inclusive range.
     *
     * @param value The value to check.
     * @param min   The minimum allowed value.
     * @param max   The maximum allowed value.
     * @param msg   The base error message.
     * @throws IllegalArgumentException if the value is outside the specified range.
     */
    public static void inRange(int value, int min, int max, String msg) {
        IllegalArgumentException iae = new IllegalArgumentException(
                String.format("%s (Value: %d, Range: [%d - %d])", msg, value, min, max)
        );
        if (value < min || value > max) {
            Log.error(iae);
            throw iae;
        }
    }

    /**
     * Validates that a double value is within a specific inclusive range.
     *
     * @param value The value to check.
     * @param min   The minimum allowed value.
     * @param max   The maximum allowed value.
     * @param msg   The base error message.
     * @throws IllegalArgumentException if the value is outside the specified range.
     */
    public static void inRange(double value, double min, double max, String msg) {
        IllegalArgumentException iae = new IllegalArgumentException(
                String.format("%s (Value: %.1f | Range: [%.1f - %.1f])", msg, value, min, max)
        );
        if (value < min || value > max) {
            Log.error(iae);
            throw iae;
        }
    }

    /**
     * Validates that a numeric value is strictly greater than zero.
     *
     * @param value The value to check.
     * @param msg   The error message to display if the value is zero or negative.
     * @throws IllegalArgumentException if the value is not positive.
     */
    public static void isPositive(double value, String msg) {
        IllegalArgumentException iae = new IllegalArgumentException(msg);
        if (value <= 0) {
            Log.error(iae);
            throw iae;
        }
    }

    /**
     * Validates a custom boolean condition.
     *
     * @param condition The condition to evaluate. If false, an exception is thrown.
     * @param msg The error message to display if the condition is NOT met.
     * @throws IllegalArgumentException if the condition evaluates to false.
     */
    public static void assertTrue(boolean condition, String msg) {
        IllegalArgumentException iae = new IllegalArgumentException(msg);
        if (!condition) {
            Log.error(iae);
            throw iae;
        }
    }

    /**
     * Validates a custom boolean condition.
     *
     * @param condition The condition to evaluate. If true, an exception is thrown.
     * @param msg The error message to display if the condition IS met.
     * @throws IllegalArgumentException if the condition evaluates to true.
     */
    public static void assertFalse(boolean condition, String msg) {
        assertTrue(!condition, msg);
    }

}