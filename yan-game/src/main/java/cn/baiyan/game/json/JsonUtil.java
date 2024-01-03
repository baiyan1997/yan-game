package cn.baiyan.game.json;

import cn.baiyan.GameContext;
import cn.baiyan.ServerConfig;
import cn.baiyan.db.DbService;
import cn.baiyan.db.cache.AbstractCacheContainer;
import cn.baiyan.db.utils.DbUtils;
import cn.baiyan.db.utils.SqlUtils;
import cn.baiyan.game.GameServer;
import cn.baiyan.game.PlayerManager;
import cn.baiyan.game.map.ItemEnt;
import cn.baiyan.game.map.MapEnt;
import cn.baiyan.game.module.login.PlayerProfile;
import cn.baiyan.game.module.login.ReqBuyItem;
import cn.baiyan.game.module.user.PlayerEnt;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class JsonUtil {

    public static Logger logger = LoggerFactory.getLogger(JsonUtil.class);

    public static Map<String, Map<Integer, JSONObject>> data = new HashMap<>();

    public static void initJsonFile() throws Exception {
        InputStream inputStream = JsonUtil.class.getClassLoader().getResourceAsStream("data.json");
        String jsonString = IOUtils.toString(inputStream, "UTF-8");
        JSONObject jsonObject = new JSONObject(jsonString);
        JSONArray jsonArray1 = jsonObject.getJSONArray("checkPoint");
        data.put("checkPoint", new HashMap<>());
        JSONArray jsonArray2 = jsonObject.getJSONArray("item");
        data.put("item", new HashMap<>());
        JSONArray jsonArray3 = jsonObject.getJSONArray("mission");
        data.put("mission", new HashMap<>());
        JSONArray jsonArray4 = jsonObject.getJSONArray("fish");
        data.put("fish", new HashMap<>());
        JSONArray jsonArray5 = jsonObject.getJSONArray("change");
        data.put("change", new HashMap<>());
        JSONArray jsonArray6 = jsonObject.getJSONArray("itemLv");
        data.put("itemLv", new HashMap<>());
        for (int i = 0; i < jsonArray1.length(); i++) {
            JSONObject temp = jsonArray1.getJSONObject(i);
            data.get("checkPoint").put(temp.getInt("id"), temp);
        }
        for (int i = 0; i < jsonArray2.length(); i++) {
            JSONObject temp = jsonArray2.getJSONObject(i);
            data.get("item").put(temp.getInt("id"), temp);
        }
        for (int i = 0; i < jsonArray3.length(); i++) {
            JSONObject temp = jsonArray3.getJSONObject(i);
            data.get("mission").put(temp.getInt("id"), temp);
        }
        for (int i = 0; i < jsonArray4.length(); i++) {
            JSONObject temp = jsonArray4.getJSONObject(i);
            data.get("fish").put(temp.getInt("id"), temp);
        }

        for (int i = 0; i < jsonArray5.length(); i++) {
            JSONObject temp = jsonArray5.getJSONObject(i);
            data.get("change").put(temp.getInt("id"), temp);
        }

        for (int i = 0; i < jsonArray6.length(); i++) {
            JSONObject temp = jsonArray6.getJSONObject(i);
            data.get("itemLv").put(temp.getInt("id"), temp);
        }
        test();
    }

    public static void test() throws Exception {
//        GameContext.loginManager.FirstLoginSendItem(1L);

//        AbstractCacheContainer<Long, PlayerEnt> container = GameContext.playerManager.getContainer();
//        PlayerEnt playerEnt = GameContext.playerManager.get(1L);
//        playerEnt.setEnergy(9000);
//        DbService.getInstance().saveColumns(playerEnt,"energy");
//        DbService.getInstance().insertOrUpdate(playerEnt);
//        ReqBuyItem item = new ReqBuyItem();
//        item.setItemId(1001);
//        item.setUserId(1);
//        item.setCount(100);
//        GameContext.shopManager.buy(1,item);


//        String sql = "select * from playerent where id  = ? ";
    }


//    public static void initJsonFile() throws IOException {
//        URL resource = DbUtils.class.getClassLoader().getResource("./json");
//        String name = resource.getFile();
//        File dir = new File(name);
//        if (dir.isDirectory()) {
//            File[] files = dir.listFiles();
//            for (File file : files) {
//                String content = FileUtils.readFileToString(file, "UTF-8");
//                JSONObject jsonObject = new JSONObject(content);
//                data.put(file.getName(), jsonObject);
//            }
//        }
//    }

    public static JSONObject getCheckPointData(int key) {
        return data.get("checkPoint").get(key);
    }

    public static JSONObject getItemData(int key) {
        return data.get("item").get(key);
    }

    public static JSONObject getMissionData(int key) {
        return data.get("mission").get(key);
    }

    public static JSONObject getFishData(int key) {
        return data.get("fish").get(key);
    }

    public static JSONObject getItemLvData(int key) {
        return data.get("itemLv").get(key);
    }

    public static JSONObject getChange(String key) {
        Map<Integer, JSONObject> change = data.get("change");
        for (Map.Entry<Integer, JSONObject> entry : change.entrySet()) {
            Integer key1 = entry.getKey();
            JSONObject value = entry.getValue();
            if (key.equals(value.getString("key"))) {
                return value;
            }
        }
        return null;
    }
}
