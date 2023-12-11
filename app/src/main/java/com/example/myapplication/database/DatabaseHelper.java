package com.example.myapplication.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "trainings.db"; // название бд
    private static final int SCHEMA = 1; // версия базы данных
    static final String TABLE_USERS = "users"; // название таблицы в бд
    // названия столбцов
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_FIRST_NAME = "first_name";
    public static final String COLUMN_LAST_NAME = "last_name";
    public static final String COLUMN_PASSWORD = "password";



    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, SCHEMA);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE " + TABLE_USERS + " (" + COLUMN_ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_USERNAME
                + " TEXT, " + COLUMN_EMAIL + " TEXT, " + COLUMN_FIRST_NAME
                + " TEXT, " + COLUMN_LAST_NAME + " TEXT, " + COLUMN_PASSWORD
                + " TEXT);");
        // добавление начальных данных
        db.execSQL("INSERT INTO "+ TABLE_USERS +" (" + COLUMN_USERNAME
                + ", " + COLUMN_EMAIL + ", " + COLUMN_FIRST_NAME + ", "
                + COLUMN_LAST_NAME + ", " + COLUMN_PASSWORD
                + ") VALUES ('tomsmith', 'tomsmith@gmail.com', 'Том', 'Смит', 'password');");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,  int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_USERS);
        onCreate(db);
    }
}