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

    static final String TABLE_TRAININGS = "trainings";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_TIME = "time";
    public static final String COLUMN_DESC = "description";
    public static final String COLUMN_IMAGE = "image";
    public static final String COLUMN_AUTHOR = "author";

    static final String TABLE_EXERCISES = "exercises";
    public static final String COLUMN_REPS = "reps";
    public static final String COLUMN_SETS = "sets";
    public static final String COLUMN_TIME_PER_SET = "time_per_set";
    public static final String COLUMN_RELAX_TIME_BETWEEN_SETS = "relax_time_between_sets";
    public static final String COLUMN_TRAINING = "training";
    static final String TABLE_FAVORITE_TRAINING_BY_USER = "favorite_training_by_user";
    public static final String COLUMN_USER = "user";



    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, SCHEMA);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE " + TABLE_USERS + " (" + COLUMN_ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_USERNAME
                + " TEXT, " + COLUMN_EMAIL + " TEXT, " + COLUMN_FIRST_NAME
                + " TEXT, " + COLUMN_LAST_NAME + " TEXT, " + COLUMN_PASSWORD
                + " TEXT, " + COLUMN_IMAGE + " TEXT);");
        db.execSQL("CREATE TABLE " + TABLE_TRAININGS + " (" + COLUMN_ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_NAME
                + " TEXT, " + COLUMN_TIME + " INTEGER, " + COLUMN_DESC
                + " TEXT, " + COLUMN_IMAGE + " TEXT, " + COLUMN_AUTHOR
                + " INTEGER, FOREIGN KEY (" + COLUMN_AUTHOR + ") REFERENCES " + TABLE_USERS
                + "(" + COLUMN_ID + "))");
        db.execSQL("CREATE TABLE " + TABLE_EXERCISES + " (" + COLUMN_ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_NAME
                + " TEXT, " + COLUMN_DESC + " TEXT, " + COLUMN_SETS + " INTEGER, "
                + COLUMN_REPS + " INTEGER, " + COLUMN_TIME_PER_SET + " INTEGER, "
                + COLUMN_RELAX_TIME_BETWEEN_SETS + " INTEGER, " + COLUMN_TRAINING
                + " INTEGER, FOREIGN KEY (" + COLUMN_TRAINING + ") REFERENCES " + TABLE_TRAININGS
                + "(" + COLUMN_ID + "))");
        db.execSQL("CREATE TABLE " + TABLE_FAVORITE_TRAINING_BY_USER + " (" + COLUMN_ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_USER
                + " INTEGER, " + COLUMN_TRAINING + " INTEGER, FOREIGN KEY (" + COLUMN_USER
                + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_ID + "), FOREIGN KEY ("
                + COLUMN_TRAINING + ") REFERENCES " + TABLE_TRAININGS + "(" + COLUMN_ID + "))");
        // добавление начальных данных
        db.execSQL("INSERT INTO "+ TABLE_USERS +" (" + COLUMN_USERNAME
                + ", " + COLUMN_EMAIL + ", " + COLUMN_FIRST_NAME + ", "
                + COLUMN_LAST_NAME + ", " + COLUMN_PASSWORD + ", " + COLUMN_IMAGE
                + ") VALUES ('tomsmith', 'tomsmith@gmail.com', 'Том', 'Смит', 'password', null);");
        db.execSQL("INSERT INTO "+ TABLE_TRAININGS +" (" + COLUMN_NAME
                + ", " + COLUMN_TIME + ", " + COLUMN_DESC + ", "
                + COLUMN_IMAGE + ", " + COLUMN_AUTHOR
                + ") VALUES ('run', '30', 'just run', 'empty', '1');");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,  int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRAININGS);
        onCreate(db);
    }
}