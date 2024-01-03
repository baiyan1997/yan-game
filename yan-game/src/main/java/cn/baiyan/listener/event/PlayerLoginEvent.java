package cn.baiyan.listener.event;

import cn.baiyan.listener.BasePlayerEvent;
import cn.baiyan.listener.EventType;

/**
 * 登录事件
 */
public class PlayerLoginEvent extends BasePlayerEvent {

    public PlayerLoginEvent(EventType evtType, long playerId) {
        super(evtType, playerId);
    }
}
