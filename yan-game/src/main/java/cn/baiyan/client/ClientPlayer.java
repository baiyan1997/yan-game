package cn.baiyan.client;

import cn.baiyan.game.message.ReqAccountLogin;
import cn.baiyan.message.IdSession;
import com.google.gson.Gson;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.json.JSONObject;

public class ClientPlayer {

    private String name;

    private IdSession session;

    public ClientPlayer(IdSession session){
        this.session = session;
    }

    public void createNew (){

    }

    public void login() {
        ReqAccountLogin request = new ReqAccountLogin();
//        this.sendMessage(request);
    }


    /**
     * 发送消息
     * @param message
     */
    public void sendMessage(JSONObject message) {
        System.err.println(String.format("发送请求-->  %s %s", message.getClass().getSimpleName(), new Gson().toJson(message)));
        this.session.sendPacket(message);
    }

    private class ClientHandler extends IoHandlerAdapter {

        @Override
        public void messageReceived(IoSession session, Object message) {
            System.err.println(String.format("收到响应<--  %s %s", message.getClass().getSimpleName(), new Gson().toJson(message)));
        }

        @Override
        public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
            // TODO 到时候换成log
            System.out.println("exception");
        }
    }

}
