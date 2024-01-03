package cn.baiyan.cross.demo;


import cn.baiyan.ServerConfig;
import cn.baiyan.cross.CrossTransportManager;
import cn.baiyan.cross.callback.CallBackCommands;
import cn.baiyan.cross.callback.G2FCallBack;
import cn.baiyan.cross.callback.RequestCallback;
import cn.baiyan.game.HostAndPort;
import cn.baiyan.message.Message;
import cn.baiyan.utils.NumberUtil;

public class CrossDemoGameService {
    public static void sayHello() {
        try {
            G2FCallBack req = new G2FCallBack();
            req.addParam("name", "Lily");
            req.setCommand(CallBackCommands.HELLO);

            String matchUrl = ServerConfig.getInstance().getMatchUrl();
            String ip = matchUrl.split(":")[0];
            int port = NumberUtil.intValue(matchUrl.split(":")[1]);
            Message callBack = CrossTransportManager.getInstance().request(HostAndPort.valueOf(ip, port), req);
            System.out.println(callBack);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sayHello2() {
        try {
            G2FCallBack req = new G2FCallBack();
            req.addParam("name", "Lily");
            req.setCommand(CallBackCommands.HELLO);
            String matchUrl = ServerConfig.getInstance().getMatchUrl();
            String ip = matchUrl.split(":")[0];
            int port = NumberUtil.intValue(matchUrl.split(":")[1]);
            CrossTransportManager.getInstance().request(HostAndPort.valueOf(ip, port), req, new RequestCallback() {
                @Override
                public void onSuccess(Message callBack) {
                    System.out.println(callBack);
                }

                @Override
                public void onError(Throwable error) {
                    error.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
