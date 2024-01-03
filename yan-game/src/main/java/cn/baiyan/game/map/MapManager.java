package cn.baiyan.game.map;

import cn.baiyan.GameContext;
import cn.baiyan.db.DbService;
import cn.baiyan.db.utils.DbUtils;
import cn.baiyan.game.json.JsonUtil;
import cn.baiyan.game.module.login.ReqAdvertisement;
import cn.baiyan.game.module.login.ReqStartGoFish;
import cn.baiyan.game.module.login.ReqStartNextFish;
import cn.baiyan.game.module.user.PlayerEnt;
import cn.baiyan.message.IdSession;
import cn.baiyan.net.MessagePusher;
import org.apache.commons.lang3.RandomUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.*;

public class MapManager {

    private static Logger logger = LoggerFactory.getLogger(LoginManager.class);

    public void startGoFish(IdSession session, ReqStartGoFish req) throws Exception {
        String userId = req.getUserId();
        int mapId = req.getMapId();
        JSONObject result = new JSONObject();
        result.put("moduleId", 104);
        ItemEnt item3 = GameContext.itemManager.getUseItemByType(userId, 3);
        ItemEnt item4 = GameContext.itemManager.getUseItemByType(userId, 4);
        PlayerEnt playerEnt = GameContext.loginManager.get(userId);
        if (playerEnt.getEnergy() <= 0) {
            return;
        }

        JSONObject itemCfg3 = JsonUtil.getItemData(item3.getItemId());
        // 清除道具使用次
        if (item4 != null) {
            String sql = "update itement set useCount = 0 where id = '" + userId + "' and " + "itemId = '" + item4.getItemId() + "'";
            DbUtils.executeSql(sql);
        }


        // 打窩
        JSONObject checkPointData = JsonUtil.getCheckPointData(mapId);
        String fishWeight = checkPointData.getString("fishWeight");

        int itemType1 = itemCfg3.getInt("quality");
        String[] oneFishWeight = fishWeight.split(",");
        Map<Integer, Integer> weight = new HashMap<>();
        for (String s : oneFishWeight) {
            String[] temp = s.split(":");
            int fishId = Integer.parseInt(temp[0]);
            int tempWeight = Integer.parseInt(temp[1]);
            JsonUtil.getFishData(fishId);
            int fishType = JsonUtil.getFishData(fishId).getInt("type1");
            weight.put(fishId, tempWeight);
            if (item4 != null) {
                JSONObject itemCfg4 = JsonUtil.getItemData(item4.getItemId());
                int itemType = itemCfg4.getInt("quality");
                if (fishType == itemType) {
                    tempWeight += itemCfg4.getInt("param") * item4.getUseCount();
                }
            }
            if (fishType == itemType1) {
                tempWeight += itemCfg3.getInt("param");
            }
            weight.put(fishId, tempWeight);
        }

        // 开始钓鱼
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < 5; i++) {
            int fishId = calWeight(weight);
            JSONObject fishData = JsonUtil.getFishData(fishId);
            // 钓到鱼计算重量
            int fishKg = RandomUtils.nextInt(fishData.getInt("minWeight"), fishData.getInt("maxWeight"));
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("fishId", fishId);
            jsonObject.put("weight", fishKg);
            jsonArray.put(jsonObject);
        }

