package cn.baiyan.net;

import cn.baiyan.annocation.MessageMeta;
import cn.baiyan.message.Message;
import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;

import java.util.ArrayList;
import java.util.List;

@MessageMeta
@ProtobufClass
public class CombineMessage extends Message {
    private List<Packet> packets = new ArrayList<>();

    public CombineMessage() {

    }

    /**
     * add new message to combine queue
     */
    public void addMessage(Message message) {
        this.packets.add(Packet.valueOf(message));
    }

    public List<Packet> getPackets() {
        return packets;
    }

    public int getCacheSize() {
        return this.packets.size();
    }
}
