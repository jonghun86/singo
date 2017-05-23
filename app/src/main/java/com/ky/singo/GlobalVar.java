package com.ky.singo;

/**
 * Created by ggungnae on 2017-05-23.
 */

public class GlobalVar {
    private static String id;
    public static String getId() {
        return id;
    }

    public static void setId(String id) {
        GlobalVar.id = id;
    }
}
