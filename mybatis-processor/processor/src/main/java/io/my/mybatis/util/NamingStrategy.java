package io.my.mybatis.util;

import javax.lang.model.element.Element;

import io.my.mybatis.annotation.field.ColumnName;

public class NamingStrategy {
    
    private NamingStrategy() {
        throw new IllegalAccessError();
    }

    public static String columnName(Element e) {
        if (e == null) {
            return null;
        }
        return columnName(e, e.toString());
    }

    public static String columnName(Element e, String field) {
        if (e == null) {
            return null;
        }
        return columnName(e.getAnnotation(ColumnName.class), field);
    }

    public static String columnName(ColumnName column, String field) {
        if (column != null ) {
            return column.columnName();
        } else if (field == null) {
            return null;
        }

        return NamingStrategy.camelToSnake(field);
    }

    public static String camelToSnake(String str) {
        StringBuilder sb = new StringBuilder();
        char[] charArr = firstCharLower(str);

        for (char c : charArr) {
            if (c >= 'A' && c <= 'Z') {
                sb.append("_").append((char)(c + 32));
            } else {
                sb.append(c);
            }
        }

        return sb.toString();
    }

    public static char[] firstCharLower(String str) {
        return firstCharLower(str.toCharArray());
    }

    public static char[] firstCharLower(char[] charArr) {
        charArr[0] = charLower(charArr[0]);
        return charArr;
    }

    public static char charLower(char c) {
        if (c >= 'A' && c <= 'Z') {
            return (char) (c + 32);
        }
        return c;
    }

    public static char[] firstCharUpper(String str) {
        return firstCharUpper(str.toCharArray());
    }

    public static char[] firstCharUpper(char[] charArr) {
        charArr[0] = charUpper(charArr[0]);
        return charArr;
    }

    public static char charUpper(char c) {
        if (c >= 'a' && c <= 'z') {
            return (char) (c-32);
        }
        return c;
    }

    public static String snakeToCamel(String str) {
        StringBuilder sb = new StringBuilder();
        char[] charArr = firstCharLower(str);

        boolean isUp = false;
        for (int i=0; i<charArr.length; i++) {
            if (charArr[i] == '_') {
                isUp = true;
                continue;
            }

            if (isUp) {
                sb.append(charUpper(charArr[i]));
                isUp = false;
            } else {
                sb.append(charLower(charArr[i]));
            }
        }

        return sb.toString();
    }
    
}
