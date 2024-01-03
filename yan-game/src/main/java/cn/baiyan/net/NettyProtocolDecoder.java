package cn.baiyan.net;

import cn.baiyan.message.Message;
import com.google.gson.JsonArray;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

public class NettyProtocolDecoder extends SimpleChannelInboundHandler<TextWebSocketFrame> {


    Logger logger = LoggerFactory.getLogger(NettyProtocolDecoder.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        String text = msg.text();
        JSONObject jsonObject = new JSONObject(text);
        logger.info("receive info:::::::::::::" + text);
        int module = jsonObject.getInt("module");
        int cmd = jsonObject.getInt("cmd");
        Class<?> msgClazz = MessageFactory.INSTANCE.getMessage((short) module, (byte) cmd);
        try {
            Codec messageCodec = Codec.getSerializer(msgClazz);
            Message message = (Message) messageCodec.decode(jsonObject, msgClazz, ctx.name());
            ctx.fireChannelRead(message);

        } catch (Exception e) {
            logger.error("读取消息出错，模块号{}，类型{}，异常", new Object[]{module, cmd, e});
        }

        ctx.channel().writeAndFlush(new TextWebSocketFrame("服务器时间" + LocalDateTime.now() + " " + msg.text()));
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        //id 表示唯一的值，LongText 是唯一的 ShortText 不是唯一
        System.out.println("handlerAdded 被调用" + ctx.channel().id().asLongText());
        System.out.println("handlerAdded 被调用" + ctx.channel().id().asShortText());
    }


    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        System.out.println("handlerRemoved 被调用" + ctx.channel().id().asLongText());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("异常发生 " + cause.getMessage());
        ctx.close(); //关闭连接
    }

    //    @Override
//    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
//        if (in.readableBytes() < 4) {
//            return;
//        }
//        IMessageDecoder msgDecoder = SerializerHelper.getInstance().getDecoder();
//        in.markReaderIndex();
//        // --------------消息协议格式--------------
//        // package length | moduleId | cmd | body
//        // int short short byte[]
//        int length = in.readInt();
//        if (length > maxReceiveBytes) {
//            logger.error("单包长度[{}]过大，断开链接", length);
//            ctx.close();
//            return;
//        }
//
//        if (in.readableBytes() < length) {
//            in.resetReaderIndex();
//            return;
//        }
//
//        // 消息元信息常量3表示信息body前面的两个字段，一个short表示module，一个byte表示cmd
//        final int metaSize = 3;
//        short moduleId = in.readShort();
//        byte cmd = in.readByte();
//        byte[] body = new byte[length - metaSize];
//        in.readBytes(body);
//        Message msg = msgDecoder.readMessage(moduleId, cmd, body);
//        if (moduleId > 0) {
//            out.add(msg);
//        } else { // 属于组合包
//            CombineMessage combineMessage = (CombineMessage) msg;
//            List<Packet> packets = combineMessage.getPackets();
//            for (Packet packet : packets) {
//                // 依次拆包反序列化为具体的message
//                out.add(Packet.asMessage(packet));
//            }
//
//        }
//    }
}
