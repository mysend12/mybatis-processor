package io.my.mybatis.util;

public class NamingStrategy {
    
    private NamingStrategy() {
        throw new IllegalAccessError();
    }

    public static String camelToSnake(String str) {
        StringBuilder sb = new StringBuilder();
        char[] charArr = firstCharLower(str);
        str.toUpperCase();
        str.toLowerCase();

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
    
}
