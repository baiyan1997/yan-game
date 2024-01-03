package cn.baiyan.client;

import cn.baiyan.ServerConfig;
import cn.baiyan.game.HostAndPort;
import cn.baiyan.game.ServerScanPaths;
import cn.baiyan.message.IdSession;
import cn.baiyan.message.Message;
import cn.baiyan.net.*;
import cn.baiyan.utils.JsonUtils;

/**
 * 客户端启动程序
 */
public class ClientStartUp {
    public static void main(String[] args) throws Exception {
        // 初始化协议池
        MessageFactory.INSTANCE.initMessagePool(ServerScanPaths.MESSAGE_PATH);
        // 读取服务器配置
        ServerConfig.getInstance();

        int serverPort = ServerConfig.getInstance().getServerPort();
        HostAndPort hostAndPort = new HostAndPort();
        hostAndPort.setHost("127.0.0.1");
        hostAndPort.setPort(serverPort);

        SerializerFactory serializerFactory = SerializerHelper.getInstance().getSerializerFactory();
        IMessageDispatcher msgDispatcher =  new IMessageDispatcher() {
            @Override
            public void onSessionCreated(IdSession session) {

            }

            @Override
            public void dispatch(IdSession session, Message message) {
                System.out.println("收到消息<--" + message.getClass() + "=" + JsonUtils.object2String(message));

            }

            @Override
            public void onSessionClosed(IdSession session) {

            }
        };

        RpcClientFactory rpcClientFactory = new RpcClientFactory(msgDispatcher, serializerFactory);
        IdSession session = rpcClientFactory.createSession(hostAndPort);
        ClientPlayer robot = new ClientPlayer(session);
        robot.login();
//        robot.selectedPlayer(10000L);
    }
}
