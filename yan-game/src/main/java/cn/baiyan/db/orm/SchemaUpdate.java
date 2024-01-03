package cn.baiyan.db.orm;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

public class SchemaUpdate {
    public void execute(Connection connection, Set<Class<?>> codeTables) throws SQLException {
        TableConfiguration tableConfiguration = new TableConfiguration();
        tableConfiguration.register(codeTables);

        DatabaseMetadata databaseMetadata = new DatabaseMetadata(connection);
        List<String> tables = databaseMetadata.getTables(connection);
        for (String table : tables) {
            if (table.endsWith("ent")) {
                databaseMetadata.getTableMetadata(table);
            }
        }

        List<String> sqls = createDdlSqls(tableConfiguration.getTables(), databaseMetadata.getTables());
        for (String sql : sqls) {
            connection.createStatement().execute(sql);
            System.out.println("执行schema --> " + sql);
        }

    }

    /**
     * 生产ddl的sql语句列表
     *
     * @param tablesDef      代码的表定义
     * @param tablesMetadata 数据库的表schema
     * @return
     */
    public List<String> createDdlSqls(Map<String, TableDefinition> tablesDef, Map<String, TableMetadata> tablesMetadata) {
        List<String> result = new ArrayList<>();
        for (Map.Entry<String, TableDefinition> entry : tablesDef.entrySet()) {
            TableDefinition tableDef = entry.getValue();
            String tableName = entry.getKey();
            if (!tablesMetadata.containsKey(tableName)) {
                // 数据库不存在，建表
                result.add(tableDef.sqlCreateString());
            } else {
                // 数据库已存在，查看是否有字段新增,不处理删除的字段
                Iterator<String> it = tableDef.sqlAlterStrings(tablesMetadata.get(tableName));
                while (it.hasNext()) {
                    result.add(it.next());
                }
            }
        }

        return result;
    }
}
