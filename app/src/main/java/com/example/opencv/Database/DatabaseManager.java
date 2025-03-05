package com.example.opencv.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

public class DatabaseManager {
    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;

    public DatabaseManager(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLiteException {
        dbHelper.openDataBase();
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public long insertLesson(String type, String image, String description, String option1, String option2, String option3) {
        ContentValues values = new ContentValues();
        values.put("type", type);
        values.put("image", image);
        values.put("description", description);
        values.put("option1", option1);
        values.put("option2", option2);
        values.put("option3", option3);
        return database.insert("Activity_1_Lecciones", null, values);
    }

    public Cursor getLesson(int id) {
        String[] columns = {"type", "image", "description", "option1", "option2", "option3"};
        return database.query("Activity_1_Lecciones", columns, "id=" + id, null, null, null, null);
    }
    public Cursor getAllLessons() {
        String[] columns = {"id", "type", "image", "description", "option1", "option2", "option3"};
        return database.query("Activity_1_Lecciones", columns, null, null, null, null, null);
    }

    public int updateLesson(int id, String type, String image, String description, String option1, String option2, String option3) {
        ContentValues values = new ContentValues();
        values.put("type", type);
        values.put("image", image);
        values.put("description", description);
        values.put("option1", option1);
        values.put("option2", option2);
        values.put("option3", option3);
        return database.update("Activity_1_Lecciones", values, "id=" + id, null);
    }

    public int deleteLesson(int id) {
        return database.delete("Activity_1_Lecciones", "id=" + id, null);
    }
}
