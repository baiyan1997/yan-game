package cn.baiyan.game.map;

import cn.baiyan.GameContext;
import cn.baiyan.db.DbService;
import cn.baiyan.db.cache.BaseCacheService;
import cn.baiyan.db.utils.DbUtils;
import cn.baiyan.game.message.ReqAccountLogin;
import cn.baiyan.game.module.login.ReqLoginInfo;
import cn.baiyan.game.module.user.PlayerEnt;
import cn.baiyan.message.IdSession;
import cn.baiyan.net.HttpUtil;
import cn.baiyan.net.MessagePusher;
import cn.baiyan.net.SessionManager;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class LoginManager extends BaseCacheService<String, PlayerEnt> {

    private static Logger logger = LoggerFactory.getLogger(LoginManager.class);

    public void handleAccountLogin(IdSession session, ReqAccountLogin req) throws SQLException {
        String code = req.getCode();
        String anonymous_code = req.getAnonymousCode();
        String appId = "tt04f6724b9bc449e802";
        String secret = "1e80aa2bb9b7caea9b94c2f09f6c4b57d806db10";
        logger.info("code:" + code + "anoymous_code" + anonymous_code);
        String str = "https://minigame.zijieapi.com/mgplatform/api/apps/jscode2session?appid=" + appId + "&secret=" + secret + "&code=" + code + "&anonymous_code=" + anonymous_code;
        logger.info("url");
        String s = HttpUtil.sendGet(str);
        JSONObject object = new JSONObject(s);
        logger.info("openId:" + s);
        String openId = object.getString("openid");
        logger.info("start login userId:" + openId);
        PlayerEnt playerEnt = GameContext.playerManager.get(openId);
        // 玩家没有账号
        if (playerEnt == null) {
            PlayerEnt player = new PlayerEnt();
            player.setId(openId);
            player.setLastLoginTime(System.currentTimeMillis());
            player.setEnergy(50);
            FirstLoginSendItem(openId);
            DbService.getInstance().insertOrUpdate(player);
            //加入在线列表
            GameContext.playerManager.add2Online(player);
        } else {
            playerEnt.setLastLoginTime(System.currentTimeMillis());
            String sql = "update playerent set lastLoginTime = " + System.currentTimeMillis();
            DbUtils.executeSql(sql);
            //加入在线列表
            GameContext.playerManager.add2Online(playerEnt);
        }

        //绑定session与玩家id
        session.setAttribute(IdSession.ID, openId);
        SessionManager.INSTANCE.registerNewPlayer(openId, session);
        JSONObject jsonObject = new JSONObject();
        // 登录成功  推送协议
        jsonObject.append("loginStatus", 1);
        jsonObject.append("moduleId", 101);
        jsonObject.append("userId", openId);
        MessagePusher.pushMessage(openId, jsonObject);

    }

    public void FirstLoginSendItem(String userId) throws SQLException {
        String sql = "insert into itement values ('" + userId + "',1300,1,1,0,1)," + "('" + userId + "',1301,1,1,0,1)," +  "('" + userId + "',1302,1,1,0,1),"+
                 "('" + userId + "',1000,100, 1,0,1),"+ "('" + userId + "',1100,100,1,0,1)," + "('" + userId + "',1200,100,1,0,1)";
        DbUtils.executeSql(sql);
    }

    public void pushLoginMessage(IdSession session, ReqLoginInfo loginInfo) throws SQLException {
        String userId = loginInfo.getUserId();
        // 图鉴信息
        String sql = "select * from mapent where id = '" + userId + "'";
        List<MapEnt> maps = DbUtils.queryMany(DbUtils.DB_USER, sql, MapEnt.class);
        JSONObject jsonObject = new JSONObject();
        JSONArray mapArray = new JSONArray();
        for (MapEnt map : maps) {
            JSONObject temp = new JSONObject();
            int mapId = map.getMapId();
            String evenGetFish = map.getEvenGetFish();
            temp.put("mapId", mapId);
            temp.put("fish", evenGetFish);
            mapArray.put(temp);
        }
        jsonObject.put("map", mapArray.toString());

        // 背包信息
        String itemSql = "select * from itement where id = '" + userId + "'";
        List<ItemEnt> items = DbUtils.queryMany(DbUtils.DB_USER, itemSql, ItemEnt.class);
        JSONArray itemArray = new JSONArray();
        for (ItemEnt item : items) {
            JSONObject temp = new JSONObject();
            int itemId = item.getItemId();
            int itemCount = item.getCount();
            if (itemId == 1000) {
                jsonObject.put("gold", itemCount);
                continue;
            }
            temp.put("itemId", itemId);
            temp.put("itemCount", itemCount);
            temp.put("level",item.getLevel());
            itemArray.put(temp);
        }
        jsonObject.put("item", itemArray.toString());

        // 鱼的信息
        String fishSql = "select * from fishent where id = '" + userId + "'";
        List<Fish> fishes = DbUtils.queryMany(DbUtils.DB_USER, fishSql, Fish.class);
        JSONArray fishArray = new JSONArray();
        for (Fish fish : fishes) {
            JSONObject temp = new JSONObject();
            int fishId = fish.getFishId();
            temp.put("fishId", fishId);
            temp.put("fishMaxWeight", 0);
            List<Fish> collect = fishes.stream().filter(v -> v.getFishId() == fishId).collect(Collectors.toList());
            for (Fish subFish : collect) {
                int weight = subFish.getWeight();
                if (weight >= temp.getInt("fishMaxWeight")) {
                    temp.put("fishMaxWeight", weight);
                }
            }
            fishArray.put(temp);
        }
        removeDuplicates(fishArray);
        fishArray = delRepeat(fishArray);
        jsonObject.put("fish", fishArray.toString());
        JSONArray fishWeight = new JSONArray();
        for (Fish fish : fishes) {
            JSONObject temp = new JSONObject();
            int fishId = fish.getFishId();
            int weight = fish.getWeight();
            if (fish.getFishStatus() == 1) {
                continue;
            }
            temp.put("fishId", fishId);
            temp.put("fishWeight", weight);
            temp.put("fishStatus", fish.getFishStatus());
            fishWeight.put(temp);
        }
        jsonObject.put("fishInfo", fishWeight.toString());
        jsonObject.put("moduleId", 102);

        PlayerEnt playerEnt = get(userId);
        if (playerEnt == null) {
            return;
        }
        int fishMan = playerEnt.getFishMan();
        jsonObject.put("fishMan", fishMan);
        int rodTime = playerEnt.getRodTime();
        jsonObject.put("rodTime", rodTime);
        int fishTime = playerEnt.getFishTime();
        jsonObject.put("fishTime", fishTime);
        jsonObject.put("energy", playerEnt.getEnergy());
        MessagePusher.pushMessage(userId, jsonObject);
    }


    public static JSONArray removeDuplicates(JSONArray jsonArray) {
        Set<Object> set = new HashSet<>();
        List<Object> list = jsonArray.toList();
        set.addAll(list);
        return new JSONArray(set);
    }


    private static JSONArray delRepeat(JSONArray jsonArray) {
        Set<String> set = new HashSet<>();
        JSONArray uniqueJsonArray = new JSONArray();

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String jsonString = jsonObject.toString();

            if (!set.contains(jsonString)) {
                set.add(jsonString);
                uniqueJsonArray.put(jsonObject);
            }
        }
        return uniqueJsonArray;
    }

    public void reqLoginInfo(IdSession session, ReqLoginInfo req) throws SQLException {
        pushLoginMessage(session, req);
    }

    @Override
    public PlayerEnt load(String id) throws Exception {
        String sql = "SELECT * FROM playerent WHERE id = ? ";
        PlayerEnt playerEnt = DbUtils.queryOneById(DbUtils.DB_USER, sql, PlayerEnt.class, id);
        return playerEnt;
    }
}
