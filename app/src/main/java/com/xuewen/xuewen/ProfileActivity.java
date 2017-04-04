package com.xuewen.xuewen;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.xuewen.bean.UUidBean;
import com.xuewen.networkservice.ApiService;
import com.xuewen.networkservice.UUidResult;
import com.xuewen.utility.CurrentUser;
import com.xuewen.utility.GlobalUtil;
import com.xuewen.utility.MyTextWatcher;
import com.xuewen.utility.ToastMsg;
import com.xuewen.utility.Validate;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {

    @BindView(R.id.avatar)
    ImageView avatar;

    @BindView(R.id.username)
    EditText username;
    @BindView(R.id.usernameTextInfo)
    TextView usernameTextInfo;
    @BindView(R.id.status)
    EditText status;
    @BindView(R.id.statusTextInfo)
    TextView statusTextInfo;
    @BindView(R.id.description)
    EditText description;
    @BindView(R.id.descriptionTextInfo)
    TextView descriptionTextInfo;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.confirm)
    ImageView confirm;
    @BindView(R.id.visibilityController)
    LinearLayout visibilityController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ButterKnife.bind(this);

        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                patchModifyUserInfoService(CurrentUser.userId);
            }
        });

        // retrieve data
        // 不可见 -> 加载成功 -> 可见
        visibilityController.setVisibility(View.INVISIBLE);
        requestAndRender(CurrentUser.userId);

        username.addTextChangedListener(new MyTextWatcher(this, 10, usernameTextInfo));
        status.addTextChangedListener(new MyTextWatcher(this, 20, statusTextInfo));
        description.addTextChangedListener(new MyTextWatcher(this, 50, descriptionTextInfo));

    }


    private void requestAndRender(int uid) {

        ApiService apiService = ApiService.retrofit.create(ApiService.class);
        Call<UUidResult> call = apiService.requestUUid(uid);
        call.enqueue(new Callback<UUidResult>() {
            @Override
            public void onResponse(Call<UUidResult> call, Response<UUidResult> response) {

                if (!response.isSuccessful()) {
                    Toast.makeText(ProfileActivity.this, ToastMsg.SERVER_ERROR, Toast.LENGTH_LONG).show();
                    return;
                }
                if (response.body().status != 200) {
                    Toast.makeText(ProfileActivity.this, response.body().errmsg, Toast.LENGTH_LONG).show();
                    return;
                }

                //Toast.makeText(ProfileActivity.this, response.body().toString(), Toast.LENGTH_SHORT).show();
                UUidBean bean = response.body().data;
                username.setText(bean.username);
                status.setText(bean.status);
                description.setText(bean.description);
                ImageLoader.getInstance().displayImage(GlobalUtil.getInstance().baseAvatarUrl+bean.avatarUrl, avatar, GlobalUtil.getInstance().circleBitmapOptions);

                visibilityController.setVisibility(View.VISIBLE);

            }
            @Override
            public void onFailure(Call<UUidResult> call, Throwable t) {
                Toast.makeText(ProfileActivity.this, ToastMsg.NETWORK_ERROR+" : "+t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }


    private void patchModifyUserInfoService(int uid) {

        if (Validate.isExistEmpty(username, status, description)) {
            Toast.makeText(ProfileActivity.this, ToastMsg.ARG_INVALID_EMPTY, Toast.LENGTH_SHORT).show();
            return;
        }

        String usernameString = username.getText().toString();
        RequestBody username = RequestBody.create(MediaType.parse("multipart/form-data"), usernameString);
        String statusString = status.getText().toString();
        RequestBody status = RequestBody.create(MediaType.parse("multipart/form-data"), statusString);
        String descriptionString = description.getText().toString();
        RequestBody description = RequestBody.create(MediaType.parse("multipart/form-data"), descriptionString);
        String schoolString = "中山大学";
        RequestBody school = RequestBody.create(MediaType.parse("multipart/form-data"), schoolString);
        String majorString = "软件工程";
        RequestBody major = RequestBody.create(MediaType.parse("multipart/form-data"), majorString);
        String gradeString = "2014本科生";
        RequestBody grade = RequestBody.create(MediaType.parse("multipart/form-data"), gradeString);

        //File file = new File(getExternalFilesDir(null)+"/test.jpg");
        //RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        //MultipartBody.Part body = MultipartBody.Part.createFormData("avatar", file.getName(), requestBody);

        // 执行请求
        ApiService apiService = ApiService.retrofit.create(ApiService.class);
        Call<UUidResult> call = apiService.requestUUid(
                uid,
                username,
                status,
                description,
                school,
                major,
                grade,
                null
        );
        call.enqueue(new Callback<UUidResult>() {
            @Override
            public void onResponse(Call<UUidResult> call, Response<UUidResult> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(ProfileActivity.this, ToastMsg.SERVER_ERROR, Toast.LENGTH_LONG).show();
                    return;
                }
                if (response.body().status != 200) {
                    Toast.makeText(ProfileActivity.this, response.body().errmsg, Toast.LENGTH_LONG).show();
                    return;
                }

                ToastMsg.showTips(ProfileActivity.this, ToastMsg.MODIFY_SUCCESS);
                MainActivity.getDataKeeper().mineCached = false;
                finish();
            }

            @Override
            public void onFailure(Call<UUidResult> call, Throwable t) {
                Toast.makeText(ProfileActivity.this, ToastMsg.NETWORK_ERROR+" : "+t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
