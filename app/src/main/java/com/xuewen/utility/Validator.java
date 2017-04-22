package com.xuewen.utility;

/**
 * Created by huangyuming on 17-2-5.
 */

public class Validator {
    public static boolean validateNoEmpty(String ... strings) {
        for (String s : strings) {
            if (s.isEmpty()) {
                return false;
            }
        }
        return true;
    }
}
