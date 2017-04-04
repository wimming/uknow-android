package com.xuewen.xuewen;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
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
import com.xuewen.utility.StatisticStorage;
import com.xuewen.utility.ToastMsg;
import com.xuewen.utility.TextViewValidator;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.avatar)
    ImageView avatar;
    @BindView(R.id.username)
    EditText username;
    @BindView(R.id.status)
    EditText status;
    @BindView(R.id.description)
    EditText description;
    @BindView(R.id.school)
    TextView school;
    @BindView(R.id.major)
    TextView major;
    @BindView(R.id.grade)
    TextView grade;

    @BindView(R.id.usernameTextInfo)
    TextView usernameTextInfo;
    @BindView(R.id.statusTextInfo)
    TextView statusTextInfo;
    @BindView(R.id.descriptionTextInfo)
    TextView descriptionTextInfo;

    @BindView(R.id.confirm)
    ImageView confirm;
    @BindView(R.id.visibilityController)
    LinearLayout visibilityController;

    private StatisticStorage statisticStorage = new StatisticStorage();

    @OnClick(R.id.schoolRow)
    void schoolClick() {
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(ProfileActivity.this, android.R.layout.select_dialog_singlechoice);
        for (int i = 0; i < statisticStorage.schoolsData.length; ++i) {
            arrayAdapter.add(statisticStorage.schoolsData[i]);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        })
        .setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String strName = arrayAdapter.getItem(which);
                school.setText(strName);
            }
        }).create().show();
    }
    @OnClick(R.id.majorRow)
    void majorClick() {
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(ProfileActivity.this, android.R.layout.select_dialog_singlechoice);
        for (int i = 0; i < statisticStorage.majorData.length; ++i) {
            arrayAdapter.add(statisticStorage.majorData[i]);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        })
        .setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String strName = arrayAdapter.getItem(which);
                major.setText(strName);
            }
        }).create().show();
    }
    @OnClick(R.id.gradeRow)
    void gradeClick() {
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(ProfileActivity.this, android.R.layout.select_dialog_singlechoice);
        for (int i = 0; i < statisticStorage.yearData.length; ++i) {
            arrayAdapter.add(statisticStorage.yearData[i]);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        })
        .setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String strName = arrayAdapter.getItem(which);
                grade.setText(strName);
            }
        }).create().show();
    }

//    private List<Map<String, String>> dataSource = new ArrayList<Map<String, String>>();

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

//        ListView listView = (ListView) findViewById(R.id.settingList);
//        SimpleAdapter simpleAdapter = new SimpleAdapter(this, dataSource,
//                R.layout.activity_profile_item, new String[] {"label", "content", "arr"}, new int[] {R.id.label, R.id.content, R.id.arr});
//        listView.setAdapter(simpleAdapter);

    }


    private void requestAndRender(int uid) {

        ApiService apiService = ApiService.retrofit.create(ApiService.class);
        Call<UUidResult> call = apiService.requestUUid(uid);
        call.enqueue(new Callback<UUidResult>() {
            @Override
            public void onResponse(Call<UUidResult> call, Response<UUidResult> response) {

                if (!response.isSuccessful()) {
                    Toast.makeText(ProfileActivity.this, ToastMsg.SERVER_ERROR, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (response.body().status != 200) {
                    Toast.makeText(ProfileActivity.this, response.body().errmsg, Toast.LENGTH_SHORT).show();
                    return;
                }

                //Toast.makeText(ProfileActivity.this, response.body().toString(), Toast.LENGTH_SHORT).show();
                UUidBean bean = response.body().data;
                username.setText(bean.username);
                status.setText(bean.status);
                description.setText(bean.description);
                ImageLoader.getInstance().displayImage(GlobalUtil.getInstance().baseAvatarUrl+bean.avatarUrl, avatar, GlobalUtil.getInstance().circleBitmapOptions);
                school.setText(bean.school);
                major.setText(bean.major);
                grade.setText(bean.grade);

//                dataSource.clear();
//                Map<String, String> map;
//                map = new HashMap<>();
//                map.put("label", "学校");
//                map.put("content", bean.school);
//                map.put("arr", ">");
//                dataSource.add(map);
//                map = new HashMap<>();
//                map.put("label", "专业");
//                map.put("content", bean.major);
//                map.put("arr", ">");
//                dataSource.add(map);
//                map = new HashMap<>();
//                map.put("label", "年级");
//                map.put("content", bean.grade);
//                map.put("arr", ">");
//                dataSource.add(map);

                visibilityController.setVisibility(View.VISIBLE);

            }
            @Override
            public void onFailure(Call<UUidResult> call, Throwable t) {
                Toast.makeText(ProfileActivity.this, ToastMsg.NETWORK_ERROR+" : "+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void patchModifyUserInfoService(int uid) {

        if (TextViewValidator.isExistEmpty(username, status, description, school, major, grade)) {
            Toast.makeText(ProfileActivity.this, ToastMsg.ARG_INVALID_EMPTY, Toast.LENGTH_SHORT).show();
            return;
        }

        String usernameString = username.getText().toString();
        RequestBody username = RequestBody.create(MediaType.parse("multipart/form-data"), usernameString);
        String statusString = status.getText().toString();
        RequestBody status = RequestBody.create(MediaType.parse("multipart/form-data"), statusString);
        String descriptionString = description.getText().toString();
        RequestBody description = RequestBody.create(MediaType.parse("multipart/form-data"), descriptionString);
        String schoolString = school.getText().toString();
        RequestBody school = RequestBody.create(MediaType.parse("multipart/form-data"), schoolString);
        String majorString = major.getText().toString();
        RequestBody major = RequestBody.create(MediaType.parse("multipart/form-data"), majorString);
        String gradeString = grade.getText().toString();
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
                    Toast.makeText(ProfileActivity.this, ToastMsg.SERVER_ERROR, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (response.body().status != 200) {
                    Toast.makeText(ProfileActivity.this, response.body().errmsg, Toast.LENGTH_SHORT).show();
                    return;
                }

                ToastMsg.showTips(ProfileActivity.this, ToastMsg.MODIFY_SUCCESS);
                MainActivity.getDataKeeper().mineCached = false;
                finish();
            }

            @Override
            public void onFailure(Call<UUidResult> call, Throwable t) {
                Toast.makeText(ProfileActivity.this, ToastMsg.NETWORK_ERROR+" : "+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
