package io.my.mybatis.generator;

import java.util.List;

public class QueryGenerator {

    private QueryGenerator() {
        throw new IllegalAccessError();
    }

    public static String selectQuery(String tableName, String columnName, String fieldName) {
        return new StringBuilder().append("SELECT * FROM ")
                                .append(tableName)
                                .append(" WHERE ")
                                .append(columnName)
                                .append("=#{")
                                .append(fieldName)
                                .append("}")
                                .toString();
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
    
}
