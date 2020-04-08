package com.example.history_day.ui;

import androidx.appcompat.app.AppCompatActivity;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.history_day.R;
import com.example.history_day.bean.User;

public class AdminLoginActivity extends AppCompatActivity implements View.OnClickListener {

    private Button adLogin,adBack;
    private EditText adUser,adPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        initView();
    }

    private void initView() {
        adBack=findViewById(R.id.bt_admin_back);
        adLogin=findViewById(R.id.bt_admin_login);
        adUser=findViewById(R.id.ed_admin_user);
        adPassword=findViewById(R.id.ed_admin_pwd);
        adBack.setOnClickListener(this);
        adLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_admin_login:
                String name=adUser.getText().toString().trim();
                String pass=adPassword.getText().toString().trim();
                //登录
                if (!TextUtils.isEmpty(name)&&!TextUtils.isEmpty(pass)){
                    if (name.equals("admin")&&pass.equals("123456")) {
                        User user = new User();
                        user.setUsername(name);
                        user.setPassword(pass);
                        user.login(new SaveListener<User>() {
                            @Override
                            public void done(User user, BmobException e) {
                                if (e == null) {
                                    Toast.makeText(getApplicationContext(), "登录成功！", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(AdminLoginActivity.this, ADMainActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(getApplicationContext(), "登录失败！" + e.getErrorCode(),
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }else {
                        Toast.makeText(getApplicationContext(),"请输入正确的管理员账户！",Toast.LENGTH_LONG).show();
                    }
                }else {
                    Toast.makeText(getApplicationContext(),"输入框不能为空！",Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.bt_admin_back:
                Intent intent1=new Intent(AdminLoginActivity.this,LoginActivity.class);
                startActivity(intent1);
                finish();
                break;
        }
    }
}
