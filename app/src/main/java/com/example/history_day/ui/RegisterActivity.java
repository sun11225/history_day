package com.example.history_day.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

import com.example.history_day.R;
import com.example.history_day.bean.User;


public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText userName, password, email, password_1;
    private ImageButton imageButton;
    private Button register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
    }

    private void initView() {
        userName = findViewById(R.id.ed_user_1);
        password = findViewById(R.id.ed_pwd_1);
        email = findViewById(R.id.ed_e_mail);
        password_1 = findViewById(R.id.ed_pwd_2);
        imageButton = findViewById(R.id.ibtn_back);
        register = findViewById(R.id.btn_register_1);

        register.setOnClickListener(this);
        imageButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ibtn_back:
                finish();
                break;
            case R.id.btn_register_1:
                String name1 = userName.getText().toString().trim();
                String pass1 = password.getText().toString().trim();
                String pass2 = password_1.getText().toString().trim();
                final String email1 = email.getText().toString().trim();

                if (!TextUtils.isEmpty(name1) && !TextUtils.isEmpty(pass1)) {

                    if (pass1.equals(pass2)) {

                        if (!TextUtils.isEmpty(email1)) {
                            User user = new User();
                            user.setUsername(name1);
                            user.setPassword(pass1);
                            user.setEmail(email1);
                            user.signUp(new SaveListener<User>() {
                                @Override
                                public void done(User user, BmobException e) {
                                    if (e == null) {
                                        Toast.makeText(RegisterActivity.this, "注册成功,请前往邮箱激活账户！", Toast.LENGTH_LONG).show();
                                        Intent intent=new Intent(RegisterActivity.this,LoginActivity.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        Toast.makeText(RegisterActivity.this, "注册失败!" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
//                        //邮箱注册
//                        BmobUser.requestEmailVerify(email1, new UpdateListener() {
//                            @Override
//                            public void done(BmobException e) {
//                                if (e==null){
//                                    Toast.makeText(getApplicationContext(),"注册请求验证邮件成功，请到"
//                                            + email1 + "邮箱中进行激活账户。",Toast.LENGTH_LONG).show();
//
//                                    finish();
//                                }else {
//                                    Log.e("BMOB", e.toString());
//                                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
//                                }
//                            }
//                        })
                        } else {
                            Toast.makeText(getApplicationContext(), "邮箱不能为空！", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "两次密码输入不一致！", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "用户名或密码不能为空！", Toast.LENGTH_SHORT).show();

                }
                break;
        }
    }
}
