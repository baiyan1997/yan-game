package cn.baiyan.game.module.login;

import cn.baiyan.annocation.MessageMeta;
import cn.baiyan.game.LoginDataPool;
import cn.baiyan.game.Modules;
import cn.baiyan.message.Message;

@MessageMeta(module = Modules.BUY, cmd = LoginDataPool.REQ_LOGIN)
public class ReqBuyItem extends Message {

    private  String userId;

    private int itemId;

    private int count;

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
