package cn.baiyan.net;

/**
 * 消息序列化工厂
 */
public interface SerializerFactory {

    /**
     * 生成解码器
     */
    IMessageDecoder getDecoder();

    /**
     * 生成编码器
     */
    IMessageEncoder getEncoder();
}
