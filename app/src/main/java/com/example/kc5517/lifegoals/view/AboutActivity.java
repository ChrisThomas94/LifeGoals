package com.example.kc5517.lifegoals.view;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

import com.example.kc5517.lifegoals.R;


import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;

/**
 * Created by Chris on 12-Jun-17.
 *
 */

public class AboutActivity extends AppCompatActivity {

    public AboutActivity(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        ActionBar ab = getSupportActionBar();

        PackageInfo pInfo = null;
        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        Element version = new Element();
        version.setTitle("Version: " + pInfo.versionName);

        /*Element projects = new Element();
        projects.setTitle("Related Projects");
        projects.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent projects = new Intent(getBaseContext(), ProjectsActivity.class);
                startActivity(projects);
            }
        });*/

        /*Element acknowledgements = new Element();
        acknowledgements.setTitle("Acknowledgements");
        acknowledgements.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent credits = new Intent(getBaseContext(), AcknowledgementsActivity.class);
                startActivity(credits);
            }
        });*/

        View aboutPage = new AboutPage(this)
                .isRTL(false)
                .setImage(R.drawable.pen_writing)
                .setDescription(" ")
                .addItem(version)
                .addEmail("chris.thomas94@hotmail.co.uk")
                .addWebsite("www.wildswap.com")
                .addFacebook("wildswap")
                .addTwitter("WildSwap")
                .addInstagram("wild_swap")
                .addPlayStore("com.wildswap.wildswapapp")
                .addGitHub("ChrisThomas94")
                //.addItem(projects)
                //.addItem(acknowledgements)
                .create();

        setContentView(aboutPage);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return (super.onOptionsItemSelected(menuItem));
    }
}
