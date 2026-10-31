package com.example.hnust_app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {
    // 版本号升级到 3，强制刷新表结构
    public DBHelper(Context context) {
        super(context, "HnustTeacher.db", null, 3);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 老师账号表 (带姓名、手机号)
        db.execSQL("CREATE TABLE teacher (username TEXT PRIMARY KEY, password TEXT, name TEXT, phone TEXT)");
        db.execSQL("INSERT INTO teacher (username, password, name, phone) VALUES ('teacher', '123456', '张老师', '13800138000')");

        // 课表
        db.execSQL("CREATE TABLE course (id INTEGER PRIMARY KEY AUTOINCREMENT, course_name TEXT, time TEXT, location TEXT, class_name TEXT)");
        db.execSQL("INSERT INTO course (course_name, time, location, class_name) VALUES ('Android移动开发', '周一 1-2节', '逸夫楼 302', '计科2101班')");
        db.execSQL("INSERT INTO course (course_name, time, location, class_name) VALUES ('Java程序设计', '周三 3-4节', '逸夫楼 105', '软工2102班')");

        // 学生表
        db.execSQL("CREATE TABLE student (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, stu_id TEXT, class_name TEXT)");
        db.execSQL("INSERT INTO student (name, stu_id, class_name) VALUES ('张三', '2101001', '计科2101班')");
        db.execSQL("INSERT INTO student (name, stu_id, class_name) VALUES ('李四', '2101002', '计科2101班')");
        db.execSQL("INSERT INTO student (name, stu_id, class_name) VALUES ('王五', '2102001', '软工2102班')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 如果版本变了，直接删表重建（开发阶段最省事的做法）
        db.execSQL("DROP TABLE IF EXISTS teacher");
        db.execSQL("DROP TABLE IF EXISTS course");
        db.execSQL("DROP TABLE IF EXISTS student");
        onCreate(db);
    }

    public boolean login(String u, String p) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM teacher WHERE username=? AND password=?", new String[]{u, p});
        boolean ok = c.getCount() > 0;
        c.close();
        return ok;
    }

    public void registerTeacher(String u, String p) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put("username", u);
        v.put("password", p);
        v.put("name", "新老师");
        v.put("phone", "未设置");
        db.insert("teacher", null, v);
        db.close();
    }

    // 找回密码
    public String getPasswordByUsername(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT password FROM teacher WHERE username=?", new String[]{username});
        String pwd = null;
        if (c.moveToFirst()) pwd = c.getString(0);
        c.close();
        return pwd;
    }

    // 获取老师姓名和手机号
    public String[] getTeacherInfo(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT name, phone FROM teacher WHERE username=?", new String[]{username});
        String[] info = new String[]{"未设置", "未设置"};
        if (c.moveToFirst()) {
            info[0] = c.getString(0) == null ? "未设置" : c.getString(0);
            info[1] = c.getString(1) == null ? "未设置" : c.getString(1);
        }
        c.close();
        return info;
    }

    // 更新姓名和手机号
    public void updateTeacherInfo(String username, String name, String phone) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put("name", name);
        v.put("phone", phone);
        db.update("teacher", v, "username=?", new String[]{username});
        db.close();
    }

    // 更新密码
    public void updatePassword(String username, String newPwd) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put("password", newPwd);
        db.update("teacher", v, "username=?", new String[]{username});
        db.close();
    }

    // 课表相关
    public List<Course> getAllCourses() {
        List<Course> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM course", null);
        while (c.moveToNext()) {
            list.add(new Course(c.getInt(0), c.getString(1), c.getString(2), c.getString(3), c.getString(4)));
        }
        c.close();
        return list;
    }

    public void addCourse(Course course) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("course_name", course.courseName);
        values.put("time", course.time);
        values.put("location", course.location);
        values.put("class_name", course.className);
        db.insert("course", null, values);
        db.close();
    }

    public void deleteCourse(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("course", "id=?", new String[]{String.valueOf(id)});
        db.close();
    }

    // 学生相关
    public List<Student> getStudentsByClass(String className) {
        List<Student> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM student WHERE class_name=?", new String[]{className});
        while (c.moveToNext()) {
            list.add(new Student(c.getString(1), c.getString(2), c.getString(3)));
        }
        c.close();
        return list;
    }

    public void addStudent(Student student) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", student.name);
        values.put("stu_id", student.stuId);
        values.put("class_name", student.className);
        db.insert("student", null, values);
        db.close();
    }

    // 一键批量导入班级常规学生（模拟教务系统下发）
    public void importRegularStudents(String className) {
        SQLiteDatabase db = this.getWritableDatabase();

        // 防重复导入检查：如果这个班已经有超过5个学生，就认为已经导入过了
        Cursor c = db.rawQuery("SELECT count(*) FROM student WHERE class_name=?", new String[]{className});
        if (c.moveToFirst() && c.getInt(0) > 5) {
            c.close();
            db.close();
            return; // 已经导入过了，直接返回
        }
        c.close();

        // 批量插入 15 个模拟学生
        for (int i = 1; i <= 15; i++) {
            ContentValues v = new ContentValues();
            v.put("name", "常规学生" + i);
            v.put("stu_id", "202401" + (i < 10 ? "0" + i : i));
            v.put("class_name", className);
            db.insert("student", null, v);
        }
        db.close();
    }
}
