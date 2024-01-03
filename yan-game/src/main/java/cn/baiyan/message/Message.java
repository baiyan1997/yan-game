package cn.baiyan.message;

import cn.baiyan.actor.MailBox;
import cn.baiyan.annocation.MessageMeta;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class Message {

    /**
     * messageMeta, module of message
     */
    public short getModule() {
        MessageMeta annotation = getClass().getAnnotation(MessageMeta.class);
        if (annotation != null) {
            return annotation.module();
        }
        return 0;
    }

    /**
     * messageMeta, subType of module
     */
    public byte getCmd() {
        MessageMeta annotation = getClass().getAnnotation(MessageMeta.class);
        if (annotation != null) {
            return annotation.cmd();
        }
        return 0;
    }

    public MailBox mailQueue() {
        return null;
    }

    public String key() {
        return this.getModule() + "-" + this.getCmd();
    }
}
