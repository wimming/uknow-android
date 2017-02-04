package com.xuewen.utility;

import android.content.Context;

import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

/**
 * Created by huangyuming on 17-2-2.
 */

public class IWXAPIHelper {
    private static final String APP_ID = "wx7fe80e9797f0b6c4";
    private static IWXAPI api;

    public static void regToWx(Context context) {
        api = WXAPIFactory.createWXAPI(context, APP_ID, true);
        api.registerApp(APP_ID);
    }
    public static IWXAPI getApi() {
        return api;
    }
    public static String getAppId() {
        return APP_ID;
    }
}
