package com.xuewen.networkservice;

import java.util.List;

/**
 * Created by ym on 16-11-4.
 */

public class Contributor {

    String login;

    boolean site_admin;

    int contributions;

    @Override
    public String toString() {
        return login + " (" + contributions + ")" + site_admin + "/";
    }
}