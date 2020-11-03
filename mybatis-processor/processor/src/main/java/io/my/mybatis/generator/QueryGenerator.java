package io.my.mybatis.generator;

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
    
}
