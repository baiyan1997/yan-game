package cn.baiyan.net;

import cn.baiyan.game.HostAndPort;
import cn.baiyan.message.IdSession;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

public class RpcClientFactory {
    private final static Logger logger = LoggerFactory.getLogger(RpcClientFactory.class);
    private IMessageDispatcher messageDispatcher;

    private SerializerFactory messageSerializer;

    private EventLoopGroup group = new NioEventLoopGroup(4);

    public RpcClientFactory(IMessageDispatcher messageDispatcher, SerializerFactory messageSerializer) {
        this.messageDispatcher = messageDispatcher;
        this.messageSerializer = messageSerializer;
    }

    public IdSession createSession(HostAndPort hostAndPort) throws Exception {
        logger.info("准备启动客户端");
        try {
            Bootstrap b = new Bootstrap();
            b.group(group).channel(NioSocketChannel.class).handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel arg0) throws Exception {
                    ChannelPipeline pipeline = arg0.pipeline();
                    pipeline.addLast(new NettyProtocolDecoder());
                    pipeline.addLast(new NettyProtocolEncoder());
                    pipeline.addLast((new MsgIoHandler(messageDispatcher, messageSerializer)));
                }
            });

            ChannelFuture f = b.connect(new InetSocketAddress(hostAndPort.getHost(), hostAndPort.getPort())).sync();
            IdSession session = new NettySession(f.channel());

            return session;
        } catch (Exception e) {
            e.printStackTrace();
            group.shutdownGracefully();
            throw e;
        }
    }
}
