package com.example.kc5517.lifegoals.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "goals_db";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        // create goals table
        db.execSQL(Goals.CREATE_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + Goals.TABLE_NAME);

        // Create tables again
        onCreate(db);
    }

    public long insertGoal(String goal) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        values.put(Goals.COLUMN_GOAL, goal);

        // insert row
        long id = db.insert(Goals.TABLE_NAME, null, values);

        // close db connection
        db.close();

        // return newly inserted row id
        return id;
    }

    public Goals getGoals(long id) {
        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(Goals.TABLE_NAME,
                new String[]{Goals.COLUMN_ID, Goals.COLUMN_GOAL, Goals.COLUMN_TIMESTAMP},
                Goals.COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        // prepare goals object
        Goals goalDB = new Goals(
                cursor.getInt(cursor.getColumnIndex(Goals.COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(Goals.COLUMN_GOAL)),
                cursor.getString(cursor.getColumnIndex(Goals.COLUMN_TIMESTAMP)));

        // close the db connection
        cursor.close();

        return goalDB;
    }

    public List<Goals> getAllGoals() {
        List<Goals> goals = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + Goals.TABLE_NAME + " ORDER BY " +
                Goals.COLUMN_TIMESTAMP + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Goals goalsDB = new Goals(cursor.getInt(cursor.getColumnIndex(Goals.COLUMN_ID))
                        ,cursor.getString(cursor.getColumnIndex(Goals.COLUMN_GOAL))
                        ,cursor.getString(cursor.getColumnIndex(Goals.COLUMN_TIMESTAMP)));

                goals.add(goalsDB);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return goals list
        return goals;
    }

    public int getGoalsCount() {
        String countQuery = "SELECT  * FROM " + Goals.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }

    public int updateGoal(Goals goals) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Goals.COLUMN_GOAL, goals.getGoal());

        // updating row
        return db.update(Goals.TABLE_NAME, values, Goals.COLUMN_ID + " = ?",
                new String[]{String.valueOf(goals.getId())});
    }

    public void deleteGoal(Goals goals) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Goals.TABLE_NAME, Goals.COLUMN_ID + " = ?",
                new String[]{String.valueOf(goals.getId())});
        db.close();
    }
}
