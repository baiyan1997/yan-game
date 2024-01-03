package cn.baiyan.net;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;

public class HttpHandler extends SimpleChannelInboundHandler<Object> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof FullHttpRequest) {//如果是HTTP请求，进行HTTP操作
            System.out.println(msg.toString());
        } else if (msg instanceof TextWebSocketFrame) {//如果是Websocket请求，则进行websocket操作
            ctx.fireChannelRead(msg);
        }
    }
}
