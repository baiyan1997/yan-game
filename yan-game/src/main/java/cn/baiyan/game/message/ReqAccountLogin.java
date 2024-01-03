package cn.baiyan.game.message;

import cn.baiyan.actor.MailBox;
import cn.baiyan.annocation.MessageMeta;
import cn.baiyan.game.LoginDataPool;
import cn.baiyan.game.Modules;
import cn.baiyan.message.Message;
import cn.baiyan.thread.ThreadCenter;
import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;

@MessageMeta(module = Modules.LOGIN, cmd = LoginDataPool.REQ_LOGIN)
@ProtobufClass
public class ReqAccountLogin extends Message {

    private long userId;

    private String code;

    private String anonymousCode;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getAnonymousCode() {
        return anonymousCode;
    }

    public void setAnonymousCode(String anonymousCode) {
        this.anonymousCode = anonymousCode;
    }

    public MailBox mailQueue() {
        return ThreadCenter.getLoginQueue().getSharedMailQueue(userId);
    }


}
