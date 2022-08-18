package utils;

public class Utils {

    public static boolean isNumber(String s) {
        //loop through the input String's characters
        for (int i = 0; i < s.length(); i++) {

            if (!Character.isDigit(s.charAt(i))) return false;

        }
        //returns true only if none of the String's characters are digits
        return true;
    }

}
