package cn.baiyan.db.orm;


import java.util.*;

public class TableDefinition {

    private String tableName;

    private Map<String, ColumnDefinition> columns = new LinkedHashMap<>();

    private String comment;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public void addColumn(ColumnDefinition column) {
        columns.put(column.getName(), column);
    }

    public String sqlCreateString() {
        StringBuilder sb = new StringBuilder("CREATE TABLE").append(' ').append(tableName).append(" (");
        Iterator<ColumnDefinition> iterator = columns.values().stream().iterator();
        while (iterator.hasNext()) {
            ColumnDefinition col = iterator.next();
            sb.append(col.getName()).append(' ');
            sb.append(col.getJdbcType());
            String defaultValue = col.getDefaultValue();
            if (defaultValue != null) {
                sb.append(" default ").append(defaultValue);
            }
            if (col.isNullable()) {
                sb.append(" ");
            } else {
                sb.append(" not null");
            }
            if (col.isPrimary()) {
                sb.append("PRIMARY KEY");
            }
            String columnComment = col.getComment();
            if (columnComment != null) {
                sb.append(columnComment);
            }
            if (iterator.hasNext()) {
                sb.append(", ");
            }
        }
        sb.append(")");
        if (comment != null) {
            sb.append(comment);
        }

        return sb.toString();
    }

    public Iterator<String> sqlAlterStrings(TableMetadata tableMetadata) {
        StringBuilder root = new StringBuilder("alter table " + tableName).append(' ');
        Iterator<ColumnDefinition> iterator = columns.values().iterator();
        List<String> results = new ArrayList<>();
        while (iterator.hasNext()) {
            final ColumnDefinition column = iterator.next();
            ColumnMetadata columnInfo = tableMetadata.getColumnMetadata(column.getName());
            if (columnInfo == null) {
                StringBuilder alter = new StringBuilder(root.toString())
                        .append(" add column ")
                        .append(column.getName())
                        .append(' ')
                        .append(column.getJdbcType());
                String defaultValue = column.getDefaultValue();
                if (defaultValue != null) {
                    alter.append(" default ").append(defaultValue);
                }

                if (column.isNullable()) {
                    alter.append(" ");
                } else {
                    alter.append(" not null");
                }

                if (column.isPrimary()) {
                    alter.append(" PRIMARY KEY ");
                }

                String columnComment = column.getComment();
                if (columnComment != null) {
                    alter.append(column.getComment());
                }

                results.add(alter.toString());
            }
        }
        return results.iterator();

    }
}
