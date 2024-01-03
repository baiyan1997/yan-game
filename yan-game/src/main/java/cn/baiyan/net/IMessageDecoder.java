package cn.baiyan.net;

import cn.baiyan.message.Message;

/**
 * 私有协议栈消息解码器
 */
public interface IMessageDecoder {

    /**
     * 根据消息源信息反序列号为消息
     * body已经是一个完整的消息包体，所以解码buff不需要复杂的操作，用NIO的byteBuffer即可
     */
    Message readMessage(short module, byte cmd, byte[] body);
}
