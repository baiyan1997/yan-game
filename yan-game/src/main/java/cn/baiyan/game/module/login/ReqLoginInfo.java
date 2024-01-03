package cn.baiyan.game.module.login;

import cn.baiyan.annocation.MessageMeta;
import cn.baiyan.game.LoginDataPool;
import cn.baiyan.game.Modules;
import cn.baiyan.message.Message;

@MessageMeta(module = Modules.LOGIN_INFO, cmd = LoginDataPool.REQ_LOGIN)
public class ReqLoginInfo extends Message {

    private String userId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
