package com.xuewen.xuewen;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.xuewen.networkservice.ApiService;
import com.xuewen.networkservice.UUidResult;
import com.xuewen.utility.CurrentUser;
import com.xuewen.utility.GlobalUtil;
import com.xuewen.utility.ToastMsg;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Multipart;

public class ModifyInfoActivity extends AppCompatActivity {
    ImageView avatar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_info);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        avatar = (ImageView) findViewById(R.id.avatar);
        ImageLoader.getInstance().displayImage("drawable://" +  R.drawable.avatar, avatar, GlobalUtil.getInstance().circleBitmapOptions);

        String usernameString = "tion";
        RequestBody username = RequestBody.create(MediaType.parse("multipart/form-data"), usernameString);
        String statusString = "new description";
        RequestBody status = RequestBody.create(MediaType.parse("multipart/form-data"), statusString);
        String descriptionString = "new description";
        RequestBody description = RequestBody.create(MediaType.parse("multipart/form-data"), descriptionString);
        String schoolString = "new description";
        RequestBody school = RequestBody.create(MediaType.parse("multipart/form-data"), schoolString);
        String majorString = "new description";
        RequestBody major = RequestBody.create(MediaType.parse("multipart/form-data"), majorString);
        String gradeString = "new description";
        RequestBody grade = RequestBody.create(MediaType.parse("multipart/form-data"), gradeString);

        File file = new File(getExternalFilesDir(null)+"/test.jpg");

        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("avatar", file.getName(), requestBody);

        // 执行请求
        ApiService apiService = ApiService.retrofit.create(ApiService.class);
        Call<UUidResult> call = apiService.requestUUid(
                CurrentUser.userId,
                username,
                status,
                description,
                school,
                major,
                grade,
                body
        );
        call.enqueue(new Callback<UUidResult>() {
            @Override
            public void onResponse(Call<UUidResult> call, Response<UUidResult> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(ModifyInfoActivity.this, ToastMsg.SERVER_ERROR, Toast.LENGTH_LONG).show();
                    return;
                }
                if (response.body().status != 200) {
                    Toast.makeText(ModifyInfoActivity.this, response.body().errmsg, Toast.LENGTH_LONG).show();
                    return;
                }

                Toast.makeText(ModifyInfoActivity.this, "CHENG GONG", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<UUidResult> call, Throwable t) {
                Toast.makeText(ModifyInfoActivity.this, ToastMsg.NETWORK_ERROR+" : "+t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }
}
