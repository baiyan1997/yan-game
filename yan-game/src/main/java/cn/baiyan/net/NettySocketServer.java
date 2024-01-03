package cn.baiyan.net;

import cn.baiyan.ServerConfig;
import cn.baiyan.game.ServerScanPaths;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NettySocketServer implements ServerNode {
    private Logger logger = LoggerFactory.getLogger(NettySocketServer.class);

    // 避免使用默认线程池参数
    private EventLoopGroup bossGroup = new NioEventLoopGroup(4);
    private EventLoopGroup workerGroup = new NioEventLoopGroup();

    @Override
    public void start() throws Exception {
        int serverPort = ServerConfig.getInstance().getServerPort();
        logger.info("netty socket 服务已经启动，正在监听用户的请求@port:" + serverPort + ".......");
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class).
                    childHandler(new ChildChannelHandler());
            ChannelFuture channelFuture = b.bind(serverPort).sync();
            // 监听关闭
            channelFuture.channel().closeFuture().sync();
        } catch (Exception e) {
            logger.error("服务器启动失败");
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
            throw e;
        }
    }

    @Override
    public void shutdown() {

    }

    private class ChildChannelHandler extends ChannelInitializer<SocketChannel> {

        @Override
        protected void initChannel(SocketChannel socketChannel) throws Exception {
            ChannelPipeline pipeline = socketChannel.pipeline();
            pipeline.addLast(new HttpServerCodec());
            pipeline.addLast(new ChunkedWriteHandler());
            pipeline.addLast(new HttpObjectAggregator(8192));
            pipeline.addLast(new WebSocketServerProtocolHandler("/hello2"));

//            pipeline.addLast(new NettyProtocolDecoder());
            pipeline.addLast(new IOEventHandler(new MessageDispatcher(ServerScanPaths.MESSAGE_PATH)));
//            pipeline.addLast(new NettyProtocolEncoder());
        }
    }
}
