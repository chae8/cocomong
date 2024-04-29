package com.sw.cocomong.view.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.sw.cocomong.R;
import com.sw.cocomong.task.JoinTask;

import java.util.concurrent.ExecutionException;

public class JoinActivity extends AppCompatActivity {
    Button login, join, findPw;
    EditText name, pw, pwCheck;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        login = findViewById(R.id.btn_login);
        join = findViewById(R.id.btn_join);
        findPw = findViewById(R.id.btn_findPw);

        name = findViewById(R.id.et_loginName);
        pw = findViewById(R.id.et_loginPw);
        pwCheck = findViewById(R.id.et_loginPwCheck);

        login.setVisibility(View.GONE);
        findPw.setVisibility(View.GONE);

        join.setOnClickListener(v-> {
            JoinTask joinTask = new JoinTask(name.getText().toString(),pw.getText().toString());
            String result= null;
            try {
                result = joinTask.execute(name.getText().toString(),pw.getText().toString()).get();
            } catch (Exception e){
                e.printStackTrace();
            }
            Log.w("받은 값 (Join): ", result);

            finish();
        });
    }
}
