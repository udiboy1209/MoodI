package org.iitb.moodi.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import org.iitb.moodi.MoodIndigoClient;
import org.iitb.moodi.R;
import org.iitb.moodi.api.EventsResponse;
import org.iitb.moodi.ui.fragment.NavigationDrawerFragment;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class MainActivity extends BaseActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent i = new Intent(getBaseContext(), RegistrationActivity.class);
        startActivity(i);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //collapsingToolbar = (CollapsingToolbarLayout)findViewById(R.id.collapsing_toolbar);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_menu_white_24dp);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));


        mToolbar.setTitle("Home");
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /*public void customizeToolbar(int resId, String title, View widget){
        if(mToolbarContainer.getChildCount()>1)
            mToolbarContainer.removeViewAt(mToolbarContainer.getChildCount()-1);
        if(widget!=null)
            mToolbarContainer.addView(widget);

        mTitle = title;
        restoreActionBar();
    }*/

    public void gotoEventList(View v){
        int id = Integer.valueOf((String)v.getTag());
        Intent eventlist = new Intent();
        eventlist.setClass(MainActivity.this, EventsActivity.class);
        eventlist.putExtra("id",id);
        startActivity(eventlist);

        /*final ProgressDialog dialog = ProgressDialog.show(this, "",
                "Fetching data. Please wait...", true);

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(API_URL)
                .build();
        MoodIndigoClient methods = restAdapter.create(MoodIndigoClient.class);
        Callback callback = new Callback() {
            @Override
            public void success(Object o, Response response) {
                EventsResponse c = (EventsResponse)o;
                dialog.dismiss();
                Log.d("EventFetchResponse", c.id+" "+c.genres.length);


            }
            @Override
            public void failure(RetrofitError retrofitError) {
                retrofitError.printStackTrace();
                dialog.dismiss();
                Toast.makeText(MainActivity.this, "Error fetching data", Toast.LENGTH_LONG).show();
            }
        };
        methods.getEvents(id, callback);*/
    }

    public int getColorResource(int id){
        switch(id){
            case 1:
                return R.color.color_compi;
            case 2:
                return R.color.color_informals;
            case 3:
                return R.color.color_concerts;
            case 4:
                return R.color.color_proshows;
            case 5:
                return R.color.color_arts;
        }
        return R.color.color_compi;
    }

    public int getBackgroundResource(int id){
        switch(id){
            case 1:
                return R.drawable.compi;
            case 2:
                return R.drawable.informals;
            case 3:
                return R.drawable.pronites;
            case 4:
                return R.drawable.proshows;
            case 5:
                return R.drawable.workshops;
        }
        return R.color.color_compi;
    }
}
