package com.example.hnust_app;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class AddCourseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);

        DBHelper db = new DBHelper(this);
        EditText etName = findViewById(R.id.et_course_name);
        EditText etTime = findViewById(R.id.et_time);
        EditText etLocation = findViewById(R.id.et_location);
        EditText etClass = findViewById(R.id.et_class_name);

        Button btnSave = findViewById(R.id.btn_save_course);
        btnSave.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String time = etTime.getText().toString().trim();
            String location = etLocation.getText().toString().trim();
            String className = etClass.getText().toString().trim();

            if (name.isEmpty() || time.isEmpty() || location.isEmpty() || className.isEmpty()) {
                Toast.makeText(this, "请填写完整信息", Toast.LENGTH_SHORT).show();
                return;
            }

            // 调用刚才写的添加方法
            db.addCourse(new Course(name, time, location, className));
            Toast.makeText(this, "添加成功", Toast.LENGTH_SHORT).show();
            finish(); // 关掉当前页，自动回到课表页
        });
    }
}