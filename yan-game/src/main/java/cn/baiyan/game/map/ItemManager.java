package cn.baiyan.game.map;

import cn.baiyan.GameContext;
import cn.baiyan.db.DbService;
import cn.baiyan.db.utils.DbUtils;
import cn.baiyan.game.json.JsonUtil;
import cn.baiyan.game.module.login.ReqChange;
import cn.baiyan.game.module.login.ReqUpGradeItem;
import cn.baiyan.game.module.login.ReqUseItem;
import cn.baiyan.message.IdSession;
import cn.baiyan.net.MessagePusher;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ItemManager {

    private static Logger logger = LoggerFactory.getLogger(ItemManager.class);

    public ItemEnt getGoldInfo(String userId) throws Exception {
        String sql = "SELECT * FROM itement where id = ? and itemId = '1000'";
        return DbUtils.queryOneById(DbUtils.DB_USER, sql, ItemEnt.class, userId);
    }

    public void useItem(String userId, ReqUseItem req) throws Exception {
        int itemId = req.getItemId();
        ItemEnt item = selectItem(userId, itemId);
        JSONObject response = new JSONObject();
        response.put("moduleId", 106);
        if (item == null) {
            return;
        }

        int count = item.getCount() - 1;
        // 最后一次 改下状态
        if (count == 0) {
            String sql3 = "delete from itement where id = '" + userId + "' and " + "itemId = '" + itemId + "'";
            DbUtils.executeSql(sql3);
        }

        JSONObject itemData = JsonUtil.getItemData(itemId);
        int type = itemData.getInt("type1");
        String sql2 = null;
        if (type == 4) {
            int useCount = item.getUseCount() + 1;
            sql2 = "update itement set itemStatus = 1,count = " + count + ",useCount = " + useCount + " where id = '" + userId + "' and " + "itemId = '" + itemId + "'";
        } else {
            sql2 = "update itement set itemStatus = 1,count = " + count + " where id = '" + userId + "' and " + "itemId = '" + itemId + "'";
        }
        DbUtils.executeSql(sql2);
        response.put("status", 1);
        response.put("itemId", itemId);
        response.put("count", count);
        logger.info("use item userId:" + userId + "itemId:" + itemId);
        MessagePusher.pushMessage(userId, response);
    }

    public ItemEnt selectItem(String userId, int itemId) throws Exception {
        String sql = "SELECT * FROM itement where id = ? and itemId = ?";
        List<String> list = new ArrayList<>();
        list.add(userId);
        list.add(String.valueOf(itemId));
        return DbUtils.queryOneById(DbUtils.DB_USER, sql, ItemEnt.class, list);
    }

    public ItemEnt getUseItemByType(String userId, int type) throws SQLException {
        String sql = "select * from itement where id = '" + userId + "' and itemStatus = 1";
        List<ItemEnt> items = DbUtils.queryMany(DbUtils.DB_USER, sql, ItemEnt.class);
        for (ItemEnt item : items) {
            if (item.getItemId() == 1300) {
                continue;
            }
            if (item.getItemId() == 1301) {
                continue;
            }
            if (item.getItemId() == 1302) {
                continue;
            }
            if(item.getItemId() == 1000){
                continue;
            }
            JSONObject itemData = JsonUtil.getItemData(item.getItemId());
            if (itemData.getInt("type1") == type) {
                return item;
            }
        }
        return null;
    }

    public ItemEnt getItem(String userId, int itemId) throws Exception {
        String sql = "select * from itement where id = ? and itemId = ?";
        List<String> list = new ArrayList<>();
        list.add(userId);
        list.add(String.valueOf(itemId));
        return DbUtils.queryOneById(DbUtils.DB_USER, sql, ItemEnt.class, list);
    }

    public void reqChange(IdSession session, ReqChange req) throws Exception {
        String userId = req.getUserId();
        String content = req.getContent();
        JSONObject change = JsonUtil.getChange(content);
        if (change == null) {
            return;
        }

        String sql = "select * from exchangent where id = '" + userId + "' and content = '" + content +"'";
        List<String> list = new ArrayList<>();
        list.add(String.valueOf(userId));
        list.add(content);
        Exchange exchange = DbUtils.queryOneById(DbUtils.DB_USER, sql, Exchange.class, list);
        if (exchange != null) {
            return;
        } else {
            Exchange data = new Exchange();
            data.setId(userId);
            data.setContent(content);
            DbService.getInstance().insertOrUpdate(data);
        }
        int itemId = change.getInt("itemId");
        int num = change.getInt("num");
        ItemEnt itemEnt = selectItem(userId, itemId);
        JSONObject result = new JSONObject();
        result.put("moduleId", 108);
        result.put("itemId", itemId);
        int itemCount = 0;
        if (itemEnt == null) {
            ItemEnt item = new ItemEnt();
            item.setId(userId);
            item.setItemId(itemId);
            item.setCount(num);
            item.setItemStatus(0);
            itemCount = num;
            DbService.getInstance().insertOrUpdate(item);
        } else {
            itemCount = itemEnt.getCount() + num;
            String sql2 = "update itement set count = " + itemCount + " where id = '" + userId + "' and itemId = '" + itemId +"'";
            DbUtils.executeSql(sql2);
        }
        result.put("count", itemCount);
        MessagePusher.pushMessage(userId, result);
    }

    public void upGradeItem(IdSession session, ReqUpGradeItem req) throws Exception {
        int itemId = req.getItemId();
        String userId = req.getUserId();
        ItemEnt item = GameContext.itemManager.getItem(userId, itemId);
        // 没有这个道具
        if (item == null) {
            return;
        }
        int level = item.getLevel();
        level += 1;
        String dataId = itemId + "" + level;
        JSONObject itemLvData = JsonUtil.getItemLvData(Integer.parseInt(dataId));
        int cost = itemLvData.getInt("cost");
        ItemEnt goldInfo = getGoldInfo(userId);
        int haveCount = goldInfo.getCount();
        if (haveCount < cost) {
            return;
        }

        int value = haveCount - cost;
        // 扣除道具
        subGold(userId, value);
        //  升级
        String sql = "update itement set level = " + level + " where id = '" + userId + "' and itemId = '" + itemId + "'";
        DbUtils.executeSql(sql);
    }

    public void subGold(String userId, int value) throws SQLException {
        String sql = "update itement set count = " + value + " where id = '" + userId + "' and itemId = '1000'";
        DbUtils.executeSql(sql);
    }
}
