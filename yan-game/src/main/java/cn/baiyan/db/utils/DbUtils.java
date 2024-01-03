package cn.baiyan.db.utils;

import cn.baiyan.db.cache.AbstractCacheable;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class DbUtils {
    private static Logger logger = LoggerFactory.getLogger(DbUtils.class);

    /**
     * 策划数据库
     */
    private static final String DB_DATA = "config";

    /**
     * 玩家数据库
     */
    public static final String DB_USER = "user";

    private static HikariDataSource configDataSource;

    private static HikariDataSource userDataSource;

    public static void init() throws Exception {
        InputStream resourceAsStream = DbUtils.class.getClassLoader().getResourceAsStream("jdbc.properties");
        Properties props = new Properties();
        props.load(new LineNumberReader(new InputStreamReader(resourceAsStream)));
        configDataSource = createDataSource(props, DB_DATA);
        userDataSource = createDataSource(props, DB_USER);
    }

    private static HikariDataSource createDataSource(Properties props, String db) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(props.getProperty(db + ".dataSource.jdbc"));
        config.setUsername(props.getProperty(db + ".dataSource.user"));
        config.setPassword(props.getProperty(db + ".dataSource.password"));
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", 250);
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        HikariDataSource ds = new HikariDataSource(config);
        return ds;
    }

    /**
     * 查询返回一个bean实体
     *
     * @param alias 数据库别名
     */
    public static <T> T queryOne(String alias, String sql, Class<?> entity) throws SQLException {
        Connection connection = getConnection(alias);
        return DbHelper.queryOne(connection, sql, entity);
    }

    public static <T> T queryOneById(String alias, String sql, Class<?> entity, String id) throws Exception {
        Connection connection = getConnection(alias);
        return DbHelper.queryOne(connection, sql, entity, id);
    }

    public static <T> T queryOneById(String alias, String sql, Class<?> entity, List<String> ids) throws Exception {
        Connection connection = getConnection(alias);
        return DbHelper.queryOne(connection, sql, entity, ids);
    }

    /**
     * @param alias  数据库别名
     * @param sql
     * @param entity
     * @param <T>
     * @return
     * @throws SQLException
     */
    public static <T> List<T> queryMany(String alias, String sql, Class<?> entity) throws SQLException {
        Connection connection = getConnection(alias);
        return DbHelper.queryMany(connection, sql, entity);
    }

    /**
     * 查询返回 一个Map
     *
     * @param alias
     * @param sql
     * @return
     * @throws SQLException
     */
    public static Map<String, Object> queryMap(String alias, String sql) throws SQLException {
        Connection connection = getConnection(alias);
        return DbHelper.queryMap(connection, sql);
    }


    /**
     * 查询返回一个map
     *
     * @param alias 数据库别名
     * @param sql
     * @return
     */
    public static List<Map<String, Object>> queryMapList(String alias, String sql) throws SQLException {
        Connection connection = getConnection(alias);
        return DbHelper.queryMapList(connection, sql);
    }

    /**
     * 执行特定的sql语句(只有db库有执行权限)
     *
     * @param sql
     * @return
     */
    public static int executeUpdate(String sql) throws SQLException {
        Connection connection = getConnection(DB_USER);
        return DbHelper.executeUpdate(connection, sql);
    }

    public static boolean executeSql(String sql) throws SQLException {
        Connection connection = getConnection(DB_USER);
        return DbHelper.executeSql(connection, sql);
    }

    public static int executePreparedUpdate(AbstractCacheable entity) throws SQLException {
        Connection connection = getConnection(DB_USER);
        return DbHelper.executeUpdate(connection, entity);
    }

    public static int executePreparedInsert(AbstractCacheable entity) throws SQLException {
        Connection connection = getConnection(DB_USER);
        return DbHelper.executeInsert(connection, entity);
    }


    public static Connection getConnection(String alias) {
        try {
            if (DB_DATA.equals(alias)) {
                return configDataSource.getConnection();
            } else if (DB_USER.equals(alias)) {
                return userDataSource.getConnection();
            }
        } catch (Exception e) {
            logger.error("", e);
        }
        return null;
    }


}
