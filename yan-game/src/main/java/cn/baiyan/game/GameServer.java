package cn.baiyan.game;

import cn.baiyan.GameContext;
import cn.baiyan.ServerConfig;
import cn.baiyan.ServerVersion;
import cn.baiyan.cross.CrossServer;
import cn.baiyan.cross.demo.CrossDemoGameService;
import cn.baiyan.db.BaseEntity;
import cn.baiyan.db.DbService;
import cn.baiyan.db.OrmProcessor;
import cn.baiyan.db.orm.SchemaUpdate;
import cn.baiyan.db.utils.DbUtils;
import cn.baiyan.game.json.JsonUtil;
import cn.baiyan.hotswap.Person;
import cn.baiyan.hotswap.Student;
import cn.baiyan.listener.EventDispatcher;
import cn.baiyan.listener.EventType;
import cn.baiyan.listener.ListenerManager;
import cn.baiyan.listener.event.PlayerLoginEvent;
import cn.baiyan.net.MessageFactory;
import cn.baiyan.net.NettySocketServer;
import cn.baiyan.net.ServerNode;
import cn.baiyan.redis.RedisManager;
import cn.baiyan.thread.TimerManager;
import cn.baiyan.utils.ClassScanner;
import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

public class GameServer {

    private Logger logger = LoggerFactory.getLogger(GameServer.class);

    private static GameServer gameServer = new GameServer();

    private ServerNode socketServer;

    private ServerNode httpServer;

    private ServerNode crossServer;

    public static GameServer getInstance() {
        return gameServer;
    }

    public void start() throws Exception {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        frameworkInit();
    }

    private void gameLogicInit() {
        // 游戏启动时，各种业务初始化写在这里吧
        GameContext.playerManager.loadAllPlayerProfiles();
    }


    private void frameworkInit() throws Exception {
        GameContext.init();
        DbUtils.init();
        // 加载服务版本号
        ServerVersion.load();
        // 初始化协议池
        MessageFactory.INSTANCE.initMessagePool(ServerScanPaths.MESSAGE_PATH);

        // 初始化数据库
        RedisManager.init();
        //读取服务器配置
        ServerConfig config = ServerConfig.getInstance();

        // 初始化orm框架
        OrmProcessor.INSTANCE.initOrmBridges(ServerScanPaths.ORM_PATH);
        // 数据库自动更新schema
        Set<Class<?>> codeTables = ClassScanner.listAllSubclasses("cn.baiyan.game", BaseEntity.class);
        new SchemaUpdate().execute(DbUtils.getConnection(DbUtils.DB_USER), codeTables);
        // 事件初始化

        ListenerManager.INSTANCE.init();
        // 游戏启动时，各种业务初始化写在这里吧
        gameLogicInit();
        // 存盘初始化
        DbService.getInstance().init();

        // 读取所有json文件
        JsonUtil.initJsonFile();
        new TimerManager();
//        if (config.getCrossPort() > 0) {
//            crossServer = new CrossServer();
//            crossServer.start();
//        }
        socketServer = new NettySocketServer();
        socketServer.start();
        CrossDemoGameService.sayHello();
    }
}


