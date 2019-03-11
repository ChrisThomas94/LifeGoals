package com.development.kc5517.lifegoals.utils;

public class Goal {
    private String goal;
    private int id;

    public Goal(int id, String goal)
    {
        this.id=id;
        this.goal=goal;
    }

    public String getGoal() {
        return goal;
    }

    public int getGoalId() {
        return id;
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }

    public void setGoalId(int id) {
        this.id = id;
    }
}
