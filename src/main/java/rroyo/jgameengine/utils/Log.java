package rroyo.jgameengine.utils;

import java.util.logging.Level;
import java.util.logging.Logger;

public final class Log {

    private static Logger logger = Logger.getGlobal();

    public static void info(String message) {
        logger.info(message);
    }

    public static void warning(String message) {
        logger.warning(message);
    }

}
