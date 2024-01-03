package cn.baiyan.cross.callback;

import cn.baiyan.annocation.MessageMeta;
import cn.baiyan.cross.CrossCommands;
import cn.baiyan.game.Modules;
import cn.baiyan.logger.LoggerUtils;
import cn.baiyan.message.Message;
import cn.baiyan.utils.JsonUtils;

/**
 * 跨服回调响应方
 */
@MessageMeta(module = Modules.CROSS, cmd = CrossCommands.F2G_CALL_BACK)
public class F2GCallBack extends Message {

    private int index;

    private String data;

    private String msgClass;

    public static F2GCallBack valueOf(Message message) {
        F2GCallBack response = new F2GCallBack();
        response.data = JsonUtils.object2String(message);
        response.msgClass = message.getClass().getName();

        return response;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getMsgClass() {
        return msgClass;
    }

    public void setMsgClass(String msgClass) {
        this.msgClass = msgClass;
    }

    public Message getMessage() {
        try {
            return (Message) JsonUtils.string2Object(data, Class.forName(msgClass));
        } catch (Exception e) {
            LoggerUtils.error("", e);
            return null;
        }
    }
}