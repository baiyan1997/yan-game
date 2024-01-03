package cn.baiyan.cross.demo;


import cn.baiyan.annocation.MessageMeta;
import cn.baiyan.cross.CrossCommands;
import cn.baiyan.game.Modules;
import cn.baiyan.message.Message;

@MessageMeta(module = Modules.CROSS, cmd  = CrossCommands.G2F_HEART_BEAT)
public class G2FHeartBeat extends Message {

    private long time = System.currentTimeMillis();

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
