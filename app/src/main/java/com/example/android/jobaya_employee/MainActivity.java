package com.example.android.jobaya_employee;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // declare Views
   // TextView screenTitleTxt;
  //  TextView logoutTxt;

    // declare action bar to change the title displayed
    ActionBar actionBar ;
    MenuItem searchItem;
    // list of all the jobs
    static List<Job> AllJobsList;
    static  List<Job> AppliedJobsList;
    static FragmentManager fragmentManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // init fragment manager
        fragmentManager = getSupportFragmentManager();
        // init the vies
       // screenTitleTxt = findViewById(R.id.screen_title_txt);
       // screenTitleTxt.setText("Jobs");
       // logoutTxt = findViewById(R.id.logout_txt);
        // init the action bar
        actionBar = getSupportActionBar();
        actionBar.setTitle("Jobs");
        // logout function
       /* logoutTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // prompt the user
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Log-out");
                builder.setMessage("Are you sure you want to log-out ?");
                builder.setIcon(R.drawable.ic_exit_to_app_black_24dp);
                builder.setCancelable(false);
                builder.setPositiveButton("log-out", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences sharedPreferences = LoginActivity.sharedPreferences;
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("loggedIn" , "false");
                        editor.commit();

                        // return to login
                        Intent intent = new Intent(MainActivity.this , LoginActivity.class);
                        finish();
                        startActivity(intent);

                    }
                });
                builder.setNegativeButton("stay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();

            }
        });*/

        // declare the bottom navigation bar
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);

        // display the all jobs fragment when activity is first created
        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame , new FragmentAllJobs()).commit();

        // filled from fragments
        AllJobsList = new ArrayList<>();
        AppliedJobsList = new ArrayList<>();

    }

    // bottom nav item selection listener
    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    Fragment selectedFragment = null;
                    switch (menuItem.getItemId())
                    {
                        case R.id.allJobsTab:
                            selectedFragment = new FragmentAllJobs();
                           // screenTitleTxt.setText("Jobs");
                            searchItem.setVisible(true);
                            searchItem.collapseActionView();
                            actionBar.setTitle("Jobs");
                            break;
                        case R.id.appliedJobsTab:
                            selectedFragment = new FragmentAppliedJobs();
                           // screenTitleTxt.setText("Applied Jobs");
                            actionBar.setTitle("Applied Jobs");
                            searchItem.collapseActionView();
                            searchItem.setVisible(false);
                            break;
                        case R.id.matchedJobsTab:
                            selectedFragment = new FragmentMatchedJobs();
                          //  screenTitleTxt.setText("Matched Jobs");
                            actionBar.setTitle("Matched Jobs");
                            searchItem.setVisible(false);
                            searchItem.collapseActionView();
                            break;
                        case R.id.userTab:
                            selectedFragment = new FragmentUserProfile();
                           // screenTitleTxt.setText("Profile");
                            actionBar.setTitle("Profile");
                            searchItem.setVisible(false);
                            searchItem.collapseActionView();
                            break;
                    }

                    fragmentManager.beginTransaction().replace(R.id.main_frame , selectedFragment).commit();
                    return true;
                }
            };

    @Override
    public void onBackPressed() {


        // alert dialog to stay on page
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Exit App");
        builder.setMessage("leave app ?");
        builder.setCancelable(false);
        builder.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MainActivity.super.onBackPressed();

            }
        });
        builder.setNegativeButton("Stay", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // do nothing
            }
        });
        builder.show();
    }



    // to inflate the action bar menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.action_bar_menu , menu);
        MenuItem logoutItem = menu.findItem(R.id.logout_action_bar);
        MenuItem messageItem = menu.findItem(R.id.messenger_action_bar);
        searchItem = menu.findItem(R.id.search_action_bar);

        android.support.v7.widget.SearchView searchView = (android.support.v7.widget.SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new android.support.v7.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //FragmentAllJobs.multipleToggleSwitch.setVisibility(View.VISIBLE);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterSearch(newText);
                // reset toggle to all jobs
               // FragmentAllJobs.multipleToggleSwitch.setVisibility(View.INVISIBLE);
                FragmentAllJobs.showingSearchFor.setText("showing search results for \" "+newText+" \"");
                return false;
            }
        });
        searchItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                FragmentAllJobs.multipleToggleSwitch.setVisibility(View.INVISIBLE);
                FragmentAllJobs.showingSearchFor.setVisibility(View.VISIBLE);
                FragmentAllJobs.showingSearchFor.setText("showing search results for \" \"");
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                FragmentAllJobs.multipleToggleSwitch.setVisibility(View.VISIBLE);
                FragmentAllJobs.showingSearchFor.setVisibility(View.INVISIBLE);
                FragmentAllJobs.multipleToggleSwitch.setCheckedTogglePosition(0);
                return true;
            }
        });
       return true;
    }

    // search filter
    private void filterSearch(String text) {
        //new array list that will hold the filtered data
        ArrayList<Job> filterdNames = new ArrayList<>();

        //looping through existing elements
        for (Job s : FragmentAllJobs.jobsList) {
            //if the existing elements contains the search input
            if (s.title.toLowerCase().contains(text.toLowerCase())) {
                //adding the element to filtered list
                filterdNames.add(s);
            }
        }

        // check if there is no search result
        if(filterdNames.size()==0)
        {
            FragmentAllJobs.errorImage.setImageResource(R.drawable.problem);
            FragmentAllJobs.errorImage.setVisibility(View.VISIBLE);
            FragmentAllJobs.errorText.setText("no results found matching your search");
            FragmentAllJobs.errorText.setVisibility(View.VISIBLE);
        }
        else {


            FragmentAllJobs.errorImage.setVisibility(View.INVISIBLE);
            FragmentAllJobs.errorText.setVisibility(View.INVISIBLE);

        }

        //calling a method of the adapter class and passing the filtered list
        FragmentAllJobs.adapterAllJobs.filterList(filterdNames);
    }

    // handle action bar icon clicks
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.logout_action_bar:
               // Toast.makeText(this, "logout", Toast.LENGTH_SHORT).show();
                // logout logic
                // prompt the user
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Log-out");
                builder.setMessage("Are you sure you want to log-out ?");
                builder.setIcon(R.drawable.ic_exit_to_app_black_24dp);
                builder.setCancelable(false);
                builder.setPositiveButton("log-out", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences sharedPreferences = LoginActivity.sharedPreferences;
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("loggedIn" , "false");
                        editor.commit();
                        // return to login
                        Intent intent = new Intent(MainActivity.this , LoginActivity.class);
                        finish();
                        startActivity(intent);
                    }
                });
                builder.setNegativeButton("stay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder.show();
                break;
            case R.id.messenger_action_bar:
               // Toast.makeText(this, "messenger", Toast.LENGTH_SHORT).show();
                break;
            case R.id.search_action_bar:
               // Toast.makeText(this, "search", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
