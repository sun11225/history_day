package com.example.history_day.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.history_day.MainActivity;
import com.example.history_day.bean.User;
import com.example.history_day.utils.ShareUtils;

import androidx.appcompat.app.AppCompatActivity;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.util.Utils;

import com.example.history_day.R;

import com.example.history_day.MainActivity;
import com.example.history_day.utils.ShareUtils;
import com.google.android.material.snackbar.Snackbar;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText userName, password;
    private Button login, register;
    private CheckBox checkBox;
    private TextView adminTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //初始化
        initView();
    }

    private void initView() {
        userName = findViewById(R.id.ed_user);
        password = findViewById(R.id.ed_pwd);
        login = findViewById(R.id.bt_login);
        register = findViewById(R.id.bt_register);
        checkBox = findViewById(R.id.checkbox_1);
        register.setOnClickListener(this);
        login.setOnClickListener(this);

        adminTv=findViewById(R.id.tv_admin);
        adminTv.setOnClickListener(this);

        //默认没有点击保存
        boolean rememberPwd = ShareUtils.getBoolean(this, "rememberPwd", false);
        checkBox.setChecked(rememberPwd);
        //判断是否点击保存
        if (rememberPwd) {
            //读取用户密码
            String user = ShareUtils.getString(this, "user", "");
            String password1 = ShareUtils.getString(this, "password", "");
            userName.setText(user);
            password.setText(password1);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_login:
                String name = userName.getText().toString().trim();
                String pass = password.getText().toString().trim();
                if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(pass)) {
                    //登录
                    User user = new User();
                    user.setUsername(name);
                    user.setPassword(pass);
                    user.login(new SaveListener<User>() {

                        @Override
                        public void done(User user, BmobException e) {
                            if (e == null && user.getEmailVerified()) {
                                Toast.makeText(getApplicationContext(), "登录成功！", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                assert e != null;
                                Toast.makeText(getApplicationContext(), "登录失败！" + e.getErrorCode(),
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(getApplicationContext(), "输入框不能为空！", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.bt_register:
                //注册
                Intent intent1 = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent1);
                break;
            case R.id.tv_admin:
                Intent intent=new Intent(LoginActivity.this,AdminLoginActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //保存状态
        ShareUtils.putBoolean(this, "rememberPwd", checkBox.isChecked());

        //panduans判断是否点击了保存
        if (checkBox.isChecked()) {
            ShareUtils.putString(this, "user", userName.getText().toString().trim());
            ShareUtils.putString(this, "password", password.getText().toString().trim());
        } else {
            //删除储存的信息
            ShareUtils.deleteShare(this, "user");
            ShareUtils.deleteShare(this, "password");
        }
    }
}
