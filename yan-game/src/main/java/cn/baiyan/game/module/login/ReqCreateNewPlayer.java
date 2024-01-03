package cn.baiyan.game.module.login;

import cn.baiyan.annocation.MessageMeta;
import cn.baiyan.game.Modules;
import cn.baiyan.game.PlayerDataPool;
import cn.baiyan.message.Message;

@MessageMeta(module = 1, cmd = PlayerDataPool.REQ_CREATE_PLAYER)
public class ReqCreateNewPlayer extends Message {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
