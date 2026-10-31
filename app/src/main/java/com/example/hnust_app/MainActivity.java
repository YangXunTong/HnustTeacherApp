package com.example.hnust_app;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 自动登录检查
        SharedPreferences sp = getSharedPreferences("LoginState", Context.MODE_PRIVATE);
        if (sp.getBoolean("is_logged_in", false)) {
            startActivity(new Intent(this, HomeActivity.class));
            finish();
            return;
        }

        setContentView(R.layout.activity_main);

        DBHelper db = new DBHelper(this);
        EditText etUser = findViewById(R.id.et_user);
        EditText etPass = findViewById(R.id.et_pass);
        Button btnLogin = findViewById(R.id.btn_login);
        TextView tvRegister = findViewById(R.id.tv_to_register);
        TextView tvForget = findViewById(R.id.tv_to_forget);

        btnLogin.setOnClickListener(v -> {
            String user = etUser.getText().toString();
            String pass = etPass.getText().toString();
            if (db.login(user, pass)) {
                SharedPreferences.Editor editor = sp.edit();
                editor.putBoolean("is_logged_in", true);
                editor.putString("username", user);
                editor.apply();
                startActivity(new Intent(this, HomeActivity.class));
                finish();
            } else {
                Toast.makeText(this, "账号或密码错误", Toast.LENGTH_SHORT).show();
            }
        });

        // 注册弹窗
        tvRegister.setOnClickListener(v -> {
            LinearLayout layout = new LinearLayout(this);
            layout.setOrientation(LinearLayout.VERTICAL);
            layout.setPadding(50, 20, 50, 20);
            EditText inputU = new EditText(this); inputU.setHint("新账号");
            EditText inputP = new EditText(this); inputP.setHint("新密码");
            inputP.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            layout.addView(inputU); layout.addView(inputP);

            new AlertDialog.Builder(this).setTitle("注册").setView(layout)
                    .setPositiveButton("确定", (d, w) -> {
                        db.registerTeacher(inputU.getText().toString(), inputP.getText().toString());
                        Toast.makeText(this, "注册成功", Toast.LENGTH_SHORT).show();
                    }).setNegativeButton("取消", null).show();
        });

        // 找回密码弹窗
        tvForget.setOnClickListener(v -> {
            EditText inputU = new EditText(this); inputU.setHint("输入账号");
            new AlertDialog.Builder(this).setTitle("找回密码").setView(inputU)
                    .setPositiveButton("查询", (d, w) -> {
                        String pwd = db.getPasswordByUsername(inputU.getText().toString());
                        if (pwd != null) {
                            new AlertDialog.Builder(this).setTitle("密码").setMessage(pwd).setPositiveButton("知道了", null).show();
                        } else {
                            Toast.makeText(this, "账号不存在", Toast.LENGTH_SHORT).show();
                        }
                    }).setNegativeButton("取消", null).show();
        });
    }
}