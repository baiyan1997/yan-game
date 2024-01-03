package cn.baiyan.db.utils;

public final class StringUtils {
    public StringUtils() {
    }

    public static boolean isEmpty(String word) {
        return word == null || word.length() <= 0;
    }

    public static boolean isNotEmpty(String word) {
        return !isEmpty(word);
    }

    /**
     * 将单词的第一个字母大写
     */
    public static String firstLetterToUpperCase(String word) {
        StringBuffer sb = new StringBuffer(word);
        sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
        return sb.toString();
    }

    /**
     * 将单次的第一个字母小写
     */
    public static String firstLetterToLowerCase(String word) {
        StringBuilder sb = new StringBuilder(word);
        sb.setCharAt(0, Character.toLowerCase(sb.charAt(0)));
        return sb.toString();
    }
}

