package com.xuewen.networkservice;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.xuewen.utility.ToastMsg;
import com.xuewen.xuewen.R;

import java.util.List;

import butterknife.BindViews;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by huangyuming on 16-12-2.
 */

public class APITestActivity extends Activity {

    @BindViews({ R.id.QR, R.id.QQid, R.id.UUidI, R.id.QQidL, R.id.QQidC, R.id.QF, R.id.UUidF, R.id.UUidR, R.id.UF, R.id.Q, R.id.UUid, R.id.UUidPATCH, R.id.QQidA, R.id.UUidFPOST })
    List<Button> buttons;

    private ApiService apiService;

    private int uid = 2;
    private int qid = 6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_apitest);

        ButterKnife.bind(this);

        for (int i = 0; i < buttons.size(); ++i) {
           buttons.get(i).setOnClickListener(onClickListener);
        }

        apiService = ApiService.retrofit.create(ApiService.class);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int id = view.getId();

            if (id == buttons.get(0).getId()) {
                Call<QRResult> call = apiService.requestQR();
                call.enqueue(new Callback<QRResult>() {
                    @Override
                    public void onResponse(Call<QRResult> call, Response<QRResult> response) {
                        if (!response.isSuccessful()) {
                            Toast.makeText(APITestActivity.this, ToastMsg.SERVER_ERROR, Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (response.body().status != 200) {
                            Toast.makeText(APITestActivity.this, response.body().errmsg, Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Toast.makeText(APITestActivity.this, response.body().toString(), Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onFailure(Call<QRResult> call, Throwable t) {
                        Toast.makeText(APITestActivity.this, ToastMsg.NETWORK_ERROR, Toast.LENGTH_SHORT).show();
                    }
                });
            }
            else if (id == buttons.get(1).getId()) {
                Call<QQidResult> call = apiService.requestQQid(qid, uid);
                call.enqueue(new Callback<QQidResult>() {
                    @Override
                    public void onResponse(Call<QQidResult> call, Response<QQidResult> response) {
                        if (!response.isSuccessful()) {
                            Toast.makeText(APITestActivity.this, ToastMsg.SERVER_ERROR, Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (response.body().status != 200) {
                            Toast.makeText(APITestActivity.this, response.body().errmsg, Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Toast.makeText(APITestActivity.this, response.body().toString(), Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onFailure(Call<QQidResult> call, Throwable t) {
                        Toast.makeText(APITestActivity.this, ToastMsg.NETWORK_ERROR, Toast.LENGTH_SHORT).show();
                    }
                });
            }
            else if (id == buttons.get(2).getId()) {
                Call<UUidIResult> call = apiService.requestUUidI(uid, 3);
                call.enqueue(new Callback<UUidIResult>() {
                    @Override
                    public void onResponse(Call<UUidIResult> call, Response<UUidIResult> response) {
                        if (!response.isSuccessful()) {
                            Toast.makeText(APITestActivity.this, ToastMsg.SERVER_ERROR, Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (response.body().status != 200) {
                            Toast.makeText(APITestActivity.this, response.body().errmsg, Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Toast.makeText(APITestActivity.this, response.body().toString(), Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onFailure(Call<UUidIResult> call, Throwable t) {
                        Toast.makeText(APITestActivity.this, ToastMsg.NETWORK_ERROR, Toast.LENGTH_SHORT).show();
                    }
                });
            }
            else if (id == buttons.get(3).getId()) {
                Call<QQidLResult> call = apiService.requestQQidL(qid);
                call.enqueue(new Callback<QQidLResult>() {
                    @Override
                    public void onResponse(Call<QQidLResult> call, Response<QQidLResult> response) {
                        if (!response.isSuccessful()) {
                            Toast.makeText(APITestActivity.this, ToastMsg.SERVER_ERROR, Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (response.body().status != 200) {
                            Toast.makeText(APITestActivity.this, response.body().errmsg, Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Toast.makeText(APITestActivity.this, response.body().toString(), Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onFailure(Call<QQidLResult> call, Throwable t) {
                        Toast.makeText(APITestActivity.this, ToastMsg.NETWORK_ERROR, Toast.LENGTH_SHORT).show();
                    }
                });
            }
            else if (id == buttons.get(4).getId()) {
                Call<QQidCResult> call = apiService.requestQQidC(qid, 1, uid);
                call.enqueue(new Callback<QQidCResult>() {
                    @Override
                    public void onResponse(Call<QQidCResult> call, Response<QQidCResult> response) {
                        if (!response.isSuccessful()) {
                            Toast.makeText(APITestActivity.this, ToastMsg.SERVER_ERROR, Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (response.body().status != 200) {
                            Toast.makeText(APITestActivity.this, response.body().errmsg, Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Toast.makeText(APITestActivity.this, response.body().toString(), Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onFailure(Call<QQidCResult> call, Throwable t) {
                        Toast.makeText(APITestActivity.this, ToastMsg.NETWORK_ERROR, Toast.LENGTH_SHORT).show();
                    }
                });
            }
            else if (id == buttons.get(5).getId()) {
                Call<QFResult> call = apiService.requestQF("query___string");
                call.enqueue(new Callback<QFResult>() {
                    @Override
                    public void onResponse(Call<QFResult> call, Response<QFResult> response) {
                        if (!response.isSuccessful()) {
                            Toast.makeText(APITestActivity.this, ToastMsg.SERVER_ERROR, Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (response.body().status != 200) {
                            Toast.makeText(APITestActivity.this, response.body().errmsg, Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Toast.makeText(APITestActivity.this, response.body().toString(), Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onFailure(Call<QFResult> call, Throwable t) {
                        Toast.makeText(APITestActivity.this, ToastMsg.NETWORK_ERROR, Toast.LENGTH_SHORT).show();
                    }
                });
            }
            else if (id == buttons.get(6).getId()) {
                Call<UUidFResult> call = apiService.requestUUidF(uid);
                call.enqueue(new Callback<UUidFResult>() {
                    @Override
                    public void onResponse(Call<UUidFResult> call, Response<UUidFResult> response) {
                        if (!response.isSuccessful()) {
                            Toast.makeText(APITestActivity.this, ToastMsg.SERVER_ERROR, Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (response.body().status != 200) {
                            Toast.makeText(APITestActivity.this, response.body().errmsg, Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Toast.makeText(APITestActivity.this, response.body().toString(), Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onFailure(Call<UUidFResult> call, Throwable t) {
                        Toast.makeText(APITestActivity.this, ToastMsg.NETWORK_ERROR, Toast.LENGTH_SHORT).show();
                    }
                });
            }
            else if (id == buttons.get(7).getId()) {

                Call<UUidRResult> call = apiService.requestUUidR(uid);
                call.enqueue(new Callback<UUidRResult>() {
                    @Override
                    public void onResponse(Call<UUidRResult> call, Response<UUidRResult> response) {
                        if (!response.isSuccessful()) {
                            Toast.makeText(APITestActivity.this, ToastMsg.SERVER_ERROR, Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (response.body().status != 200) {
                            Toast.makeText(APITestActivity.this, response.body().errmsg, Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Toast.makeText(APITestActivity.this, response.body().toString(), Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onFailure(Call<UUidRResult> call, Throwable t) {
                        Toast.makeText(APITestActivity.this, ToastMsg.NETWORK_ERROR, Toast.LENGTH_SHORT).show();
                    }
                });
            }
            else if (id == buttons.get(8).getId()) {

                Call<UFResult> call = apiService.requestUF("query_____++__string");
                call.enqueue(new Callback<UFResult>() {
                    @Override
                    public void onResponse(Call<UFResult> call, Response<UFResult> response) {
                        if (!response.isSuccessful()) {
                            Toast.makeText(APITestActivity.this, ToastMsg.SERVER_ERROR, Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (response.body().status != 200) {
                            Toast.makeText(APITestActivity.this, response.body().errmsg, Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Toast.makeText(APITestActivity.this, response.body().toString(), Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onFailure(Call<UFResult> call, Throwable t) {
                        Toast.makeText(APITestActivity.this, ToastMsg.NETWORK_ERROR, Toast.LENGTH_SHORT).show();
                    }
                });
            }
            else if (id == buttons.get(9).getId()) {

                Call<QResult> call = apiService.requestQ(uid, "xxxx", 7);
                call.enqueue(new Callback<QResult>() {
                    @Override
                    public void onResponse(Call<QResult> call, Response<QResult> response) {
                        if (!response.isSuccessful()) {
                            Toast.makeText(APITestActivity.this, ToastMsg.SERVER_ERROR, Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (response.body().status != 200) {
                            Toast.makeText(APITestActivity.this, response.body().errmsg, Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Toast.makeText(APITestActivity.this, response.body().toString(), Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onFailure(Call<QResult> call, Throwable t) {
                        Toast.makeText(APITestActivity.this, ToastMsg.NETWORK_ERROR, Toast.LENGTH_SHORT).show();
                    }
                });
            }
            else if (id == buttons.get(10).getId()) {

                Call<UUidResult> call = apiService.requestUUid(uid);
                call.enqueue(new Callback<UUidResult>() {
                    @Override
                    public void onResponse(Call<UUidResult> call, Response<UUidResult> response) {
                        if (!response.isSuccessful()) {
                            Toast.makeText(APITestActivity.this, ToastMsg.SERVER_ERROR, Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (response.body().status != 200) {
                            Toast.makeText(APITestActivity.this, response.body().errmsg, Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Toast.makeText(APITestActivity.this, response.body().toString(), Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onFailure(Call<UUidResult> call, Throwable t) {
                        Toast.makeText(APITestActivity.this, ToastMsg.NETWORK_ERROR, Toast.LENGTH_SHORT).show();
                    }
                });
            }
            else if (id == buttons.get(11).getId()) {

                Call<UUidResult> call = apiService.requestUUid(uid,
                        RequestBody.create(MediaType.parse("multipart/form-data"), "mine"),
                        RequestBody.create(MediaType.parse("multipart/form-data"), "mime"),
                        RequestBody.create(MediaType.parse("multipart/form-data"), "mime"),
                        RequestBody.create(MediaType.parse("multipart/form-data"), "mime"),
                        RequestBody.create(MediaType.parse("multipart/form-data"), "mime"),
                        RequestBody.create(MediaType.parse("multipart/form-data"), "mime"),
                        null);
                call.enqueue(new Callback<UUidResult>() {
                    @Override
                    public void onResponse(Call<UUidResult> call, Response<UUidResult> response) {
                        if (!response.isSuccessful()) {
                            Toast.makeText(APITestActivity.this, ToastMsg.SERVER_ERROR, Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (response.body().status != 200) {
                            Toast.makeText(APITestActivity.this, response.body().errmsg, Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Toast.makeText(APITestActivity.this, response.body().toString(), Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onFailure(Call<UUidResult> call, Throwable t) {
                        Toast.makeText(APITestActivity.this, ToastMsg.NETWORK_ERROR, Toast.LENGTH_SHORT).show();
                    }
                });
            }
            else if (id == buttons.get(12).getId()) {

                Call<QQidAResult> call = apiService.requestQQidA(qid,
                        RequestBody.create(MediaType.parse("multipart/form-data"), "8"),
                        RequestBody.create(MediaType.parse("multipart/form-data"), "99"),
                        null);
                call.enqueue(new Callback<QQidAResult>() {
                    @Override
                    public void onResponse(Call<QQidAResult> call, Response<QQidAResult> response) {
                        if (!response.isSuccessful()) {
                            Toast.makeText(APITestActivity.this, ToastMsg.SERVER_ERROR, Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (response.body().status != 200) {
                            Toast.makeText(APITestActivity.this, response.body().errmsg, Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Toast.makeText(APITestActivity.this, response.body().toString(), Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onFailure(Call<QQidAResult> call, Throwable t) {
                        Toast.makeText(APITestActivity.this, ToastMsg.NETWORK_ERROR, Toast.LENGTH_SHORT).show();
                    }
                });
            }
            else if (id == buttons.get(13).getId()) {

                Call<UUidFResult> call = apiService.requestUUidF(uid, 5);
                call.enqueue(new Callback<UUidFResult>() {
                    @Override
                    public void onResponse(Call<UUidFResult> call, Response<UUidFResult> response) {
                        if (!response.isSuccessful()) {
                            Toast.makeText(APITestActivity.this, ToastMsg.SERVER_ERROR, Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (response.body().status != 200) {
                            Toast.makeText(APITestActivity.this, response.body().errmsg, Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Toast.makeText(APITestActivity.this, response.body().toString(), Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onFailure(Call<UUidFResult> call, Throwable t) {
                        Toast.makeText(APITestActivity.this, ToastMsg.NETWORK_ERROR+" : "+t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

        }
    };
}
