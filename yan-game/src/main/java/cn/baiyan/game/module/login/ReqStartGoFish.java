package cn.baiyan.game.module.login;

import cn.baiyan.annocation.MessageMeta;
import cn.baiyan.game.LoginDataPool;
import cn.baiyan.game.Modules;
import cn.baiyan.message.Message;

import java.util.List;


@MessageMeta(module = Modules.FISH, cmd = LoginDataPool.REQ_LOGIN)
public class ReqStartGoFish extends Message {

    private String userId;

    private int mapId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getMapId() {
        return mapId;
    }

    public void setMapId(int mapId) {
        this.mapId = mapId;
    }

}
