package cn.baiyan.logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum LoggerSystem {
    /**
     * system exception
     */
    EXCEPTION,
    /**
     * http command
     */
    HTTP_COMMAND,
    /**
     * job scheduler
     */
    CORN_JOB,
    /**
     * server monitor
     */
    MONITOR;

    public Logger getLogger() {
        return LoggerFactory.getLogger(this.name());
    }
}
