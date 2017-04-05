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
import com.xuewen.utility.CurrentUser;
import com.xuewen.utility.StatisticStorage;
import com.xuewen.utility.ToastMsg;
import com.xuewen.utility.Validator;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginInfoActivity extends AppCompatActivity {

    @BindView(R.id.year) Spinner year;
    @BindView(R.id.education) Spinner education;

    @BindView(R.id.school_auto_complete) AutoCompleteTextView school_auto_complete;
    @BindView(R.id.major_auto_complete) AutoCompleteTextView major_auto_complete;

    @BindView(R.id.confirm)
    TextView confirm;

    private ArrayAdapter<String> major_auto_complete_adapter;
    private ArrayAdapter<String> school_auto_complete_adapter;

    private ArrayAdapter<String>  year_adapter;
    private ArrayAdapter<String> education_adapter;

    private StatisticStorage statisticStorage = new StatisticStorage();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_info);
        ButterKnife.bind(this);

        // auto complement
        school_auto_complete_adapter = new ArrayAdapter<>(this, android.R.layout.simple_expandable_list_item_1, statisticStorage.schoolsData);
        major_auto_complete_adapter = new ArrayAdapter<>(this, android.R.layout.simple_expandable_list_item_1, statisticStorage.majorData);
        school_auto_complete.setAdapter(school_auto_complete_adapter);
        major_auto_complete.setAdapter(major_auto_complete_adapter);

        // spinner
        year_adapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, statisticStorage.yearData);
        education_adapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, statisticStorage.educationData);

        year_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        education_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        year.setAdapter(year_adapter);
        education.setAdapter(education_adapter);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                patchModifyUserInfoService(CurrentUser.userId);
            }
        });

    }

    private void patchModifyUserInfoService(int uid) {

        if (!Validator.validateNoEmpty(
                school_auto_complete.getText().toString(),
                major_auto_complete.getText().toString(),
                year.getSelectedItem().toString(),
                education.getSelectedItem().toString()
                )) {

            Toast.makeText(LoginInfoActivity.this, ToastMsg.INVALID_ARG_EMPTY, Toast.LENGTH_SHORT).show();
            return;
        }

        String schoolString = school_auto_complete.getText().toString();
        RequestBody school = RequestBody.create(MediaType.parse("multipart/form-data"), schoolString);
        String majorString = major_auto_complete.getText().toString();
        RequestBody major = RequestBody.create(MediaType.parse("multipart/form-data"), majorString);
        String gradeString = year.getSelectedItem().toString()+education.getSelectedItem().toString();
        RequestBody grade = RequestBody.create(MediaType.parse("multipart/form-data"), gradeString);

        // 执行请求
        ApiService apiService = ApiService.retrofit.create(ApiService.class);
        Call<UUidPResult> call = apiService.requestUUidP(
                uid,
                school,
                major,
                grade
        );
        call.enqueue(new Callback<UUidPResult>() {
            @Override
            public void onResponse(Call<UUidPResult> call, Response<UUidPResult> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(LoginInfoActivity.this, ToastMsg.SERVER_ERROR, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (response.body().status != 200) {
                    Toast.makeText(LoginInfoActivity.this, response.body().errmsg, Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent intent = new Intent(LoginInfoActivity.this, MainActivity.class);
                startActivity(intent);
            }

            @Override
            public void onFailure(Call<UUidPResult> call, Throwable t) {
                Toast.makeText(LoginInfoActivity.this, ToastMsg.NETWORK_ERROR+" : "+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

}
