package cn.baiyan.net;

import cn.baiyan.message.Message;

/**
 * 私有协议栈消息编码器
 */
public interface IMessageEncoder {

    /**
     * 把一个具体消息序列化byte[]
     */
    byte[] writeMessageBody(Message message);
}
