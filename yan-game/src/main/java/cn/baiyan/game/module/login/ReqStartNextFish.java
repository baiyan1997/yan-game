package cn.baiyan.game.module.login;

import cn.baiyan.annocation.MessageMeta;
import cn.baiyan.game.LoginDataPool;
import cn.baiyan.game.Modules;
import cn.baiyan.message.Message;

@MessageMeta(module = Modules.NEXT_FISH, cmd = LoginDataPool.REQ_LOGIN)
public class ReqStartNextFish extends Message {
    private String userId;

    private int mapId;

    private int fishId;

    private int weight;

    private int status;

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

    public int getFishId() {
        return fishId;
    }

    public void setFishId(int fishId) {
        this.fishId = fishId;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
