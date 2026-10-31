package com.example.hnust_app;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.List;

public class CourseListActivity extends AppCompatActivity {

    private RecyclerView rv;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_list);

        dbHelper = new DBHelper(this);
        rv = findViewById(R.id.recycler_view);
        rv.setLayoutManager(new LinearLayoutManager(this));

        FloatingActionButton fab = findViewById(R.id.fab_add_course);
        fab.setOnClickListener(v -> startActivity(new Intent(this, AddCourseActivity.class)));
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    private void loadData() {
        List<Course> courseList = dbHelper.getAllCourses();

        // 传入两个监听器：点击和长按
        rv.setAdapter(new CourseAdapter(courseList,
                // 1. 点击事件（跳转详情）
                course -> {
                    Intent intent = new Intent(this, CourseDetailActivity.class);
                    intent.putExtra("course_name", course.courseName);
                    intent.putExtra("time", course.time);
                    intent.putExtra("location", course.location);
                    intent.putExtra("class_name", course.className);
                    startActivity(intent);
                },
                // 2. 长按事件（弹出删除确认框）
                course -> {
                    new AlertDialog.Builder(this)
                            .setTitle("删除课程")
                            .setMessage("确定要删除【" + course.courseName + "】吗？")
                            .setPositiveButton("确定删除", (dialog, which) -> {
                                dbHelper.deleteCourse(course.id); // 从数据库删除
                                loadData(); // 重新加载列表刷新界面
                            })
                            .setNegativeButton("取消", null)
                            .show();
                }
        ));
    }
}