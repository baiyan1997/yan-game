package cn.baiyan.net;

import cn.baiyan.message.IdSession;
import cn.baiyan.message.Message;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class MsgIoHandler extends ChannelInboundHandlerAdapter {

    private final static Logger logger = LoggerFactory.getLogger(MsgIoHandler.class);

    private IMessageDispatcher messageDispatcher;

    private SerializerFactory messageSerializer;

    public MsgIoHandler(IMessageDispatcher messageDispatcher, SerializerFactory messageSerializer) {
        this.messageDispatcher = messageDispatcher;
        this.messageSerializer = messageSerializer;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        if (!ChannelUtils.addChannelSession(ctx.channel(), new NettySession(ctx.channel()))) {
            ctx.channel().close();
            logger.error("duplicate session, ip=[{}]", ChannelUtils.getIp(ctx.channel()));
        }

        IdSession session = ChannelUtils.getSessionBy(ctx.channel());
        messageDispatcher.onSessionCreated(session);
    }

    @Override
    public void channelRead(ChannelHandlerContext context, Object msg) throws Exception {
        Message packet = (Message) msg;
        logger.info("receive pact, content is {}", packet.getClass().getSimpleName());
        final Channel channel = context.channel();
        IdSession session = ChannelUtils.getSessionBy(channel);
        messageDispatcher.dispatch(session, packet);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        Channel channel = ctx.channel();
        if (channel.isActive() || channel.isOpen()) {
            ctx.close();
        }
        if (!(cause instanceof IOException)) {
            logger.error("remote:" + channel.remoteAddress(), cause);
        }
    }
}
