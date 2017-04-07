package com.xuewen.xuewen;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.tbruyelle.rxpermissions.RxPermissions;
import com.xuewen.networkservice.ApiService;
import com.xuewen.networkservice.TKLoginResult;
import com.xuewen.utility.CurrentUser;
import com.xuewen.utility.ToastMsg;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.functions.Action1;

/**
 * Created by huangyuming on 17-2-6.
 */

public class EntryActivity extends AppCompatActivity {

    Handler mHandler = new Handler();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        RxPermissions rxPermissions = new RxPermissions(EntryActivity.this);
        rxPermissions.request(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.READ_CONTACTS)
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean granted) {
                        if (granted) {
//                            Toast.makeText(EntryActivity.this, ToastMsg.AUTHENTICATION_SUCCESS, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(EntryActivity.this, ToastMsg.AUTHENTICATION_FAILED, Toast.LENGTH_SHORT).show();
                            mHandler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    finish();
                                }
                            }, 3000);
                        }
                    }
                });

        String token = getSharedPreferences("login_info", MODE_PRIVATE).getString("token", "");
        final int user_id = getSharedPreferences("login_info", MODE_PRIVATE).getInt("user_id", -1);

        if (token.isEmpty() || user_id == -1) {
            startActivity(new Intent(EntryActivity.this, LoginActivity.class));
        }
        else {
            ApiService apiService = ApiService.retrofit.create(ApiService.class);

            Call<TKLoginResult> call = apiService.requestTKLogin(user_id, token);
            call.enqueue(new Callback<TKLoginResult>() {
                @Override
                public void onResponse(Call<TKLoginResult> call, Response<TKLoginResult> response) {
                    if (!response.isSuccessful()) {
                        Toast.makeText(EntryActivity.this, ToastMsg.SERVER_ERROR, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (response.body().status != 200) {
                        Toast.makeText(EntryActivity.this, response.body().errmsg, Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(EntryActivity.this, LoginActivity.class));
                        return;
                    }

                    CurrentUser.cookie = response.headers().get("Set-Cookie");
                    CurrentUser.userId = user_id;

                    if (response.body().data.isNew == 0) {
                        Intent intent = new Intent(EntryActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                    else {
                        Intent intent = new Intent(EntryActivity.this, PerfectActivity.class);
                        startActivity(intent);
                    }

                }
                @Override
                public void onFailure(Call<TKLoginResult> call, Throwable t) {
                    Toast.makeText(EntryActivity.this, ToastMsg.NETWORK_ERROR, Toast.LENGTH_SHORT).show();
                }
            });
        }

    }
}
