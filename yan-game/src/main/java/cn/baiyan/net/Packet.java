package cn.baiyan.net;

import cn.baiyan.message.Message;
import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;

/**
 * full message unit
 */
public class Packet {

    @Protobuf(order = 10)
    private int module;

    @Protobuf(order = 11)
    private int cmd;

    // body of each message
    @Protobuf(order = 12, fieldType = FieldType.BYTES)
    private byte[] body;

    public Packet() {

    }

    public static Packet valueOf(Message message) {
        Packet packet = new Packet();
        packet.module = message.getModule();
        packet.cmd = message.getCmd();

        IMessageEncoder msgEncoder = SerializerHelper.getInstance().getEncoder();
        packet.body = msgEncoder.writeMessageBody(message);
        return packet;
    }

    public static Message asMessage(Packet packet) {
        IMessageDecoder msgDecoder = SerializerHelper.getInstance().getDecoder();
        return msgDecoder.readMessage((short) packet.module, (byte) packet.cmd, packet.body);
    }


}
