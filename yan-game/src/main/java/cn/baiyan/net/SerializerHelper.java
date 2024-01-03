package cn.baiyan.net;

public class SerializerHelper {
    public static volatile SerializerHelper instance;

    /**
     * 消息私有协议栈编解码
     */
    private MessageCodecFactory codecFactory;

    /**
     * 消息序列化解码器
     */
    private static SerializerFactory serializerFactory = new ReflectSerializerFactory();

    public static SerializerHelper getInstance() {
        if (instance != null) {
            return instance;
        }
        synchronized (SerializerHelper.class) {
            if (instance == null) {
                SerializerHelper self = new SerializerHelper();
                self.initialize();
                instance = self;
            }
        }
        return instance;
    }

    private void initialize() {
        codecFactory = new MessageCodecFactory();
    }

    public MessageCodecFactory getCodecFactory() {
        return codecFactory;
    }

    public void setCodecFactory(MessageCodecFactory codecFactory) {
        this.codecFactory = codecFactory;
    }

    public static void setSerializerFactory(SerializerFactory serializerFactory) {
        SerializerHelper.serializerFactory = serializerFactory;
    }

    public IMessageDecoder getDecoder() {
        return serializerFactory.getDecoder();
    }

    public IMessageEncoder getEncoder() {
        return serializerFactory.getEncoder();
    }

    public SerializerFactory getSerializerFactory() {
        return serializerFactory;
    }

}
