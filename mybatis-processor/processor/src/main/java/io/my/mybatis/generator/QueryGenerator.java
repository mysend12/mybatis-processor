package io.my.mybatis.generator;

import java.util.List;

import io.my.mybatis.model.OrderBy;

public class QueryGenerator {

    private QueryGenerator() {
        throw new IllegalAccessError();
    }

    public static String selectQuery(
        String tableName, 
        String columnName, 
        String fieldName, 
        String orderColumnName, 
        OrderBy order, 
        int limit) {

            if (fieldName == null || columnName == null) {
                return null;
            }

            StringBuilder sb = new StringBuilder().append(selectQuery(tableName, columnName, fieldName));

            if (!orderColumnName.equals("")) {
                selectQuery(sb, orderColumnName, order);
            }

            if (limit > 0) {
                selectQuery(sb, limit);
            }

            return sb.toString();
    }

    public static String selectQuery(String tableName, String columnName, String fieldName) {

        return new StringBuilder().append("SELECT * FROM ")
                                .append(tableName)
                                .append(" WHERE ")
                                .append(columnName)
                                .append("=#{")
                                .append(fieldName)
                                .append("}")
                                .toString()
        ;
    }

    private static StringBuilder selectQuery(
        StringBuilder sb,
        String orderColumnName,
        OrderBy order) {
            return sb.append(" ORDER BY ")
                    .append(orderColumnName)
                    .append(" ")
                    .append(order)
            ;
    }

    private static StringBuilder selectQuery(StringBuilder sb, int limit) {
        return sb.append(" LIMIT ")
                .append(limit)
        ;
    }

    public static String insertQuery(String tableName, List<String> columnList, List<String> fieldList) {
        StringBuilder sb = new StringBuilder();

        if (columnList.isEmpty() || 
            fieldList.isEmpty() || 
            columnList.size() != fieldList.size()) {
            return null;
        } 

        sb.append("INSERT INTO ").append(tableName).append(" (");

        int columnCount = columnList.size() - 1;

        for (int i=0;i<columnCount ;i++) {
            sb.append(columnList.get(i)).append(", ");
        }

        sb.append(columnList.get(columnCount)).append(") ");
        sb.append("VALUES (");

        for (int i=0; i<columnCount; i++) {
            sb.append("#{").append(fieldList.get(i)).append("}").append(", ");
        }

        sb.append("#{").append(fieldList.get(columnCount)).append("}").append(")");

        return sb.toString();
    }

    public static String updateQuery(
        String tableName, 
        List<String> columnList, 
        List<String> fieldList, 
        String conditionField, 
        String conditionColumn) {

        if (columnList.isEmpty() || 
            fieldList.isEmpty() || 
            columnList.size() != fieldList.size()) {
            return null;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE ").append(tableName).append(" SET ");

        int columnCount = columnList.size() - 1;
        
        for (int i=0;i<columnCount; i++) {
            sb.append(columnList.get(i)).append("=").append("#{").append(fieldList.get(i)).append("}, ");
        }

        sb.append(columnList.get(columnCount))
            .append("=")
            .append("#{")
            .append(fieldList.get(columnCount))
            .append("}")
            .append(" WHERE ")
            .append(conditionColumn)
            .append("=")
            .append("#{")
            .append(conditionField)
            .append("}");

        return sb.toString();
    }

    // DELETE FROM test.test WHERE ID = 11
    public static String deleteQuery(String tableName, String columnName, String fieldName) {
        return new StringBuilder().append("DELETE FROM ")
                                .append(tableName)
                                .append(" WHERE ")
                                .append(columnName)
                                .append("=#{")
                                .append(fieldName)
                                .append("}")
                                .toString();
    }
    
}
