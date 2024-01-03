package cn.baiyan.game;

import cn.baiyan.db.cache.BaseCacheService;
import cn.baiyan.db.utils.DbUtils;
import cn.baiyan.game.module.login.AccountProfile;
import cn.baiyan.game.module.login.PlayerProfile;
import cn.baiyan.game.module.user.PlayerEnt;
import cn.baiyan.logger.LoggerUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerManager extends BaseCacheService<String, PlayerEnt> {

    private Logger logger = LoggerFactory.getLogger(PlayerManager.class);

    /**
     * 所有在线玩家
     */
    private ConcurrentHashMap<String, PlayerEnt> onlines = new ConcurrentHashMap<String, PlayerEnt>();

    /**
     * 全服所有角色简况
     */
    private ConcurrentHashMap<String, PlayerProfile> playerProfiles = new ConcurrentHashMap<>();

    /**
     * 全服所有玩家的简况
     */
    private ConcurrentHashMap<String, AccountProfile> accountProfiles = new ConcurrentHashMap<>();

    public void loadAllPlayerProfiles() {
        String sql = "SELECT * FROM playerent";
        try {
            List<Map<String, Object>> result = DbUtils.queryMapList(DbUtils.DB_USER, sql);
            for (Map<String, Object> record : result) {
                PlayerProfile baseInfo = new PlayerProfile();
                baseInfo.setId((String)record.get("id"));
                baseInfo.setName((String) record.get("name"));
                baseInfo.setPassword((String) record.get("password"));
                addPlayerProfile(baseInfo);
            }
        } catch (SQLException e) {
            LoggerUtils.error("", e);
        }
    }


    public void addPlayerProfile(PlayerProfile PlayerProfile) {
        String accountId = PlayerProfile.getId();
        if (playerProfiles.containsKey(accountId)) {
            throw new RuntimeException("账号重复-->" + accountId);
        }
        PlayerProfile playerProfile = new PlayerProfile();
        playerProfile.setId(accountId);
        playerProfile.setName(PlayerProfile.getName());
        playerProfiles.put(accountId, playerProfile);
    }

    /**
     * 添加进在线列表
     *
     * @param player
     */
    public void add2Online(PlayerEnt player) {
        this.onlines.put(player.getId(), player);
    }

    @Override
    public PlayerEnt load(String playerId) throws Exception {
        String sql = "SELECT * FROM playerent where id = ?";
//		sql = MessageFormat.format(sql, String.valueOf(playerId));
        PlayerEnt player = DbUtils.queryOneById(DbUtils.DB_USER, sql, PlayerEnt.class, playerId);
        if (player != null) {
            player.doAfterInit();
        }
        return player;
    }

    public ConcurrentHashMap<String, PlayerProfile> getPlayerProfiles() {
        return playerProfiles;
    }

    public void setPlayerProfiles(ConcurrentHashMap<String, PlayerProfile> playerProfiles) {
        this.playerProfiles = playerProfiles;
    }
}
