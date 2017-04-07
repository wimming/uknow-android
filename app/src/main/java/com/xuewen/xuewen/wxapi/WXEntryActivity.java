package com.xuewen.xuewen.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.xuewen.networkservice.ApiService;
import com.xuewen.networkservice.WXLoginResult;
import com.xuewen.utility.CurrentUser;
import com.xuewen.utility.IWXAPIHelper;
import com.xuewen.utility.ToastMsg;
import com.xuewen.xuewen.LoginActivity;
import com.xuewen.xuewen.PerfectActivity;
import com.xuewen.xuewen.MainActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by huangyuming on 17-2-2.
 */

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        IWXAPIHelper.getApi().handleIntent(getIntent(), this);
    }

    @Override
    public void onReq(BaseReq baseReq) {
        Toast.makeText(this, "hhh", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResp(BaseResp baseResp) {
        if (((SendAuth.Resp)baseResp).errCode != 0) {
            Toast.makeText(this, "授权失败", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(WXEntryActivity.this, LoginActivity.class));
            return;
        }

        String code = ((SendAuth.Resp)baseResp).code;
        String appid = IWXAPIHelper.getAppId();

        ApiService apiService = ApiService.retrofit.create(ApiService.class);

        Call<WXLoginResult> call = apiService.requestWXLogin(appid, code);
        call.enqueue(new Callback<WXLoginResult>() {
            @Override
            public void onResponse(Call<WXLoginResult> call, Response<WXLoginResult> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(WXEntryActivity.this, ToastMsg.SERVER_ERROR, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (response.body().status != 200) {
                    Toast.makeText(WXEntryActivity.this, response.body().errmsg, Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(WXEntryActivity.this, LoginActivity.class));
                    return;
                }

                CurrentUser.cookie = response.headers().get("Set-Cookie");
                CurrentUser.userId = response.body().data.user_id;

                if (response.body().data.token != null && !response.body().data.token.isEmpty()) {
                    SharedPreferences.Editor editor = getSharedPreferences("login_info", MODE_PRIVATE).edit();
                    editor.putInt("user_id", response.body().data.user_id);
                    editor.putString("token", response.body().data.token);
                    editor.commit();
                }

                if (response.body().data.isNew == 0) {
                    Intent intent = new Intent(WXEntryActivity.this, MainActivity.class);
                    startActivity(intent);
                }
                else {
                    Intent intent = new Intent(WXEntryActivity.this, PerfectActivity.class);
                    startActivity(intent);
                }

            }
            @Override
            public void onFailure(Call<WXLoginResult> call, Throwable t) {
                Toast.makeText(WXEntryActivity.this, ToastMsg.NETWORK_ERROR, Toast.LENGTH_SHORT).show();
            }
        });

    }
}
