package com.development.kc5517.lifegoals.Database;

public class Entry {

    public static final String TABLE_NAME = "lifeGoals";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_GOALS = "goals";
    public static final String COLUMN_TIMESTAMP = "timestamp";

    private int id;
    private String goals;
    private String timestamp;


    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_GOALS + " TEXT,"
                    + COLUMN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP"
                    + ")";

    public Entry(int id, String goals, String timestamp) {
        this.id = id;
        this.goals = goals;
        this.timestamp = timestamp;
    }

    public int getId() {
        return id;
    }

    public String getGoals() {
        return goals;
    }

    public void setGoals(String goal) {
        this.goals = goals;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
