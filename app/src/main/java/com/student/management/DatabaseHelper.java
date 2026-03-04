package com.student.management;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME    = "StudentManagement.db";
    private static final int    DATABASE_VERSION = 1;

    public static final String TABLE_STUDENTS = "students";
    public static final String COL_ID         = "id";
    public static final String COL_NAME       = "name";
    public static final String COL_EMAIL      = "email";
    public static final String COL_PHONE      = "phone";

    private static final String SQL_CREATE_TABLE =
            "CREATE TABLE " + TABLE_STUDENTS + " ("
                    + COL_ID    + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COL_NAME  + " TEXT NOT NULL, "
                    + COL_EMAIL + " TEXT NOT NULL, "
                    + COL_PHONE + " TEXT NOT NULL"
                    + ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STUDENTS);
        onCreate(db);
    }

    // INSERT
    public long insertStudent(Student student) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_NAME,  student.getName());
        values.put(COL_EMAIL, student.getEmail());
        values.put(COL_PHONE, student.getPhone());
        return db.insert(TABLE_STUDENTS, null, values);
    }

    // SELECT ALL
    public List<Student> getAllStudents() {
        List<Student> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + TABLE_STUDENTS + " ORDER BY " + COL_NAME + " ASC", null);
        if (cursor.moveToFirst()) {
            do {
                list.add(new Student(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_EMAIL)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_PHONE))
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    // UPDATE
    public int updateStudent(Student student) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_NAME,  student.getName());
        values.put(COL_EMAIL, student.getEmail());
        values.put(COL_PHONE, student.getPhone());
        return db.update(TABLE_STUDENTS, values,
                COL_ID + "=?", new String[]{String.valueOf(student.getId())});
    }

    // DELETE
    public void deleteStudent(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_STUDENTS, COL_ID + "=?", new String[]{String.valueOf(id)});
    }

    public int getStudentCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_STUDENTS, null);
        int count = 0;
        if (cursor.moveToFirst()) count = cursor.getInt(0);
        cursor.close();
        return count;
    }
}