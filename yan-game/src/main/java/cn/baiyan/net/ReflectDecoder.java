package cn.baiyan.net;

import cn.baiyan.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;

public class ReflectDecoder implements IMessageDecoder {
    private Logger logger = LoggerFactory.getLogger(IMessageDecoder.class);

    @Override
    public Message readMessage(short module, byte cmd, byte[] body) {
        // 消息序列化这里的buff已经是一个完整的包体
        ByteBuffer in = ByteBuffer.allocate(body.length);
        in.put(body);
        in.flip();
        Class<?> msgClazz = MessageFactory.INSTANCE.getMessage(module, cmd);
        try {
            Codec messageCodec = Codec.getSerializer(msgClazz);
//            Message message = (Message) messageCodec.decode(in, msgClazz, null);
            return null;
        } catch (Exception e) {
            logger.error("读取消息出错，模块号{}，类型{}，异常", new Object[]{module, cmd, e});
        }
        return null;
    }
}
