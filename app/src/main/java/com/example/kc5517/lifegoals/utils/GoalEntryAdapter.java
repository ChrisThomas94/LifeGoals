package com.example.kc5517.lifegoals.utils;

import android.content.Context;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.example.kc5517.lifegoals.R;

import java.util.ArrayList;

import static android.view.KeyEvent.ACTION_DOWN;
import static android.view.KeyEvent.KEYCODE_ENTER;

public class GoalEntryAdapter extends BaseAdapter {

    Context context;
    ArrayList<String> goals;
    LayoutInflater inflator = null;
    View v;
    EditText goal;
    ViewHolder vholder;
    int goalMax;

    //Constructor
    public GoalEntryAdapter(Context con, ArrayList<String> goals) {
        super();
        context = con;
        this.goals = goals;
        inflator = LayoutInflater.from(con);

        AppConfig config = new AppConfig();
        goalMax = config.getGoalMax();
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
    public Object getItem(int position) {
        return goals.get(position);
    }

    //Viewholder class to contain inflated xml views
    private class ViewHolder {
        //EditText goal;
        RelativeLayout container;

    }

    public ArrayList<String> getGoals(){

        return goals;
    }

    // Called for each view
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        v = convertView;
        if (convertView == null) {
            //inflate the view for each row of listview
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.adapter_goal, null);
            //ViewHolder object to contain adapter_goal.xml elements

            goal = v.findViewById(R.id.goalText);

            //vholder = new ViewHolder();
            //vholder.goal = v.findViewById(R.id.goalText);
            //vholder.container = v.findViewById(R.id.goalRelativeContainer);

            //set holder to the view
            //v.setTag(vholder);

        } else {
            //vholder = (ViewHolder) v.getTag();
        }

        //vholder.goal.setId(position);
        //Log.d("TAG :", "position " + position);
        //vholder.goal.setText(vholder.goal.getText().toString());
        //goals.set(position, vholder.goal.getText().toString());
        //goals.set(position, goal.getText().toString());

        goal.setText(goals.get(position));


        Log.d("TAG :", "text from array " + goals.get(0));


        //Log.d("TAG :", "goals: " + goals.get(position));

        goal.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                Log.d("TAG :", "text from edit text " + goal.getText().toString());

            }
        });

        return v;
    }

}
