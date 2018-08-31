package com.example.kc5517.lifegoals.view;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kc5517.lifegoals.Database.DatabaseHelper;
import com.example.kc5517.lifegoals.Database.Goals;
import com.example.kc5517.lifegoals.R;
import com.example.kc5517.lifegoals.utils.MyDividerItemDecoration;
import com.example.kc5517.lifegoals.utils.RecyclerTouchListener;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private GoalsAdapter mAdapter;
    private List<Goals> goalsList = new ArrayList<>();
    private CoordinatorLayout coordinatorLayout;
    private RecyclerView recyclerView;
    private TextView noGoalsView;

    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        coordinatorLayout = findViewById(R.id.coordinator_layout);
        recyclerView = findViewById(R.id.recycler_view);
        noGoalsView = findViewById(R.id.empty_goals_view);

        db = new DatabaseHelper(this);

        goalsList.addAll(db.getAllGoals());

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showGoalsDialog(false, null, -1);
            }
        });

        mAdapter = new GoalsAdapter(this, goalsList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new MyDividerItemDecoration(this, LinearLayoutManager.VERTICAL, 16));
        recyclerView.setAdapter(mAdapter);

        toggleEmptyGoals();

        /**
         * On long press on RecyclerView item, open alert dialog
         * with options to choose
         * Edit and Delete
         * */
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this,
                recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, final int position) {
            }

            @Override
            public void onLongClick(View view, int position) {
                showActionsDialog(position);
            }
        }));
    }

    /**
     * Inserting new goal in db
     * and refreshing the list
     */
    private void createGoal(String goal) {
        // inserting goal in db and getting
        // newly inserted goal id
        long id = db.insertGoal(goal);

        // get the newly inserted goal from db
        Goals n = db.getGoals(id);

        if (n != null) {
            // adding new goal to array list at 0 position
            goalsList.add(0, n);

            // refreshing the list
            mAdapter.notifyDataSetChanged();

            toggleEmptyGoals();
        }
    }

    /**
     * Updating goal in db and updating
     * item in the list by its position
     */
    private void updateGoal(String goal, int position) {
        Goals g = goalsList.get(position);
        // updating goal text
        g.setGoal(goal);

        // updating goal in db
        db.updateGoal(g);

        // refreshing the list
        goalsList.set(position, g);
        mAdapter.notifyItemChanged(position);

        toggleEmptyGoals();
    }

    /**
     * Deleting goal from SQLite and removing the
     * item from the list by its position
     */
    private void deleteGoal(int position) {
        // deleting the goal from db
        db.deleteGoal(goalsList.get(position));

        // removing the goal from the list
        goalsList.remove(position);
        mAdapter.notifyItemRemoved(position);

        toggleEmptyGoals();
    }

    /**
     * Opens dialog with Edit - Delete options
     * Edit - 0
     * Delete - 0
     */
    private void showActionsDialog(final int position) {
        CharSequence colors[] = new CharSequence[]{"Edit", "Delete"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose option");
        builder.setItems(colors, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    showGoalsDialog(true, goalsList.get(position), position);
                } else {
                    deleteGoal(position);
                }
            }
        });
        builder.show();
    }

    /**
     * Shows alert dialog with EditText options to enter / edit
     * a goal.
     * when shouldUpdate=true, it automatically displays old goal and changes the
     * button text to UPDATE
     */
    private void showGoalsDialog(final boolean shouldUpdate, final Goals goals, final int position) {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getApplicationContext());
        View view = layoutInflaterAndroid.inflate(R.layout.goal_dialog, null);

        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilderUserInput.setView(view);

        final EditText inputGoal = view.findViewById(R.id.goal);
        TextView dialogTitle = view.findViewById(R.id.dialog_title);
        dialogTitle.setText(!shouldUpdate ? getString(R.string.lbl_new_goal_title) : getString(R.string.lbl_edit_goal_title));

        if (shouldUpdate && goals != null) {
            inputGoal.setText(goals.getGoal());
        }
        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton(shouldUpdate ? "update" : "save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {

                    }
                })
                .setNegativeButton("cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                dialogBox.cancel();
                            }
                        });

        final AlertDialog alertDialog = alertDialogBuilderUserInput.create();
        alertDialog.show();

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show toast message when no text is entered
                if (TextUtils.isEmpty(inputGoal.getText().toString())) {
                    Toast.makeText(MainActivity.this, "Enter goal!", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    alertDialog.dismiss();
                }

                // check if user updating goal
                if (shouldUpdate && goals != null) {
                    // update goal by it's id
                    updateGoal(inputGoal.getText().toString(), position);
                } else {
                    // create new goal
                    createGoal(inputGoal.getText().toString());
                }
            }
        });
    }

    /**
     * Toggling list and empty goals view
     */
    private void toggleEmptyGoals() {
        // you can check goalsList.size() > 0

        if (db.getGoalsCount() > 0) {
            noGoalsView.setVisibility(View.GONE);
        } else {
            noGoalsView.setVisibility(View.VISIBLE);
        }
    }
}