package com.example.hnust_app;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class StudentListActivity extends AppCompatActivity {

    private RecyclerView rv;
    private DBHelper dbHelper;
    private String className;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_list);

        className = getIntent().getStringExtra("class_name");
        setTitle(className + " 学生名单");

        dbHelper = new DBHelper(this);
        rv = findViewById(R.id.recycler_view);
        rv.setLayoutManager(new LinearLayoutManager(this));

        // 绑定一键导入按钮
        findViewById(R.id.btn_import_all).setOnClickListener(v -> {
            dbHelper.importRegularStudents(className);
            Toast.makeText(this, "常规名单同步成功！", Toast.LENGTH_SHORT).show();
            loadData(); // 刷新列表
        });

        loadData();
    }

    // 提取出加载数据的方法，方便随时刷新
    private void loadData() {
        List<Student> students = dbHelper.getStudentsByClass(className);
        rv.setAdapter(new StudentAdapter(students));
    }
}