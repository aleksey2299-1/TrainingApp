package com.example.myapplication.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.myapplication.exercise.Exercise;
import com.example.myapplication.training.Training;
import com.example.myapplication.user.User;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class DatabaseAdapter {

    private DatabaseHelper dbHelper;
    private SQLiteDatabase database;

    public DatabaseAdapter(Context context){
        dbHelper = new DatabaseHelper(context.getApplicationContext());
    }

    public DatabaseAdapter open(){
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close(){
        dbHelper.close();
    }

    private Cursor getAllUserEntries(){
        String[] columns = new String[] {DatabaseHelper.COLUMN_ID, DatabaseHelper.COLUMN_USERNAME,
                DatabaseHelper.COLUMN_EMAIL, DatabaseHelper.COLUMN_FIRST_NAME,
                DatabaseHelper.COLUMN_LAST_NAME, DatabaseHelper.COLUMN_PASSWORD,
                DatabaseHelper.COLUMN_IMAGE};
        return database.query(DatabaseHelper.TABLE_USERS, columns, null, null, null, null, null);
    }

    public ArrayList<User> getUsers(){
        ArrayList<User> users = new ArrayList<>();
        Cursor cursor = getAllUserEntries();
        while (cursor.moveToNext()){
            Bitmap image = null;
            int id = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_ID));
            String username = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_USERNAME));
            String email = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_EMAIL));
            String firstName = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_FIRST_NAME));
            String lastName = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_LAST_NAME));
            String password = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_PASSWORD));
            byte[] blob = cursor.getBlob(cursor.getColumnIndex(DatabaseHelper.COLUMN_IMAGE));
            if(blob!=null) {
                image = BitmapFactory.decodeByteArray(blob, 0, blob.length);
            }
            users.add(new User(id, username, email, firstName, lastName, password, image));
        }
        cursor.close();
        return users;
    }

    public long getUserCount(){
        return DatabaseUtils.queryNumEntries(database, DatabaseHelper.TABLE_USERS);
    }

    public User getUser(long id){
        User user = null;
        String query = String.format("SELECT * FROM %s WHERE %s=?",DatabaseHelper.TABLE_USERS, DatabaseHelper.COLUMN_ID);
        Cursor cursor = database.rawQuery(query, new String[]{ String.valueOf(id)});
        if(cursor.moveToFirst()){
            Bitmap image = null;
            String username = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_USERNAME));
            String email = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_EMAIL));
            String firstName = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_FIRST_NAME));
            String lastName = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_LAST_NAME));
            String password = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_PASSWORD));
            byte[] blob = cursor.getBlob(cursor.getColumnIndex(DatabaseHelper.COLUMN_IMAGE));
            if(blob!=null) {
                image = BitmapFactory.decodeByteArray(blob, 0, blob.length);
            }
            user = new User(id, username, email, firstName, lastName, password, image);
        }
        cursor.close();
        return user;
    }

    public User getUserByUsername(String username){
        User user = null;
        String query = String.format("SELECT * FROM %s WHERE %s=?",DatabaseHelper.TABLE_USERS, DatabaseHelper.COLUMN_USERNAME);
        Cursor cursor = database.rawQuery(query, new String[]{ String.valueOf(username)});
        if(cursor.moveToFirst()){
            Bitmap image = null;
            Long id = cursor.getLong(cursor.getColumnIndex(DatabaseHelper.COLUMN_ID));
            String email = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_EMAIL));
            String firstName = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_FIRST_NAME));
            String lastName = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_LAST_NAME));
            String password = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_PASSWORD));
            byte[] blob = cursor.getBlob(cursor.getColumnIndex(DatabaseHelper.COLUMN_IMAGE));
            if(blob!=null) {
                image = BitmapFactory.decodeByteArray(blob, 0, blob.length);
            }
            user = new User(id, username, email, firstName, lastName, password, image);
        }
        cursor.close();
        return user;
    }

    public long insertUser(User user){

        ContentValues cv = new ContentValues();

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        if(user.getImage()!=null) {
            user.getImage().compress(Bitmap.CompressFormat.PNG, 100, out);
            byte[] buffer = out.toByteArray();
            cv.put(DatabaseHelper.COLUMN_IMAGE, buffer);
        }

        cv.put(DatabaseHelper.COLUMN_USERNAME, user.getUsername());
        cv.put(DatabaseHelper.COLUMN_EMAIL, user.getEmail());
        cv.put(DatabaseHelper.COLUMN_FIRST_NAME, user.getFirstName());
        cv.put(DatabaseHelper.COLUMN_LAST_NAME, user.getLastName());
        cv.put(DatabaseHelper.COLUMN_PASSWORD, user.getPassword());

        return database.insert(DatabaseHelper.TABLE_USERS, null, cv);
    }

    public long deleteUser(long userId){

        String whereClause = "_id = ?";
        String[] whereArgs = new String[]{String.valueOf(userId)};
        return database.delete(DatabaseHelper.TABLE_USERS, whereClause, whereArgs);
    }

    public long updateUser(User user){

        String whereClause = DatabaseHelper.COLUMN_ID + "=" + user.getId();
        ContentValues cv = new ContentValues();

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        if(user.getImage()!=null) {
            user.getImage().compress(Bitmap.CompressFormat.PNG, 100, out);
            byte[] buffer = out.toByteArray();
            cv.put(DatabaseHelper.COLUMN_IMAGE, buffer);
        }

        cv.put(DatabaseHelper.COLUMN_USERNAME, user.getUsername());
        cv.put(DatabaseHelper.COLUMN_EMAIL, user.getEmail());
        cv.put(DatabaseHelper.COLUMN_FIRST_NAME, user.getFirstName());
        cv.put(DatabaseHelper.COLUMN_LAST_NAME, user.getLastName());
        cv.put(DatabaseHelper.COLUMN_PASSWORD, user.getPassword());
        return database.update(DatabaseHelper.TABLE_USERS, cv, whereClause, null);
    }

    private Cursor getAllTrainingEntries(){
        String[] columns = new String[] {DatabaseHelper.COLUMN_ID, DatabaseHelper.COLUMN_NAME,
                DatabaseHelper.COLUMN_TIME, DatabaseHelper.COLUMN_DESC,
                DatabaseHelper.COLUMN_IMAGE, DatabaseHelper.COLUMN_AUTHOR};
        return database.query(DatabaseHelper.TABLE_TRAININGS, columns, null, null, null, null, null);
    }

    public ArrayList<Training> getTrainings(){
        ArrayList<Training> trainings = new ArrayList<>();
        Cursor cursor = getAllTrainingEntries();
        while (cursor.moveToNext()){
            Bitmap image = null;
            int id = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_ID));
            String name = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_NAME));
            int time = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_TIME));
            String desc = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_DESC));
            byte[] blob = cursor.getBlob(cursor.getColumnIndex(DatabaseHelper.COLUMN_IMAGE));
            image = BitmapFactory.decodeByteArray(blob, 0, blob.length);
            int author = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_AUTHOR));
            trainings.add(new Training(id, name, time, desc, image, author));
        }
        cursor.close();
        return trainings;
    }

    public long getTrainingCount(){
        return DatabaseUtils.queryNumEntries(database, DatabaseHelper.TABLE_TRAININGS);
    }

    public Training getTraining(long id){
        Training training = null;
        String query = String.format("SELECT * FROM %s WHERE %s=?",DatabaseHelper.TABLE_TRAININGS, DatabaseHelper.COLUMN_ID);
        Cursor cursor = database.rawQuery(query, new String[]{ String.valueOf(id)});
        if(cursor.moveToFirst()){
            Bitmap image = null;
            String name = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_NAME));
            int time = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_TIME));
            String desc = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_DESC));
            byte[] blob = cursor.getBlob(cursor.getColumnIndex(DatabaseHelper.COLUMN_IMAGE));
            image = BitmapFactory.decodeByteArray(blob, 0, blob.length);
            int author = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_AUTHOR));
            training = new Training(id, name, time, desc, image, author);
        }
        cursor.close();
        return training;
    }

    public long insertTraining(Training training){

        ContentValues cv = new ContentValues();

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        training.getImage().compress(Bitmap.CompressFormat.PNG, 100, out);
        byte[] buffer = out.toByteArray();

        cv.put(DatabaseHelper.COLUMN_NAME, training.getName());
        cv.put(DatabaseHelper.COLUMN_TIME, training.getTime());
        cv.put(DatabaseHelper.COLUMN_DESC, training.getDesc());
        cv.put(DatabaseHelper.COLUMN_IMAGE, buffer);
        cv.put(DatabaseHelper.COLUMN_AUTHOR, training.getAuthor());

        return  database.insert(DatabaseHelper.TABLE_TRAININGS, null, cv);
    }

    public long deleteTraining(long trainingId){

        String whereClause = "_id = ?";
        String[] whereArgs = new String[]{String.valueOf(trainingId)};
        return database.delete(DatabaseHelper.TABLE_TRAININGS, whereClause, whereArgs);
    }

    public long updateTraining(Training training){

        String whereClause = DatabaseHelper.COLUMN_ID + "=" + training.getId();
        ContentValues cv = new ContentValues();

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        training.getImage().compress(Bitmap.CompressFormat.PNG, 100, out);
        byte[] buffer = out.toByteArray();

        cv.put(DatabaseHelper.COLUMN_NAME, training.getName());
        cv.put(DatabaseHelper.COLUMN_TIME, training.getTime());
        cv.put(DatabaseHelper.COLUMN_DESC, training.getDesc());
        cv.put(DatabaseHelper.COLUMN_IMAGE, buffer);
        cv.put(DatabaseHelper.COLUMN_AUTHOR, training.getAuthor());
        return database.update(DatabaseHelper.TABLE_TRAININGS, cv, whereClause, null);
    }

    public Boolean checkUsername(String username){
        SQLiteDatabase myDatabase = dbHelper.getWritableDatabase();
        Cursor cursor = myDatabase.rawQuery("SELECT * FROM users WHERE username = ?", new String[]{username});
        if(cursor.getCount() > 0) {
            return true;
        }else {
            return false;
        }
    }
    public Boolean checkEmail(String email){
        SQLiteDatabase myDatabase = dbHelper.getWritableDatabase();
        Cursor cursor = myDatabase.rawQuery("SELECT * FROM users WHERE email = ?", new String[]{email});
        if(cursor.getCount() > 0) {
            return true;
        }else {
            return false;
        }
    }

    public Boolean checkUsernamePassword(String username, String password){
        SQLiteDatabase myDatabase = dbHelper.getWritableDatabase();
        Cursor cursor = myDatabase.rawQuery("SELECT * FROM users WHERE username = ? AND password = ?", new String[]{username, password});
        if (cursor.getCount() > 0) {
            return true;
        }else {
            return false;
        }
    }

    private Cursor getAllExercisesEntries(){
        String[] columns = new String[] {DatabaseHelper.COLUMN_ID, DatabaseHelper.COLUMN_NAME,
                DatabaseHelper.COLUMN_DESC, DatabaseHelper.COLUMN_REPS,
                DatabaseHelper.COLUMN_SETS, DatabaseHelper.COLUMN_TIME_PER_SET,
                DatabaseHelper.COLUMN_RELAX_TIME_BETWEEN_SETS, DatabaseHelper.COLUMN_TRAINING};
        return database.query(DatabaseHelper.TABLE_EXERCISES, columns, null, null, null, null, null);
    }

    public ArrayList<Exercise> getExercises(){
        ArrayList<Exercise> exercises = new ArrayList<>();
        Cursor cursor = getAllExercisesEntries();
        while (cursor.moveToNext()){
            int id = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_ID));
            String name = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_NAME));
            String desc = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_DESC));
            int reps = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_REPS));
            int sets = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_SETS));
            int timePerSet = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_TIME_PER_SET));
            int relaxTimeBetweenSets = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_RELAX_TIME_BETWEEN_SETS));
            int trainingId = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_TRAINING));
            exercises.add(new Exercise(id, name, desc, reps, sets, timePerSet, relaxTimeBetweenSets, trainingId));
        }
        cursor.close();
        return exercises;
    }

    public long getExercisesCount(){
        return DatabaseUtils.queryNumEntries(database, DatabaseHelper.TABLE_EXERCISES);
    }

    public Exercise getExercise(long id){
        Exercise exercise = null;
        String query = String.format("SELECT * FROM %s WHERE %s=?",DatabaseHelper.TABLE_EXERCISES, DatabaseHelper.COLUMN_ID);
        Cursor cursor = database.rawQuery(query, new String[]{ String.valueOf(id)});
        if(cursor.moveToFirst()){
            String name = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_NAME));
            String desc = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_DESC));
            int reps = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_REPS));
            int sets = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_SETS));
            int timePerSet = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_TIME_PER_SET));
            int relaxTimeBetweenSets = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_RELAX_TIME_BETWEEN_SETS));
            int trainingId = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_TRAINING));
            exercise = new Exercise(id, name, desc, reps, sets, timePerSet, relaxTimeBetweenSets, trainingId);
        }
        cursor.close();
        return exercise;
    }

    public ArrayList<Exercise> getExercisesByTrainingId(long trainingId){
        ArrayList<Exercise> exercises = new ArrayList<>();
        String query = String.format("SELECT * FROM %s WHERE %s=?",DatabaseHelper.TABLE_EXERCISES, DatabaseHelper.COLUMN_TRAINING);
        Cursor cursor = database.rawQuery(query, new String[]{ String.valueOf(trainingId)});
        while (cursor.moveToNext()){
            int id = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_ID));
            String name = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_NAME));
            String desc = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_DESC));
            int reps = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_REPS));
            int sets = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_SETS));
            int timePerSet = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_TIME_PER_SET));
            int relaxTimeBetweenSets = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_RELAX_TIME_BETWEEN_SETS));
            exercises.add(new Exercise(id, name, desc, reps, sets, timePerSet, relaxTimeBetweenSets, trainingId));
        }
        cursor.close();
        return exercises;
    }

    public long insertExercise(Exercise exercise){

        ContentValues cv = new ContentValues();

        cv.put(DatabaseHelper.COLUMN_NAME, exercise.getName());
        cv.put(DatabaseHelper.COLUMN_DESC, exercise.getDesc());
        cv.put(DatabaseHelper.COLUMN_REPS, exercise.getReps());
        cv.put(DatabaseHelper.COLUMN_SETS, exercise.getSets());
        cv.put(DatabaseHelper.COLUMN_TIME_PER_SET, exercise.getTimePerSet());
        cv.put(DatabaseHelper.COLUMN_RELAX_TIME_BETWEEN_SETS, exercise.getRelaxTimeBetweenSets());
        cv.put(DatabaseHelper.COLUMN_TRAINING, exercise.getTrainingId());

        return database.insert(DatabaseHelper.TABLE_EXERCISES, null, cv);
    }

    public long deleteExercise(long exerciseId){

        String whereClause = "_id = ?";
        String[] whereArgs = new String[]{String.valueOf(exerciseId)};
        return database.delete(DatabaseHelper.TABLE_EXERCISES, whereClause, whereArgs);
    }

    public long updateExercise(Exercise exercise){

        String whereClause = DatabaseHelper.COLUMN_ID + "=" + exercise.getId();
        ContentValues cv = new ContentValues();

        cv.put(DatabaseHelper.COLUMN_NAME, exercise.getName());
        cv.put(DatabaseHelper.COLUMN_DESC, exercise.getDesc());
        cv.put(DatabaseHelper.COLUMN_REPS, exercise.getReps());
        cv.put(DatabaseHelper.COLUMN_SETS, exercise.getSets());
        cv.put(DatabaseHelper.COLUMN_TIME_PER_SET, exercise.getTimePerSet());
        cv.put(DatabaseHelper.COLUMN_RELAX_TIME_BETWEEN_SETS, exercise.getRelaxTimeBetweenSets());
        cv.put(DatabaseHelper.COLUMN_TRAINING, exercise.getTrainingId());
        return database.update(DatabaseHelper.TABLE_EXERCISES, cv, whereClause, null);
    }
}