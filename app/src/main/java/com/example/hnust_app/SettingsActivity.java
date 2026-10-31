package com.example.hnust_app;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    private DBHelper db;
    private String currentUser;
    private TextView tvName, tvPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        db = new DBHelper(this);
        SharedPreferences sp = getSharedPreferences("LoginState", Context.MODE_PRIVATE);
        currentUser = sp.getString("username", "teacher");

        tvName = findViewById(R.id.tv_name);
        tvPhone = findViewById(R.id.tv_phone);

        // 修改姓名
        findViewById(R.id.item_edit_name).setOnClickListener(v -> {
            EditText et = new EditText(this);
            et.setText(tvName.getText().toString().equals("未设置") ? "" : tvName.getText().toString());
            new AlertDialog.Builder(this).setTitle("修改姓名").setView(et)
                    .setPositiveButton("保存", (d, w) -> {
                        db.updateTeacherInfo(currentUser, et.getText().toString(), tvPhone.getText().toString());
                        Toast.makeText(this, "已保存", Toast.LENGTH_SHORT).show();
                        loadData();
                    }).setNegativeButton("取消", null).show();
        });

        // 修改手机号
        findViewById(R.id.item_edit_phone).setOnClickListener(v -> {
            EditText et = new EditText(this);
            et.setText(tvPhone.getText().toString().equals("未设置") ? "" : tvPhone.getText().toString());
            et.setInputType(InputType.TYPE_CLASS_PHONE);
            new AlertDialog.Builder(this).setTitle("修改手机号").setView(et)
                    .setPositiveButton("保存", (d, w) -> {
                        db.updateTeacherInfo(currentUser, tvName.getText().toString(), et.getText().toString());
                        Toast.makeText(this, "已保存", Toast.LENGTH_SHORT).show();
                        loadData();
                    }).setNegativeButton("取消", null).show();
        });

        // 修改密码
        findViewById(R.id.item_edit_pwd).setOnClickListener(v -> {
            LinearLayout layout = new LinearLayout(this);
            layout.setOrientation(LinearLayout.VERTICAL);
            layout.setPadding(50, 20, 50, 20);
            EditText etOld = new EditText(this); etOld.setHint("原密码");
            EditText etNew = new EditText(this); etNew.setHint("新密码");
            etOld.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            etNew.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            layout.addView(etOld); layout.addView(etNew);

            new AlertDialog.Builder(this).setTitle("修改密码").setView(layout)
                    .setPositiveButton("确定", (d, w) -> {
                        if (db.login(currentUser, etOld.getText().toString())) {
                            db.updatePassword(currentUser, etNew.getText().toString());
                            Toast.makeText(this, "密码修改成功", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this, "原密码错误", Toast.LENGTH_SHORT).show();
                        }
                    }).setNegativeButton("取消", null).show();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    private void loadData() {
        String[] info = db.getTeacherInfo(currentUser);
        tvName.setText(info[0]);
        tvPhone.setText(info[1]);
    }
}