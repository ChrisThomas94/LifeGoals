package com.example.kc5517.lifegoals.Database;

public class Goals {

    public static final String TABLE_NAME = "goals";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_GOAL = "goal";
    public static final String COLUMN_TIMESTAMP = "timestamp";

    private int id;
    private String goal;
    private String timestamp;


    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_GOAL + " TEXT,"
                    + COLUMN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP"
                    + ")";

    public Goals(int id, String goal, String timestamp) {
        this.id = id;
        this.goal = goal;
        this.timestamp = timestamp;
    }

    public int getId() {
        return id;
    }

    public String getGoal() {
        return goal;
    }

    public void setGoal(String goal) {
        this.goal = goal;
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
