package cn.baiyan;

import cn.baiyan.game.GameServer;
import cn.baiyan.net.HttpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class ServerStartUp {

    private Logger logger = LoggerFactory.getLogger(ServerStartUp.class);

    public static void main(String[] args) throws Exception {
//        Map<String, String> params = new HashMap<>();
//        params.put("appid","tt04f6724b9bc449e802");
//        params.put("secret","1e80aa2bb9b7caea9b94c2f09f6c4b57d806db10");
//        params.put("code","0");
//        params.put("anonymous_code","0");
//        String s = HttpUtil.sendPost("GET https://minigame.zijieapi.com/mgplatform/api/apps/jscode2session", params);
        GameServer.getInstance().start();
    }


}
