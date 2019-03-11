package com.example.kc5517.lifegoals.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import com.example.kc5517.lifegoals.Database.DatabaseHelper;
import com.example.kc5517.lifegoals.Database.Entry;
import com.example.kc5517.lifegoals.R;
import com.example.kc5517.lifegoals.utils.AppConfig;
import com.example.kc5517.lifegoals.utils.EntryAdapter;
import com.example.kc5517.lifegoals.utils.Goal;
import com.example.kc5517.lifegoals.utils.GoalItemAdapter;
import com.google.gson.Gson;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private EntryAdapter entryAdapter;
    private GoalItemAdapter goalItemAdapter;
    private ViewPager entryPager;
    private ListView goalListView;
    private ArrayList<Goal> goalItemList;
    private List<Entry> entryList = new ArrayList<>();
    private ArrayList<String> goalList = new ArrayList<>();
    private Menu optionsMenu;
    private boolean entryToday = false;
    private LinearLayout addGoalsLl;
    private int goalMax;
    boolean isFABOpen = false;
    FloatingActionButton fab;
    FloatingActionButton fabSubmit;
    FloatingActionButton fabAdd;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("dd/MM/yyyy ");
        String today = mdformat.format(calendar.getTime());

        db = new DatabaseHelper(this);

        entryList.addAll(db.getAllEntries());

        Log.d("TAG :", "Number of entries: "+  db.getEntriesCount());

        int numEntries = db.getEntriesCount();

        AppConfig config = new AppConfig();
        goalMax = config.getGoalMax();

        Log.d("TAG :", "Entry "+ today + ": " + entryToday);


        //if entry today then show the full database of goals, if no entry for today then force user to enter goals before showing the full database
        if(numEntries == 0){
            setContentView(R.layout.activity_add_goal);
            Log.d("TAG :", "activity add goal");

            addGoalsLl = findViewById(R.id.addGoalsLinearLayout);
            //enterGoalEditText = findViewById(R.id.enterGoalEditText);
            EditText goalDate = findViewById(R.id.goalDate);
            fab = findViewById(R.id.fab);
            fabSubmit = findViewById(R.id.fabSubmit);
            fabAdd = findViewById(R.id.fabAdd);

            goalDate.setText(today);

            //init goal item list
            goalItemList = new ArrayList<Goal>();
            Goal goal0 = new Goal(0, "");
            goalItemList.add(0,goal0);

            goalItemAdapter = new GoalItemAdapter(this, goalItemList);
            goalListView = findViewById(R.id.addGoalsList);
            goalListView.setAdapter(goalItemAdapter);

            fab.setOnClickListener(expandListener);
            fabSubmit.setOnClickListener(submitListener);
            fabAdd.setOnClickListener(addListener);

            fabSubmit.hide();
            fabAdd.hide();

        }else{
            bootEntryHistory();
        }

    }

    private void bootEntryHistory(){

        entryList = new ArrayList<>();
        entryList.addAll(db.getAllEntries());

        setContentView(R.layout.activity_main);
        entryAdapter = new EntryAdapter(getApplicationContext(), entryList);
        Log.d("TAG :", "main activity");

        entryPager = findViewById(R.id.goalsViewPager);
        entryPager.setPageTransformer(true, new ViewPagerStack());
        entryPager.setAdapter(entryAdapter);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private FloatingActionButton.OnClickListener submitListener = new FloatingActionButton.OnClickListener(){
        @Override
        public void onClick(View view) {

            goalItemAdapter.notifyDataSetChanged();

            fab.hide();
            fabSubmit.hide();
            fabAdd.hide();

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < goalItemList.size(); i++) {
                        if (!goalItemList.get(i).getGoal().equals("")) {
                            if (goalList.size() < goalItemList.size()) {
                                goalList.add(i, goalItemList.get(i).getGoal());
                            } else {
                                goalList.set(i, goalItemList.get(i).getGoal());
                            }
                        }
                    }

                    if (goalList.size() == 0) {

                        Snackbar entryAdded = Snackbar.make(addGoalsLl, "You must add a goal", Snackbar.LENGTH_LONG);
                        entryAdded.show();

                        fab.show();
                        fabSubmit.show();
                        fabAdd.show();

                    } else {

                        Log.d("TAG :", "Goal 0 from list: " + goalList.get(0));

                        Snackbar entryAdded = Snackbar.make(addGoalsLl, "Entry successfully added", Snackbar.LENGTH_LONG);
                        entryAdded.show();

                        Gson gson = new Gson();
                        String enteredGoals = gson.toJson(goalList);

                        db.insertEntry(enteredGoals);
                        Log.d("TAG :", "Entry successfully added: " + enteredGoals);

                        bootEntryHistory();
                    }
                }

            }, 1000);
        }
    };

    private FloatingActionButton.OnClickListener addListener = new FloatingActionButton.OnClickListener(){
        @Override
        public void onClick(View view) {

            addGoalToList();
            Log.d("TAG :", "goal list size: " + goalItemList.size());

        }
    };

    private FloatingActionButton.OnClickListener expandListener = new FloatingActionButton.OnClickListener(){
        @Override
        public void onClick(View view) {
            if(!isFABOpen){
                fabAdd.show();
                fabSubmit.show();
                showFABMenu();
            } else {
                closeFABMenu();
            }
        }
    };

    private void showFABMenu(){
        isFABOpen=true;
        fabSubmit.animate().translationY(-getResources().getDimension(R.dimen.standard_60));
        fabAdd.animate().translationY(-getResources().getDimension(R.dimen.standard_120));
    }

    private void addGoalToList(){
        //add new blank goal item to list
        Goal goalNew = new Goal(goalItemList.size(), "");
        goalItemList.add(goalItemList.size(),goalNew);

        //add new blank edit text to screen
        goalItemAdapter.notifyDataSetChanged();
    }

    private void closeFABMenu(){
        isFABOpen=false;
        fabSubmit.animate().translationY(0);
        fabAdd.animate().translationY(0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        this.optionsMenu = menu;
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_about) {
            Intent intent = new Intent(getApplicationContext(), AboutActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class ViewPagerStack implements ViewPager.PageTransformer {
        @Override
        public void transformPage(View page, float position) {
            if (position >= 0) {

                page.setScaleX(0.9f - 0.05f * position);
                page.setScaleY(0.9f);
                page.setTranslationX(-page.getWidth() * position);
                page.setTranslationY(30 * position);
                page.setAlpha(1-position/2);
            }
        }
    }
}