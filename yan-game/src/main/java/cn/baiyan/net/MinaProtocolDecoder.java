package cn.baiyan.net;

import cn.baiyan.message.Message;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class MinaProtocolDecoder extends CumulativeProtocolDecoder {
    private Logger logger = LoggerFactory.getLogger(MinaProtocolDecoder.class);

    @Override
    protected boolean doDecode(IoSession session, IoBuffer in, ProtocolDecoderOutput out) throws Exception {
        if (in.remaining() < 4) {
            return false;
        }
        IMessageDecoder msgDecoder = SerializerHelper.getInstance().getDecoder();
        in.mark();
        int length = in.getInt();
        int maxReceiveBytes = 4096;
        if (length > maxReceiveBytes) {
            logger.error("单包长度[{}]过大，断开连接", length);
            session.close(true);
            return true;
        }
        if (in.remaining() < length) {
            in.reset();
            return false;
        }

        // 消息元信息常量3表示消息body前面的两个字段，一个short表示module，一个byte表示cmd,
        final int metaSize = CodecProperties.MESSAGE_META_SIZE;
        short moduleId = in.getShort();
        byte cmd = in.get();
        byte[] body = new byte[length - metaSize];
        in.get(body);
        Message msg = msgDecoder.readMessage(moduleId, cmd, body);

        if (moduleId != 0) {
            out.write(msg);
        } else { // 属于组合包
            CombineMessage combineMessage = (CombineMessage) msg;
            List<Packet> packets = combineMessage.getPackets();
            for (Packet packet : packets) {
                // 一次拆包反序列化为具体的Message
                out.write(Packet.asMessage(packet));
            }
        }

        return false;
    }
}