        result.put("fishInfo", jsonArray);
        MessagePusher.pushMessage(userId, result);
    }

    private void calMission1(String userId, JSONObject result) throws SQLException {
        // 增加打窝次数
        PlayerEnt playerEnt = GameContext.loginManager.get(userId);
        // 扣除体力
        int fishTime = playerEnt.getFishTime();
        fishTime += 1;
        String fishTimeSql = "update playerent set fishTime = " + fishTime + " where id = '" + userId + "'";
        playerEnt.setFishTime(fishTime);
        int value = playerEnt.getEnergy() - 5;
        playerEnt.setEnergy(value);
        GameContext.loginManager.save(playerEnt);
        logger.info("sub energy old:" + playerEnt.getEnergy() + ",new" + value);
        String subEnergySql = "update playerent set energy = " + value + " where id = '" + userId + "'";
        DbUtils.executeSql(subEnergySql);
        JSONObject missionData = JsonUtil.getMissionData(1);
        int typeParam = missionData.getInt("typeParam");
        if (fishTime == typeParam) {
            result.put("mission", 1);
            result.put("missionStatus", 1);
        }

        int rodTime = playerEnt.getRodTime();
        rodTime += 1;
        playerEnt.setRodTime(rodTime);
        String rodTimeSql = "update playerent set rodTime = " + rodTime + " where id = '" + userId + "'";
        DbUtils.executeSql(rodTimeSql);
        JSONObject rodData = JsonUtil.getMissionData(2);
        int rodTypeParam = rodData.getInt("typeParam");
        if (fishTime == rodTypeParam) {
            result.put("mission", 2);
            result.put("missionStatus", 1);
        }
        DbUtils.executeSql(fishTimeSql);
    }

    public boolean checkItem(long userId, int itemId) throws Exception {
        String sql = "SELECT * FROM itement where id = ? and itemId = ?";
        List<String> list = new ArrayList<>();
        list.add(String.valueOf(userId));
        list.add(String.valueOf(itemId));
        ItemEnt item = DbUtils.queryOneById(DbUtils.DB_USER, sql, ItemEnt.class, list);
        return item == null;
    }

    private int calWeight(Map<Integer, Integer> weight) {
        int sum = 0;
        for (Map.Entry<Integer, Integer> entry : weight.entrySet()) {
            Integer value = entry.getValue();
            sum += value;
        }
        logger.info("fish weight:" + weight.toString());

        int rand = RandomUtils.nextInt(0, sum);
        for (Map.Entry<Integer, Integer> entry : weight.entrySet()) {
            Integer key = entry.getKey();
            Integer value = entry.getValue();
            if (rand < value) {
                return key;
            } else {
                rand -= value;
            }
        }
        return 0;
    }

    public void startNextFish(IdSession session, ReqStartNextFish req) throws Exception {
        int status = req.getStatus();
        JSONObject result = new JSONObject();
        result.put("moduleId", 105);
        String userId = req.getUserId();
        calMission1(userId, result);
        if (status == 0) {
            result.put("status", 0);
            MessagePusher.pushMessage(req.getUserId(), result);
            return;
        }
        int mapId = req.getMapId();
        int fishId = req.getFishId();
        // 更新图鉴信息
        String sql = "select * from mapent where id = ? and mapId = ?";
        List<String> list = new ArrayList<>();
        list.add(String.valueOf(userId));
        list.add(String.valueOf(mapId));
        MapEnt map = DbUtils.queryOneById(DbUtils.DB_USER, sql, MapEnt.class, list);
        if (map == null) {
            MapEnt mapEnt = new MapEnt();
            mapEnt.setMapId(mapId);
            mapEnt.setUserId(userId);
            mapEnt.setEvenGetFish(String.valueOf(fishId));
            DbService.getInstance().insertOrUpdate(mapEnt);
        } else {
            String last = map.getEvenGetFish() + "," + fishId;
            String sql1 = "update mapent set evenGetFish = '" + last + "' where id = '" + userId + "' and mapId = '" + mapId + "'";
            DbUtils.executeSql(sql1);
        }
        Fish newFish = new Fish();
        newFish.setFishId(fishId);
        newFish.setWeight(req.getWeight());
        newFish.setUserId(userId);
        newFish.setFishStatus(0);
        // 这个鱼是不是更加大
        String fishSql = "select * from fishent where id = '" + userId + "'";
        List<Fish> fishes = DbUtils.queryMany(DbUtils.DB_USER, fishSql, Fish.class);
        int max = 0;
        for (Fish fish : fishes) {
            if (fish.getFishId() == fishId) {
                if (fish.getWeight() > max)
                    max = fish.getWeight();
            }
        }
        if (newFish.getWeight() > max) {
            result.put("fishId", newFish.getFishId());
            result.put("fishWeight", max);
        }
        PlayerEnt playerEnt = GameContext.playerManager.get(userId);
        calMission2(userId, result, playerEnt);
        DbService.getInstance().insertOrUpdate(newFish);
        result.put("status", 1);
        MessagePusher.pushMessage(userId, result);
    }

    private void calMission2(String userId, JSONObject result, PlayerEnt playerEnt) throws SQLException {
        // 任务
        int fishMan = playerEnt.getFishMan();
        fishMan += 1;
        String fishManSql = "update playerent set fishTime = " + fishMan + " where id = '" + userId + "'";
        DbUtils.executeSql(fishManSql);
        JSONObject missionData = JsonUtil.getMissionData(4);
        int typeParam = missionData.getInt("typeParam");
        if (fishMan == typeParam) {
            result.put("mission", 4);
            result.put("missionStatus", 1);
        }
    }

    public void getItemOrGold(IdSession session, ReqAdvertisement req) throws Exception {
        String userId = req.getUserId();
        int type = req.getType();
        PlayerEnt playerEnt = GameContext.loginManager.get(userId);
        if (type == 1) {
            ItemEnt goldInfo = GameContext.itemManager.getGoldInfo(userId);
            ItemEnt item1 = GameContext.itemManager.getItem(userId, 1300);
            ItemEnt item2 = GameContext.itemManager.getItem(userId, 1301);
            ItemEnt item3 = GameContext.itemManager.getItem(userId, 1302);
            int count = (item1.getLevel() + item2.getLevel() + item3.getLevel()) * 2 + 150 + goldInfo.getCount();
            String sql = "update itement set count = " + count + " where id = '" + userId + "' and itemId = '" + goldInfo.getItemId() + "'";
            DbUtils.executeSql(sql);
        }

        if (type == 2) {
            int energy = playerEnt.getEnergy();
            energy = energy + 30;
            String sql = "update playerent set energy = " + energy + " where id = '" + userId + "'";
            DbUtils.executeSql(sql);
            playerEnt.setEnergy(energy);
            GameContext.loginManager.save(playerEnt);
        }
    }
}
