package cn.baiyan.logger;

import org.slf4j.Logger;

public class LoggerUtils {

    /**
     * Log an exception at the level with an accompanying message.
     */
    public static void error(String errMsg, Exception e) {
        Logger logger = LoggerSystem.EXCEPTION.getLogger();
        logger.error(errMsg, e);
    }

    /**
     * format the format string
     */
    public static void error(String format, Object... arguments) {
        Logger logger = LoggerSystem.EXCEPTION.getLogger();
        logger.error(format, arguments);
    }
}
