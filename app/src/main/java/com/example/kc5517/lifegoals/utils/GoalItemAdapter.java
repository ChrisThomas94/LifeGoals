package com.example.kc5517.lifegoals.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

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
    public Object getItem(int position) {
        return goals.get(position);
    }

    //Viewholder class to contain inflated xml views
    private class ViewHolder {
        TextView title;
        ImageView image;
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
            //vholder.title = (TextView) v.findViewById(R.id.adaptertextview);
            //vholder.image = (ImageView) v.findViewById(R.id.adapterimage);
            //set holder to the view
            v.setTag(vholder);
        } else
            vholder = (ViewHolder) v.getTag();
        //getting MyItem Object for each position
        Goal item = (Goal) goals.get(position);
//set the id to editetxt important line here as it will be helpful to set text according to position
        vholder.title.setId(position);
//setting the values from object to holder views for each row vholder.title.setText(item.getImageheading()); vholder.image.setImageResource(item.getImageid());
        vholder.title.setOnFocusChangeListener(
                new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (!hasFocus) {
                            final int id = v.getId();
                            Goal item = goals.get(id);
                            final EditText field = ((EditText) v);
                            //listforview.get(id).setImageheading(field.getText().toString());
                        }
                    }
                }
        );
        return v;
    }
}
