package com.example.kc5517.lifegoals.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
        db.execSQL(Entry.CREATE_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + Entry.TABLE_NAME);

        // Create tables again
        onCreate(db);
    }

    public long insertEntry(String goal) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        values.put(Entry.COLUMN_GOALS, goal);

        // insert row
        long id = db.insert(Entry.TABLE_NAME, null, values);

        // close db connection
        db.close();

        Log.d("TAG :", "goal added");

        // return newly inserted row id
        return id;
    }

    public Entry getEntry(long id) {
        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(Entry.TABLE_NAME,
                new String[]{Entry.COLUMN_ID, Entry.COLUMN_GOALS, Entry.COLUMN_TIMESTAMP},
                Entry.COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        // prepare goals object
        Entry goalDB = new Entry(
                cursor.getInt(cursor.getColumnIndex(Entry.COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(Entry.COLUMN_GOALS)),
                cursor.getString(cursor.getColumnIndex(Entry.COLUMN_TIMESTAMP)));

        // close the db connection
        cursor.close();

        return goalDB;
    }

    public List<Entry> getAllEntries() {
        List<Entry> entries = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + Entry.TABLE_NAME + " ORDER BY " +
                Entry.COLUMN_TIMESTAMP + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Entry entryDB = new Entry(cursor.getInt(cursor.getColumnIndex(Entry.COLUMN_ID))
                        ,cursor.getString(cursor.getColumnIndex(Entry.COLUMN_GOALS))
                        ,cursor.getString(cursor.getColumnIndex(Entry.COLUMN_TIMESTAMP)));

                entries.add(entryDB);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return goals list
        return entries;
    }

    public int getEntriesCount() {
        String countQuery = "SELECT  * FROM " + Entry.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        Log.d("TAG :", "get all goals");

        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }

    public int updateGoal(Entry entry) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Entry.COLUMN_GOALS, entry.getGoals());

        // updating row
        return db.update(Entry.TABLE_NAME, values, Entry.COLUMN_ID + " = ?",
                new String[]{String.valueOf(entry.getId())});
    }

    public void deleteGoal(Entry entry) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Entry.TABLE_NAME, Entry.COLUMN_ID + " = ?",
                new String[]{String.valueOf(entry.getId())});
        db.close();
    }

    public void deleteAllEntries() {
        // Select All Query
        String selectQuery = "SELECT  * FROM " + Entry.TABLE_NAME + " ORDER BY " +
                Entry.COLUMN_TIMESTAMP + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Entry entryDB = new Entry(cursor.getInt(cursor.getColumnIndex(Entry.COLUMN_ID))
                        ,cursor.getString(cursor.getColumnIndex(Entry.COLUMN_GOALS))
                        ,cursor.getString(cursor.getColumnIndex(Entry.COLUMN_TIMESTAMP)));

                Log.d("TAG :", "goal deleted");

                deleteGoal(entryDB);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();
    }

    public boolean checkEntryToday() {
        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("yyyy-MM-dd");
        String strDate = mdformat.format(calendar.getTime());

        Cursor cursor = db.query(Entry.TABLE_NAME,
                new String[]{Entry.COLUMN_ID, Entry.COLUMN_GOALS, Entry.COLUMN_TIMESTAMP},
                Entry.COLUMN_TIMESTAMP + "=?",
                new String[]{String.valueOf(strDate)}, null, null, null, null);

        if (cursor != null) {
            return true;
        } else {
            return false;
        }
    }
}
