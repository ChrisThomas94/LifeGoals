package com.example.kc5517.lifegoals.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.kc5517.lifegoals.Database.DatabaseHelper;
import com.example.kc5517.lifegoals.Database.Entry;
import com.example.kc5517.lifegoals.R;
import com.example.kc5517.lifegoals.utils.EntryAdapter;
import com.example.kc5517.lifegoals.utils.GoalListAdapter;
import com.example.kc5517.lifegoals.utils.GoalListAdapterRecycle;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.view.KeyEvent.ACTION_DOWN;
import static android.view.KeyEvent.KEYCODE_ENTER;

public class MainActivity extends AppCompatActivity {
    private EntryAdapter entryAdapter;
    private GoalListAdapter goalAdapter;
    private ViewPager entryPager;
    private ListView goalListView;
    private List<Entry> entryList = new ArrayList<>();
    private ArrayList<String> goalList = new ArrayList<>();
    private CoordinatorLayout coordinatorLayout;
    private RecyclerView recyclerView;
    private TextView noGoalsView;
    private LinearLayout mainLinear;
    private Menu optionsMenu;
    private Toolbar toolbar;
    private boolean entryToday;
    private LinearLayout addGoalsLl;
    private EditText enterGoalEditText;
    Gson gson = new Gson();

    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //coordinatorLayout = findViewById(R.id.coordinator_layout);
        //recyclerView = findViewById(R.id.recycler_view);
        //noGoalsView = findViewById(R.id.empty_goals_view);

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("dd/MM/yyyy ");
        String today = mdformat.format(calendar.getTime());


        db = new DatabaseHelper(this);

        entryList.addAll(db.getAllGoals());

        entryToday = db.checkEntryToday();

        Log.d("TAG :", "Entry "+ today + ": " + entryToday);


        //if entry today then show the full database of goals, if no entry for today then force user to enter goals before showing the full database
        if(entryToday){
            setContentView(R.layout.activity_add_goal);
            Log.d("TAG :", "activity add goal");

            addGoalsLl = findViewById(R.id.addGoalsLinearLayout);
            //enterGoalEditText = findViewById(R.id.enterGoalEditText);
            EditText goalDate = findViewById(R.id.goalDate);
            com.getbase.floatingactionbutton.FloatingActionButton fab = findViewById(R.id.fab);

            goalDate.setText(today);

            goalList = new ArrayList<>(10);
            goalList.add(0, "I will...");
            goalAdapter = new GoalListAdapter(this, goalList);
            goalListView = findViewById(R.id.addGoalsList);
            goalListView.setAdapter(goalAdapter);

            //enterGoalEditText.setOnKeyListener(enterListener);
            fab.setOnClickListener(submitListener);

        }else{
            setContentView(R.layout.activity_main);
            entryAdapter = new EntryAdapter(getApplicationContext(), entryList);
            Log.d("TAG :", "main activity");

            mainLinear = findViewById(R.id.main_linear);
            entryPager = findViewById(R.id.goalsViewPager);
            entryPager.setAdapter(entryAdapter);

            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            toggleEmptyGoals();
        }


        /*FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showGoalsDialog(false, null, -1);
            }
        });*/

                /*RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new MyDividerItemDecoration(this, LinearLayoutManager.VERTICAL, 16));
        recyclerView.setAdapter(entryAdapter);*/

        /**
         * On long press on RecyclerView item, open alert dialog
         * with options to choose
         * Edit and Delete
         * */
        /*recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this,
                recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, final int position) {
            }

            @Override
            public void onLongClick(View view, int position) {
                showActionsDialog(position);
            }
        }));*/
    }

    private EditText.OnKeyListener enterListener = new EditText.OnKeyListener(){
        @Override
        public boolean onKey(View view, int i, KeyEvent keyEvent){
            //If the keyevent is a key-down event on the "enter" button
            if ((keyEvent.getAction() == ACTION_DOWN) && (keyEvent.getKeyCode() == KEYCODE_ENTER)) {
                Log.d("TAG :", "ENTER PRESSED");

                EditText et = new EditText(MainActivity.this);
                et.setText("test");
                et.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                addGoalsLl.addView(et);

                return true;
            }
            return false;
        }
    };

    private com.getbase.floatingactionbutton.FloatingActionButton.OnClickListener submitListener = new com.getbase.floatingactionbutton.FloatingActionButton.OnClickListener(){
        @Override
        public void onClick(View view) {

            Gson gson = new Gson();

            String enteredGoals = gson.toJson(goalList);

            db.insertGoal(enteredGoals);

            Snackbar.make(view, "Goal successfully added", 5);

            setContentView(R.layout.activity_main);
            entryAdapter = new EntryAdapter(getApplicationContext(), entryList);
            Log.d("TAG :", "main activity");

            mainLinear = findViewById(R.id.main_linear);
            entryPager = findViewById(R.id.goalsViewPager);
            entryPager.setAdapter(entryAdapter);

            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            toggleEmptyGoals();
        }
    };

    /**
     * Inserting new goal in db
     * and refreshing the list
     */
    private void createGoal(String goal) {
        // inserting goal in db and getting
        // newly inserted goal id
        long id = db.insertGoal(goal);

        // get the newly inserted goal from db
        Entry n = db.getGoals(id);

        if (n != null) {
            // adding new goal to array list at 0 position
            entryList.add(0, n);

            // refreshing the list
            entryAdapter.notifyDataSetChanged();

            toggleEmptyGoals();
        }
    }

    /**
     * Updating goal in db and updating
     * item in the list by its position

    private void updateGoal(String goal, int position) {
        Entry g = entryList.get(position);
        // updating goal text
        g.setGoal(goal);

        // updating goal in db
        db.updateGoal(g);

        // refreshing the list
        entryList.set(position, g);
        entryAdapter.notifyItemChanged(position);

        toggleEmptyGoals();
    }/

    /**
     * Deleting goal from SQLite and removing the
     * item from the list by its position

    private void deleteGoal(int position) {
        // deleting the goal from db
        db.deleteGoal(entryList.get(position));

        // removing the goal from the list
        entryList.remove(position);
        entryAdapter.notifyItemRemoved(position);

        toggleEmptyGoals();
    }*/

    /**
     * Opens dialog with Edit - Delete options
     * Edit - 0
     * Delete - 0

    private void showActionsDialog(final int position) {
        CharSequence colors[] = new CharSequence[]{"Edit", "Delete"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose option");
        builder.setItems(colors, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    showGoalsDialog(true, entryList.get(position), position);
                } else {
                    deleteGoal(position);
                }
            }
        });
        builder.show();
    }*/

    /**
     * Shows alert dialog with EditText options to enter / edit
     * a goal.
     * when shouldUpdate=true, it automatically displays old goal and changes the
     * button text to UPDATE
     */

    /**
     * Toggling list and empty goals view
     */
    private void toggleEmptyGoals() {
        // you can check entryList.size() > 0

        if (db.getGoalsCount() > 0) {
            //noGoalsView.setVisibility(View.GONE);
        } else {
            //noGoalsView.setVisibility(View.VISIBLE);
        }
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
}