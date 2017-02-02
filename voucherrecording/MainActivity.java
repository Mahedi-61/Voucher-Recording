package com.mahediapps.voucherrecording;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import com.mahediapps.adapter.NavigationDrawerCallbacks;
import com.mahediapps.model.DatabaseHelper;


public class MainActivity extends ActionBarActivity implements NavigationDrawerCallbacks, View.OnClickListener {

    private EditText  et;
    private TextView tvTitle;
    private ImageButton ibNext, ibPrevious;
    private Button bSave;

    private NavigationDrawerFragment mNavigationDrawerFragment;

    private SharedPreferences profile;
    private Toolbar mToolbar;
    private DatabaseHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHelper = new DatabaseHelper(this);
        try {
            dbHelper.createDataBase();
        } catch (Exception ex) {
            Log.e("Allah help me", "database copy hoi ni ? ");
            throw new Error("Unable to create database");
        }
        dbHelper.openDataBase();

        mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(mToolbar);
        profile = getSharedPreferences("profile", 0);
        mNavigationDrawerFragment = (NavigationDrawerFragment) getFragmentManager().findFragmentById(R.id.fragment_drawer);
        mNavigationDrawerFragment.setup(R.id.fragment_drawer, (DrawerLayout) findViewById(R.id.drawer), mToolbar);
        mNavigationDrawerFragment.setUserData(profile.getString("user_name", ""), profile.getString("email_address", ""),
                BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));


        initialize();
    }


    private void initialize() {

    }


    private void setValueInLayoutElement() {
        //HashMap<String, String> hashmap = dbHelper.getDailyReportFromDatabase(dayId);
        int cbState = 0;

    }


    @Override
    public void onNavigationDrawerItemSelected(int position) {

        switch (position) {
            case 0: {
                //startActivity(new Intent(this, TransactionActivity.class));
                break;
            }
            case 1: {
                startActivity(new Intent(this, ViewIncomeActivity.class));
                break;
            }
            case 2: {
                startActivity(new Intent(this, ViewExpenseActivity.class));
                break;
            }
            case 3: {
                startActivity(new Intent(this, ClassifiedExpenseActivity.class));
                break;
            }
            case 4: {
                //startActivity(new Intent(this, StatisticsActivity.class));
                break;
            }
            case 5: {

                break;
            }

            case 6: {
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW,  Uri.parse("market://search?q=pub:Shibir")));

                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/search?q=pub:Shibir")));
                }
                break;
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            getMenuInflater().inflate(R.menu.main, menu);
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_content_add){
            //startActivity(new Intent(this, AddExpenseActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View v) {
        HashMap<String, String> dailyDairy = new HashMap<>();

    }

}
