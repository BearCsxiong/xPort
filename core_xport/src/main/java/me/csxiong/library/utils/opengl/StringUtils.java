package me.csxiong.library.utils.opengl;

public class StringUtils {

    public static String getSymbolNumber(int number) {
        if (number > 0) {
            return "+" + number;
        } else {
            return String.valueOf(number);
        }
    }

    public static String getSymbolNumberNoZero(int number) {
        if (number == 0) {
            return "";
        }
        return getSymbolNumber(number);
    }
}
