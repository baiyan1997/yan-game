package cn.baiyan.cross.demo;


import cn.baiyan.cross.SCSession;
import cn.baiyan.cross.callback.CallBackCommands;
import cn.baiyan.cross.callback.CallbackHandler;
import cn.baiyan.cross.callback.G2FCallBack;

public class HelloCallbackHandler extends CallbackHandler {

    @Override
    public void onRequest(SCSession session, G2FCallBack req) {
       F2GHeartBeat response = new F2GHeartBeat();
        sendBack(session, req, response);
    }

    @Override
    public int cmdType() {
        return CallBackCommands.HELLO;
    }
}
