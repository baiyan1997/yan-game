package cn.baiyan;

import cn.baiyan.game.PlayerManager;
import cn.baiyan.game.map.ItemManager;
import cn.baiyan.game.map.MapManager;
import cn.baiyan.game.map.ShopManager;
import cn.baiyan.game.map.LoginManager;

import java.util.Arrays;

/**
 * 游戏业务上下文 管理game包下的所有manager
 */
public class GameContext {
    public static PlayerManager playerManager = new PlayerManager();

    public static LoginManager loginManager = new LoginManager();

    public static ShopManager shopManager = new ShopManager();

    public static ItemManager itemManager = new ItemManager();

    public static MapManager mapManager =new MapManager();

    public static void init() {
        Class c = GameContext.class;
        Arrays.stream(c.getDeclaredFields()).forEach(f -> {
            try {
                Object obj = f.getType().newInstance();
                f.set(null, obj);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
