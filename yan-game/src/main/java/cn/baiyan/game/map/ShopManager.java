package cn.baiyan.game.map;

import cn.baiyan.GameContext;
import cn.baiyan.db.DbService;
import cn.baiyan.db.utils.DbUtils;
import cn.baiyan.game.json.JsonUtil;
import cn.baiyan.game.module.login.ReqBuyItem;
import cn.baiyan.game.module.login.ReqSellFish;
import cn.baiyan.net.MessagePusher;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class ShopManager {

    private static Logger logger = LoggerFactory.getLogger(ShopManager.class);

    public void buy(String userId, ReqBuyItem req) throws Exception {
        int itemId = req.getItemId();
        int count = req.getCount();
        JSONObject result = new JSONObject();
        result.put("moduleId", 103);
        result.put("status", 0);
        if (count <= 0) {
            return;
        }

        // 读表查询要花多少钱
        JSONObject itemData = JsonUtil.getItemData(itemId);
        int price = itemData.getInt("price");
        int cost = price * count;
        ItemEnt goldInfo = GameContext.itemManager.getGoldInfo(userId);
        int haveCount = goldInfo.getCount();
        if (haveCount < cost) {
            MessagePusher.pushMessage(userId, result);
            return;
        }
        int value = haveCount - cost;
        // 扣除道具
        GameContext.itemManager.subGold(userId, value);

        // 增加道具
        ItemEnt itemInfo = GameContext.itemManager.selectItem(userId, itemId);
        if (itemInfo == null) {
            String sql2 = "insert into itement values ('" + userId + "','" + itemId + "'," + count + ",0,0,1)";
            DbUtils.executeSql(sql2);
        } else {
            count = itemInfo.getCount() + 1;
            String sql2 = "update itement set count = " + count + " where id = '" + userId + "' and itemId = '" + itemId + "'";
            DbUtils.executeSql(sql2);
        }
        logger.info("buy item userId:" + userId + "itemId:" + itemId + "count:" + count);
    }

    public void sell(String userId, ReqSellFish reqSellFish) throws Exception {
        int fishId = reqSellFish.getFishId();
        String sql = "select * from fishent where id = ? and fishId = ? and weight = ?";
        List<String> list = new ArrayList<>();
        list.add(String.valueOf(userId));
        list.add(String.valueOf(fishId));
        list.add(String.valueOf(reqSellFish.getWeight()));
        Fish fish = DbUtils.queryOneById(DbUtils.DB_USER, sql, Fish.class, list);
        if (fish == null) {
            return;
        }
        // TODO 卖的鱼在卖有问题

        // 扣掉鱼
        String sql1 = "update fishent set fishStatus = 1 where id = '" + userId + "' and fishId = '" + fishId + "' and weight = " + fish.getWeight();
        DbUtils.executeSql(sql1);

        // 计算能够获得多少金币
        JSONObject jsonObject = JsonUtil.getFishData(fishId);
        int price = jsonObject.getInt("priceParam");
        int getGodNum = price * fish.getWeight();

        // 获得道具
        ItemEnt goldInfo = GameContext.itemManager.getGoldInfo(userId);
        int count = goldInfo.getCount() + getGodNum;
        count = goldInfo.getCount() + count;
        String sql2 = "update itement set count = " + count + " where id = '" + userId + "' and " + " itemId = '1000'";
        DbUtils.executeSql(sql2);
        logger.info("sell item userId:" + userId + "fishId:" + fishId + "weight:" + fish.getWeight());

    }

}
