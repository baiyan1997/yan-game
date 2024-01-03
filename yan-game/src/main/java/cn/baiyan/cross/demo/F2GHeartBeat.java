package cn.baiyan.cross.demo;


import cn.baiyan.annocation.MessageMeta;
import cn.baiyan.cross.CrossCommands;
import cn.baiyan.game.Modules;
import cn.baiyan.message.Message;

@MessageMeta(module = Modules.CROSS, cmd  = CrossCommands.F2G_HEART_BEAT)
public class F2GHeartBeat extends Message {

    private long time = System.currentTimeMillis();

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
