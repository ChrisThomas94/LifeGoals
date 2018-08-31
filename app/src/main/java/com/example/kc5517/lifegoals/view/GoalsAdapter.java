package com.example.kc5517.lifegoals.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.kc5517.lifegoals.Database.Goals;
import com.example.kc5517.lifegoals.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class GoalsAdapter extends RecyclerView.Adapter<GoalsAdapter.MyViewHolder> {

    private Context context;
    private List<Goals> goalsList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView goal;
        public TextView dot;
        public TextView timestamp;

        public MyViewHolder(View view) {
            super(view);
            goal = view.findViewById(R.id.goal);
            dot = view.findViewById(R.id.dot);
            timestamp = view.findViewById(R.id.timestamp);
        }
    }


    public GoalsAdapter(Context context, List<Goals> goalsList) {
        this.context = context;
        this.goalsList = goalsList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.goals_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Goals goals = goalsList.get(position);

        holder.goal.setText(goals.getGoal());

        // Displaying dot from HTML character code
        holder.dot.setText(Html.fromHtml("&#8226;"));

        // Formatting and displaying timestamp
        holder.timestamp.setText(formatDate(goals.getTimestamp()));
    }

    @Override
    public int getItemCount() {
        return goalsList.size();
    }

    /**
     * Formatting timestamp to `MMM d` format
     * Input: 2018-02-21 00:15:42
     * Output: Feb 21
     */
    private String formatDate(String dateStr) {
        try {
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = fmt.parse(dateStr);
            SimpleDateFormat fmtOut = new SimpleDateFormat("MMM d");
            return fmtOut.format(date);
        } catch (ParseException e) {

        }

        return "";
    }
}
