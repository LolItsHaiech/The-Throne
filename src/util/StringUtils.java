package util;

public class StringUtils {
    public static long hash(String str) {
        int res = 7;
        for (int i = 0; i < str.length(); i++) {
            res = res * 31 + str.charAt(i);
        }
        return res;
    }
}
