package com.triocodes.medivision.Activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.database.SQLException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.triocodes.medivision.Data.DataBaseHelper;
import com.triocodes.medivision.Data.DataBaseQueryHelper;
import com.triocodes.medivision.Fragment.DutyReportFragment;
import com.triocodes.medivision.Fragment.HomeFragment;
import com.triocodes.medivision.Fragment.JourneyReportFragment;
import com.triocodes.medivision.Fragment.MeetingFragment;
import com.triocodes.medivision.Fragment.MeetingReportFragment;
import com.triocodes.medivision.Fragment.TaskFragment;
import com.triocodes.medivision.Fragment.TaskStatusFragment;
import com.triocodes.medivision.Interface.FragmentListenerCallback;
import com.triocodes.medivision.Fragment.LoginFragment;
import com.triocodes.medivision.R;

import java.io.IOException;

public class MainActivity extends ActionBarActivity implements FragmentListenerCallback {
    DataBaseHelper dbhelper;
    private Toolbar mToolbar;
    int c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ConnectivityManager check = (ConnectivityManager)
                this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] info = check.getAllNetworkInfo();
        getdata();
        c = 1;
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment f = getFragmentManager().findFragmentById(R.id.container);
                loadHome();
                if (f instanceof TaskStatusFragment) {
                    loadTask();
                }
              /*  } else if (f instanceof RequestAcceptViewFragment) {
                    loadRequest();
                } else if (f instanceof SignUpFragment) {
                    loadLogin();
                } else if (f instanceof ViewServiceEditFragment) {
                    loadViewService();
                }*/
            }
        });
        loadLogin();
       // loadHome();
       // loadMeeting();


        if (DataBaseQueryHelper.getInstance().isNewUser() == 0) {
            loadLogin();

        } else if (DataBaseQueryHelper.getInstance().isNewUser() == 1) {
            loadHome();

        }
    }



    @Override
    public void loadLogin() {
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        Fragment mLoginFragment = new LoginFragment();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, mLoginFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }

    @Override
    public void loadMeeting() {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            Fragment mLoginFragment = new MeetingFragment();
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.enter, R.anim.exit, 0, 0);
            fragmentTransaction.replace(R.id.container, mLoginFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
    }

    @Override
    public void loadHome() {
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        Fragment mLoginFragment = new HomeFragment();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, mLoginFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void loadTask() {
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Fragment mLoginFragment = new TaskFragment();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter, R.anim.exit, 0, 0);
        fragmentTransaction.replace(R.id.container, mLoginFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void loadTaskStatus() {
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Fragment mLoginFragment = new TaskStatusFragment();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter, R.anim.exit, 0, 0);
        fragmentTransaction.replace(R.id.container, mLoginFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void loadDutyReport() {
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Fragment mLoginFragment = new DutyReportFragment();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter, R.anim.exit, 0, 0);
        fragmentTransaction.replace(R.id.container, mLoginFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void loadMeetingReport() {
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Fragment mLoginFragment = new MeetingReportFragment();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter, R.anim.exit, 0, 0);
        fragmentTransaction.replace(R.id.container, mLoginFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void loadJourneyReport() {
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Fragment mLoginFragment = new JourneyReportFragment();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter, R.anim.exit, 0, 0);
        fragmentTransaction.replace(R.id.container, mLoginFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void loadTarget() {

    }

    @Override
    public void loadReminder() {

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_exit) {
            ActivityCompat.finishAffinity(this);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void getdata() {
        // TODO Auto-generated method stub
        dbhelper = new DataBaseHelper(this); // initilize DatabaseHelper object
        try {
            dbhelper.createDataBase();
        } catch (IOException ioe) {

            throw new Error("Unable to create database");
        }
        try {
            dbhelper.openDataBase();
            dbhelper.getQueryhelper();
        } catch (SQLException sqle) {
            throw sqle;
        }
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        if (c == 1) {
            c = c + 1;
            Toast.makeText(MainActivity.this, "Tap again to exit",
                    Toast.LENGTH_SHORT).show();
        } else if (c == 2) {
            // super.onBackPressed();
            ActivityCompat.finishAffinity(this);

        }
    }
}
