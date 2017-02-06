package com.xuewen.xuewen;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

/**
 * Created by huangyuming on 17-2-6.
 */

public class EntryActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String token = getSharedPreferences("login_info", MODE_PRIVATE).getString("token", "");

        if (token.isEmpty()) {
            startActivity(new Intent(EntryActivity.this, LoginActivity.class));
        }
        else {
            Toast.makeText(this, "time to code", Toast.LENGTH_SHORT).show();
        }

    }
}
