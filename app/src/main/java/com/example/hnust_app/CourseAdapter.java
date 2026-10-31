package com.example.hnust_app;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.VH> {
    private List<Course> list;
    private OnCourseClickListener clickListener;
    private OnCourseLongClickListener longClickListener; // 新增长按接口

    public interface OnCourseClickListener { void onClick(Course course); }
    public interface OnCourseLongClickListener { void onLongClick(Course course); } // 新增

    // 构造方法接收两个监听器
    public CourseAdapter(List<Course> list, OnCourseClickListener clickListener, OnCourseLongClickListener longClickListener) {
        this.list = list;
        this.clickListener = clickListener;
        this.longClickListener = longClickListener;
    }

    @NonNull @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_course, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        Course c = list.get(position);
        holder.tvCourse.setText(c.courseName);
        holder.tvTime.setText("时间: " + c.time);
        holder.tvLocation.setText("地点: " + c.location);
        holder.tvClass.setText("班级: " + c.className);

        // 点击事件
        holder.itemView.setOnClickListener(v -> clickListener.onClick(c));

        // 长按事件
        holder.itemView.setOnLongClickListener(v -> {
            longClickListener.onLongClick(c);
            return true; // 返回true表示消费了该事件，不会触发点击
        });
    }

    @Override public int getItemCount() { return list.size(); }

    public static class VH extends RecyclerView.ViewHolder {
        TextView tvCourse, tvTime, tvLocation, tvClass;
        public VH(@NonNull View itemView) {
            super(itemView);
            tvCourse = itemView.findViewById(R.id.tv_course_name);
            tvTime = itemView.findViewById(R.id.tv_time);
            tvLocation = itemView.findViewById(R.id.tv_location);
            tvClass = itemView.findViewById(R.id.tv_class);
        }
    }
}