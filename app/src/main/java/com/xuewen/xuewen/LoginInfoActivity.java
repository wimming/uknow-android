package com.xuewen.xuewen;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.xuewen.networkservice.ApiService;
import com.xuewen.networkservice.UUidPResult;
import com.xuewen.networkservice.UUidResult;
import com.xuewen.utility.CurrentUser;
import com.xuewen.utility.ToastMsg;
import com.xuewen.utility.Validate;
import com.xuewen.utility.Validator;
import com.xuewen.xuewen.wxapi.WXEntryActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginInfoActivity extends AppCompatActivity {

    private static final String[] schools_data = {"中山大学", "清华大学", "北京大学"};
    private static final String[] major_data = {"软件工程", "计算机科学"};
    private static final String[] years_data = {"2008", "2009", "2010", "2011", "2012", "2013", "2014", "2015"};
    private static final String[] education_data = {"本科生", "硕士生", "博士生"};

    @BindView(R.id.username) EditText username;
    @BindView(R.id.description) EditText description;

    @BindView(R.id.years) Spinner years;
//    @BindView(R.id.years_text)
//    TextView years_text;
    @BindView(R.id.education) Spinner education;
//    @BindView(R.id.education_text)
//    TextView education_text;

    @BindView(R.id.school_auto_complete) AutoCompleteTextView school_auto_complete;
    @BindView(R.id.major_auto_complete) AutoCompleteTextView major_auto_complete;
    @BindView(R.id.confirmBtn) Button confirmBtn;

    private ArrayAdapter<String> major_auto_complete_adapter;
    private ArrayAdapter<String> school_auto_complete_adapter;

    private ArrayAdapter<String>  years_adapter;
    private ArrayAdapter<String> education_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_info);

        ButterKnife.bind(this);

        // auto complement
        major_auto_complete_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, major_data);
        school_auto_complete_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, schools_data);
        school_auto_complete.setAdapter(school_auto_complete_adapter);
        major_auto_complete.setAdapter(major_auto_complete_adapter);

        // spinner
        years_adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, years_data);
        education_adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, education_data);

        years_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        education_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        years.setAdapter(years_adapter);
        education.setAdapter(education_adapter);

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                patchModifyUserInfoService(CurrentUser.userId);
            }
        });

    }

    private void patchModifyUserInfoService(int uid) {

        if (!Validator.validateNoEmpty(
                username.getText().toString(),
                description.getText().toString(),
                school_auto_complete.getText().toString(),
                major_auto_complete.getText().toString(),
                years.getSelectedItem().toString(),
                education.getSelectedItem().toString()
                )) {

            Toast.makeText(LoginInfoActivity.this, ToastMsg.ARG_INVALID_EMPTY, Toast.LENGTH_SHORT).show();
            return;
        }

        String usernameString = username.getText().toString();
        RequestBody username = RequestBody.create(MediaType.parse("multipart/form-data"), usernameString);
        String descriptionString = description.getText().toString();
        RequestBody description = RequestBody.create(MediaType.parse("multipart/form-data"), descriptionString);
        String schoolString = school_auto_complete.getText().toString();
        RequestBody school = RequestBody.create(MediaType.parse("multipart/form-data"), schoolString);
        String majorString = major_auto_complete.getText().toString();
        RequestBody major = RequestBody.create(MediaType.parse("multipart/form-data"), majorString);
        String gradeString = years.getSelectedItem().toString()+education.getSelectedItem().toString();
        RequestBody grade = RequestBody.create(MediaType.parse("multipart/form-data"), gradeString);

        // 执行请求
        ApiService apiService = ApiService.retrofit.create(ApiService.class);
        Call<UUidPResult> call = apiService.requestUUidP(
                uid,
                username,
                description,
                school,
                major,
                grade
        );
        call.enqueue(new Callback<UUidPResult>() {
            @Override
            public void onResponse(Call<UUidPResult> call, Response<UUidPResult> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(LoginInfoActivity.this, ToastMsg.SERVER_ERROR, Toast.LENGTH_LONG).show();
                    return;
                }
                if (response.body().status != 200) {
                    Toast.makeText(LoginInfoActivity.this, response.body().errmsg, Toast.LENGTH_LONG).show();
                    return;
                }

                Intent intent = new Intent(LoginInfoActivity.this, MainActivity.class);
                startActivity(intent);
            }

            @Override
            public void onFailure(Call<UUidPResult> call, Throwable t) {
                Toast.makeText(LoginInfoActivity.this, ToastMsg.NETWORK_ERROR+" : "+t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

}
