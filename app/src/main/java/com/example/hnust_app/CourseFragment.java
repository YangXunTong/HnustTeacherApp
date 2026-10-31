package com.example.hnust_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.List;

public class CourseFragment extends Fragment {
    private RecyclerView rv;
    private DBHelper dbHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // 复用之前的 activity_course_list.xml 作为这个 Fragment 的布局
        View view = inflater.inflate(R.layout.activity_course_list, container, false);
        dbHelper = new DBHelper(getContext());
        rv = view.findViewById(R.id.recycler_view);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));

        FloatingActionButton fab = view.findViewById(R.id.fab_add_course);
        fab.setOnClickListener(v -> startActivity(new Intent(getActivity(), AddCourseActivity.class)));

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData();
    }

    private void loadData() {
        List<Course> courseList = dbHelper.getAllCourses();
        rv.setAdapter(new CourseAdapter(courseList,
                course -> {
                    Intent intent = new Intent(getActivity(), CourseDetailActivity.class);
                    intent.putExtra("course_name", course.courseName);
                    intent.putExtra("time", course.time);
                    intent.putExtra("location", course.location);
                    intent.putExtra("class_name", course.className);
                    startActivity(intent);
                },
                course -> {
                    new AlertDialog.Builder(getContext())
                            .setTitle("删除课程").setMessage("确定要删除【" + course.courseName + "】吗？")
                            .setPositiveButton("确定删除", (dialog, which) -> {
                                dbHelper.deleteCourse(course.id);
                                loadData();
                            }).setNegativeButton("取消", null).show();
                }
        ));
    }
}