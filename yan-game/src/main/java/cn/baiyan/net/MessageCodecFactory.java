package cn.baiyan.net;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

public class MessageCodecFactory implements ProtocolCodecFactory {

    private MinaProtocolDecoder decoder = new MinaProtocolDecoder();

    private MinaProtocolEncoder encoder = new MinaProtocolEncoder();

    @Override
    public ProtocolEncoder getEncoder(IoSession ioSession) throws Exception {
        return null;
    }

    @Override
    public ProtocolDecoder getDecoder(IoSession ioSession) throws Exception {
        return null;
    }
}
