package com.example.kc5517.lifegoals.utils;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.RelativeLayout;
import com.example.kc5517.lifegoals.R;
import java.util.ArrayList;

public class GoalItemAdapter extends BaseAdapter {

    Context context;
    ArrayList<Goal> goals;
    LayoutInflater inflator = null;
    View v;
    ViewHolder vholder;

    //Constructor
    public GoalItemAdapter(Context con, ArrayList<Goal> goals) {
        super();
        context = con;
        this.goals = goals;
        inflator = LayoutInflater.from(con);
    }

    // return position here
    @Override
    public long getItemId(int position) {
        return position;
    }

    // return size of list
    @Override
    public int getCount() {
        return goals.size();
    }

    //get Object from each position
    @Override
    public Goal getItem(int position) {
        return goals.get(position);
    }

    //Viewholder class to contain inflated xml views
    private class ViewHolder {
        EditText goal;
        RelativeLayout container;
    }

    // Called for each view
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        v = convertView;
        if (convertView == null) {
            //inflate the view for each row of listview
            v = inflator.inflate(R.layout.adapter_goal, null);

            //ViewHolder object to contain myadapter.xml elements
            vholder = new ViewHolder();
            vholder.goal = v.findViewById(R.id.goalText);
            vholder.container = v.findViewById(R.id.goalRelativeContainer);

            //set holder to the view
            v.setTag(vholder);
        } else {
            vholder = (ViewHolder) v.getTag();
        }

        //getting MyItem Object for each position
        Goal item = goals.get(position);

        if(!vholder.goal.getText().toString().equals("")) {
            vholder.goal.setId(position);
            item.setGoal(vholder.goal.getText().toString());
            goals.set(position, item);
            item.setGoalId(position);
            Log.d("TAG :", "Goal Added to list " + vholder.goal.getText().toString());
        }

        return v;
    }
}
