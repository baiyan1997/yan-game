package cn.baiyan.game.module.login;


import cn.baiyan.annocation.MessageMeta;
import cn.baiyan.game.LoginDataPool;
import cn.baiyan.game.Modules;
import cn.baiyan.message.Message;


@MessageMeta(module = Modules.SELL, cmd = LoginDataPool.REQ_LOGIN)
public class ReqSellFish extends Message {

    private String userId;

    private int fishId;

    private int weight;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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
}

