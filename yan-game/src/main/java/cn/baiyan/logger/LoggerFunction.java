package cn.baiyan.logger;


import org.apache.log4j.Logger;

public enum LoggerFunction {

    /**
     * 活动相关
     */
    ACTIVITY,;
    public Logger getLogger(){
        return LoggerBuilder.getLogger(this.name().toLowerCase());
    }
}
