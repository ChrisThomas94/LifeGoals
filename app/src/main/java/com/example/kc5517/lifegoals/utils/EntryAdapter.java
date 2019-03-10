package com.example.kc5517.lifegoals.utils;

import android.app.Application;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.kc5517.lifegoals.Database.Entry;
import com.example.kc5517.lifegoals.R;
import com.example.kc5517.lifegoals.view.MainActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;


/**
 * Created by Chris on 19-Mar-17.
 *
 */

public class EntryAdapter extends PagerAdapter {

    private Context context;
    private List<Entry> entryList;
    private LayoutInflater mLayoutInflater;
    private boolean isPagingEnabled = true;
    private GoalListAdapter goalAdapter;
    private ListView goalListView;
    private ArrayList<String> goalList = new ArrayList<>();

    public EntryAdapter(Context context, List<Entry> entryList) {
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
        this.entryList = entryList;

        Log.d("TAG :", "entry adapter");

    }

    @Override
    public int getCount() {
        return entryList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view == object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        View itemView = mLayoutInflater.inflate(R.layout.adapter_entry, null);

        RelativeLayout goalBackground = itemView.findViewById(R.id.adapter_goals);
        TextView goalDate = itemView.findViewById(R.id.goalDate);

        //formatt date
        String tStamp = formatDateTime(context, entryList.get(position).getTimestamp());

        //display timestamp on entry
        goalDate.setText(tStamp);
        //goalDate.setText(entryList.get(position).getTimestamp());

        //display goals on entry
        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        Gson gson = new Gson();

        goalList = gson.fromJson(entryList.get(position).getGoals(), type);

        goalAdapter = new GoalListAdapter(itemView.getContext(), goalList);
        goalListView = itemView.findViewById(R.id.addGoalsList);
        goalListView.setAdapter(goalAdapter);

        Log.d("TAG :", "entry adapter");
        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }

    public static String formatDateTime(Context context, String timeToFormat) {

        String finalDateTime = "";

        SimpleDateFormat iso8601Format = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");

        Date date = null;
        if (timeToFormat != null) {
            try {
                date = iso8601Format.parse(timeToFormat);
            } catch (ParseException e) {
                date = null;
            }

            if (date != null) {
                long when = date.getTime();
                int flags = 0;
                //flags |= android.text.format.DateUtils.FORMAT_SHOW_TIME;
                flags |= android.text.format.DateUtils.FORMAT_SHOW_DATE;
                flags |= android.text.format.DateUtils.FORMAT_ABBREV_MONTH;
                flags |= android.text.format.DateUtils.FORMAT_SHOW_YEAR;

                finalDateTime = android.text.format.DateUtils.formatDateTime(context,
                        when + TimeZone.getDefault().getOffset(when), flags);
            }
        }
        return finalDateTime;
    }

}


