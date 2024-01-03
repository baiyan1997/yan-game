package cn.baiyan.game.module.login;

import cn.baiyan.annocation.MessageMeta;
import cn.baiyan.game.LoginDataPool;
import cn.baiyan.game.Modules;
import cn.baiyan.message.Message;

@MessageMeta(module = Modules.REGISTER, cmd = LoginDataPool.REQ_LOGIN)
public class ReqRegister extends Message {

    private Long userId;

    private String password;

    private String name;

    public Long getId() {
        return userId;
    }

    public void setId(Long userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
