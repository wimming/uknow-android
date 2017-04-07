package com.xuewen.xuewen;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.xuewen.networkservice.ApiService;
import com.xuewen.networkservice.LogoutResult;
import com.xuewen.utility.ToastMsg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class SettingActivity extends AppCompatActivity {

//    @BindView(R.id.listView) ListView listView;

//    private static final String [] textData = {"使用帮助","关于优知", "意见反馈", "版本更新"};
    private static final String [] textData = new String[4];

    @BindView(R.id.logout)
    TextView logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);

        textData[0] = "使用帮助";
        textData[1] = "关于"+getResources().getString(R.string.app_name);
        textData[2] = "意见反馈";
        textData[3] = "版本更新";

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ListView listView = (ListView) findViewById(R.id.listView);
        List<Map<String, String>> list_data = new ArrayList<Map<String, String>>();
        for (int i = 0; i < textData.length; i++) {
            Map<String, String> tempMap = new HashMap<String, String>();
            tempMap.put("textview", textData[i]);
            list_data.add(tempMap);
        }
        SimpleAdapter simpleAdapter = new SimpleAdapter(this, list_data,
                R.layout.activity_setting_item, new String[] {"textview"}, new int[] {R.id.textview});
        listView.setAdapter(simpleAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(SettingActivity.this, ToastMsg.UNDER_IMPLEMENTATION, Toast.LENGTH_SHORT).show();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });

    }

    private void logout() {
        ApiService apiService = ApiService.retrofit.create(ApiService.class);
        Call<LogoutResult> call = apiService.requestLogout();
        call.enqueue(new Callback<LogoutResult>() {
            @Override
            public void onResponse(Call<LogoutResult> call, Response<LogoutResult> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(SettingActivity.this, ToastMsg.SERVER_ERROR, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (response.body().status != 200) {
                    Toast.makeText(SettingActivity.this, response.body().errmsg, Toast.LENGTH_SHORT).show();
                    return;
                }

                SharedPreferences.Editor editor = getSharedPreferences("login_info", MODE_PRIVATE).edit();
                editor.putInt("user_id", -1);
                editor.putString("token", "");
                editor.commit();

                Intent intent = new Intent(SettingActivity.this, EntryActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

                Toast.makeText(SettingActivity.this, "退出成功", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(Call<LogoutResult> call, Throwable t) {
                Toast.makeText(SettingActivity.this, ToastMsg.NETWORK_ERROR+" : "+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
