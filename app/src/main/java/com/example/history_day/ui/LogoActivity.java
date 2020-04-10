package com.example.history_day.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.history_day.MainActivity;
import com.example.history_day.R;

import java.util.Timer;
import java.util.TimerTask;

public class LogoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logo);

        //欢迎界面延迟跳转
        final Intent intent=new Intent(LogoActivity.this, LoginActivity.class);
        Timer timer=new Timer();
        TimerTask timerTask=new TimerTask() {
            @Override
            public void run() {
                startActivity(intent);
                finish();
            }
        };
        timer.schedule(timerTask,2*1000);
    }
}
