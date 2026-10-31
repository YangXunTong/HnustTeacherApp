package com.example.hnust_app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

public class ProfileFragment extends Fragment {

    private TextView tvUsername, tvWorkId, tvAvatar;
    private String currentUser;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        SharedPreferences sp = getContext().getSharedPreferences("LoginState", Context.MODE_PRIVATE);
        currentUser = sp.getString("username", "teacher");

        tvUsername = view.findViewById(R.id.tv_username);
        tvWorkId = view.findViewById(R.id.tv_work_id);
        tvAvatar = view.findViewById(R.id.tv_avatar);

        // 跳转到账号设置
        view.findViewById(R.id.item_setting).setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), SettingsActivity.class));
        });

        // 退出登录
        view.findViewById(R.id.btn_logout).setOnClickListener(v -> {
            new AlertDialog.Builder(getContext()).setTitle("提示").setMessage("确定退出？")
                    .setPositiveButton("退出", (dialog, which) -> {
                        SharedPreferences.Editor editor = sp.edit();
                        editor.clear();
                        editor.apply();
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }).setNegativeButton("取消", null).show();
        });

        return view;
    }

    // 每次页面显示时刷新数据
    @Override
    public void onResume() {
        super.onResume();
        if (getContext() == null) return;

        DBHelper db = new DBHelper(getContext());
        String[] info = db.getTeacherInfo(currentUser);

        String name = info[0];
        if (name == null || name.equals("未设置") || name.isEmpty()) {
            tvUsername.setText(currentUser);
            tvAvatar.setText("师");
        } else {
            tvUsername.setText(name);
            tvAvatar.setText(name.substring(0, 1)); // 取第一个字做头像
        }
        tvWorkId.setText("工号: " + currentUser);
    }
}