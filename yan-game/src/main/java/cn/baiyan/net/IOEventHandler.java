package cn.baiyan.net;

import cn.baiyan.message.IdSession;
import cn.baiyan.message.Message;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class IOEventHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    private final static Logger logger = LoggerFactory.getLogger(IOEventHandler.class);

    /**
     * 消息分发器
     */
    private IMessageDispatcher messageDispatcher;

    public IOEventHandler(IMessageDispatcher messageDispatcher) {
        super();
        this.messageDispatcher = messageDispatcher;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        //添加到channelGroup通道组
        MyChannelHandlerPool.channelGroup.add(ctx.channel());
        if (!ChannelUtils.addChannelSession(channel, new NettySession(channel))) {
            channel.close();
            logger.error("Duplicate session, IP=[{}]", ChannelUtils.getIp(channel));
        }
        IdSession userSession = ChannelUtils.getSessionBy(channel);
        messageDispatcher.onSessionCreated(userSession);
    }

//    private void sendOneMessage(String uid, String message){
//        //收到信息后，单发给channel
////        List<Channel> channelList = getChannelByName(uid);
//        channelList.forEach(channel -> channel.writeAndFlush(new TextWebSocketFrame(message)));
//    }

//    /**
//     * 根据用户id查找channel
//     *
//     * @param name
//     * @return
//     */
//    public List<Channel> getChannelByName(String name) {
//        return MyChannelHandlerPool.channelGroup.stream().filter(channel -> channel.attr(key).get().equals(name))
//                .collect(Collectors.toList());
//    }
//
//    @Override
//    public void channelRead(ChannelHandlerContext context, Object msg) throws Exception {
//        logger.info("receive info baiyan{}", msg);
//        Message packet = (Message) msg;
//        logger.info("receive pact, content is {}", packet.getClass().getSimpleName());
//
//    }


    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        //添加到channelGroup 通道组
        MyChannelHandlerPool.channelGroup.remove(ctx.channel());
        IdSession userSession = ChannelUtils.getSessionBy(channel);
        messageDispatcher.onSessionClosed(userSession);
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

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        String text = msg.text();
        JSONObject jsonObject = new JSONObject(text);
        logger.info("receive info:::::::::::::" + text);
        int module = jsonObject.getInt("module");
        int cmd = jsonObject.getInt("cmd");
        Class<?> msgClazz = MessageFactory.INSTANCE.getMessage((short) module, (byte) cmd);
        Codec messageCodec = Codec.getSerializer(msgClazz);
        Message message = (Message) messageCodec.decode(jsonObject, msgClazz, ctx.name());
        final Channel channel = ctx.channel();
        IdSession session = ChannelUtils.getSessionBy(channel);
        messageDispatcher.dispatch(session, message);
    }
}
