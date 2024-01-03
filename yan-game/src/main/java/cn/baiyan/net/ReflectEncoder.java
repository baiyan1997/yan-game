package cn.baiyan.net;

import cn.baiyan.message.Message;
import org.bson.ByteBuf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;

public class ReflectEncoder implements IMessageEncoder {
    private static Logger logger = LoggerFactory.getLogger(ReflectEncoder.class);

    private ThreadLocal<ByteBuffer> localBuffer = ThreadLocal.withInitial(() -> ByteBuffer.allocate(CodecProperties.WRITE_CAPACITY));


    @Override
    public byte[] writeMessageBody(Message message) {
        ByteBuffer allocator = localBuffer.get();
        allocator.clear();
        //写入具体的消息内容

        try {
            Codec messageCodec = Codec.getSerializer(message.getClass());
            messageCodec.encode(allocator, message, null);
        } catch (Exception e) {
            logger.error("读取消息出错，模块号{}，类型{}，异常{}", new Object[]{message.getModule(), message.getCmd(), e});
        }
        // 读写切换
        allocator.flip();
        byte[] body = new byte[allocator.remaining()];
        allocator.get(body);
        return body;
    }
}
