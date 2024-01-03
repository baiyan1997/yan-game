package cn.baiyan.game.module.login;

import cn.baiyan.annocation.MessageMeta;
import cn.baiyan.game.LoginDataPool;
import cn.baiyan.game.Modules;
import cn.baiyan.message.Message;


@MessageMeta(module = Modules.ITEM_UP_GRADE, cmd = LoginDataPool.REQ_LOGIN)
public class ReqUpGradeItem extends Message {

    private String userId;

    private int itemId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }
}
