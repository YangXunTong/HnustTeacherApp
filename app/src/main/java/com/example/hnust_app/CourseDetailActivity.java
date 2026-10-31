package com.example.hnust_app; // 注意你的包名

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class CourseDetailActivity extends AppCompatActivity {

    private String className; // 提取成全局变量，因为弹窗添加学生时需要用到
    private DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);

        db = new DBHelper(this);

        // 获取上一页传来的数据
        String courseName = getIntent().getStringExtra("course_name");
        String time = getIntent().getStringExtra("time");
        String location = getIntent().getStringExtra("location");
        className = getIntent().getStringExtra("class_name"); // 赋值给全局变量

        // 显示数据
        ((TextView)findViewById(R.id.tv_title)).setText(courseName);
        ((TextView)findViewById(R.id.tv_time)).setText("上课时间：" + time);
        ((TextView)findViewById(R.id.tv_location)).setText("上课地点：" + location);
        ((TextView)findViewById(R.id.tv_class)).setText("授课班级：" + className);

        // 1. 点击“查看名单”
        findViewById(R.id.btn_view_students).setOnClickListener(v -> {
            Intent intent = new Intent(this, StudentListActivity.class);
            intent.putExtra("class_name", className);
            startActivity(intent);
        });

        // 2. 点击“加入学生信息”（弹窗）
        findViewById(R.id.btn_add_student).setOnClickListener(v -> {
            showAddStudentDialog();
        });
    }

    // 弹窗添加学生的方法
    private void showAddStudentDialog() {
        // 动态构建弹窗里的输入框
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 20, 50, 20);

        EditText etName = new EditText(this);
        etName.setHint("请输入学生姓名");

        EditText etStuId = new EditText(this);
        etStuId.setHint("请输入学号");
        etStuId.setInputType(InputType.TYPE_CLASS_NUMBER); // 键盘只弹数字

        layout.addView(etName);
        layout.addView(etStuId);

        new AlertDialog.Builder(this)
                .setTitle("向【" + className + "】添加学生")
                .setView(layout)
                .setPositiveButton("确定添加", (dialog, which) -> {
                    String name = etName.getText().toString().trim();
                    String stuId = etStuId.getText().toString().trim();

                    if (name.isEmpty() || stuId.isEmpty()) {
                        Toast.makeText(this, "姓名和学号不能为空", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // 存入数据库
                    db.addStudent(new Student(name, stuId, className));
                    Toast.makeText(this, "添加成功！", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("取消", null)
                .show();
    }
}