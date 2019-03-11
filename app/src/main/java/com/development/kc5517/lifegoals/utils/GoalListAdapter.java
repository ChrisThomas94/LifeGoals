package com.development.kc5517.lifegoals.utils;

import android.content.Context;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import com.development.kc5517.lifegoals.R;

import java.util.ArrayList;
import static android.view.KeyEvent.ACTION_DOWN;
import static android.view.KeyEvent.KEYCODE_ENTER;

public class GoalListAdapter extends BaseAdapter {

    Context context;
    ArrayList<String> goals;
    LayoutInflater inflator = null;
    View v;
    ViewHolder vholder;
    int goalMax;

    //Constructor
    public GoalListAdapter(Context con, ArrayList<String> goals) {
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
        EditText goal;
    }

    // Called for each view
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        v = convertView;
        if (convertView == null) {

            //inflate the view for each row of listview
            v = inflator.inflate(R.layout.adapter_goal, null);

            //ViewHolder object to contain myadapter.xml elements
            vholder = new ViewHolder();
            vholder.goal = v.findViewById(R.id.goalText);

            //set holder to the view
            v.setTag(vholder);

        } else {
            vholder = (ViewHolder) v.getTag();
        }

        //getting MyItem Object for each position
        //set the id to editetxt important line here as it will be helpful to set text according to position
        vholder.goal.setId(position);
        vholder.goal.setText(goals.get(position));

        return v;
    }

    private EditText.OnKeyListener enterListener = new EditText.OnKeyListener(){
        @Override
        public boolean onKey(View view, int i, KeyEvent keyEvent){
            //If the keyevent is a key-down event on the "enter" button
            if ((keyEvent.getAction() == ACTION_DOWN) && (keyEvent.getKeyCode() == KEYCODE_ENTER)) {
                Log.d("TAG :", "ENTER PRESSED");

                if(goals.size() < goalMax){
                    goals.add(goals.size()-1,"I will...");

                }

                return true;
            }
            return false;
        }
    };

    private EditText.OnFocusChangeListener focusChangeListener = new View.OnFocusChangeListener(){
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {
                final int id = v.getId();
                //Goal item = goals.get(id);
                final EditText field = ((EditText) v);
            }
        }
    };

}
