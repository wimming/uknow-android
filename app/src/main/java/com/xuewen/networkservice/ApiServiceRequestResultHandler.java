package com.xuewen.networkservice;

/**
 * Created by Administrator on 2016/12/08.
 */

public interface ApiServiceRequestResultHandler {
    public void onSuccess(Object dataBean);
    public void onError(String errorMessage);

}
