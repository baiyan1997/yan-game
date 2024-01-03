package cn.baiyan.listener.handler;

import cn.baiyan.listener.EventHandler;
import cn.baiyan.listener.EventType;
import cn.baiyan.listener.Listener;
import cn.baiyan.listener.event.PlayerLoginEvent;

@Listener
public class LoginListener {
    @EventHandler(value = EventType.LOGIN)
    public void onPlayerLogin(PlayerLoginEvent loginEvent){
        System.out.println("hello world");
    }
}
