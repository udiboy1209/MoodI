package org.iitb.moodi.ui.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.iitb.moodi.MoodIndigoClient;
import org.iitb.moodi.R;
import org.iitb.moodi.api.CityResponse;
import org.iitb.moodi.api.Event;
import org.iitb.moodi.api.EventDetailsResponse;
import org.iitb.moodi.ui.fragment.NavigationDrawerFragment;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by udiboy on 28/11/15.
 */
public class EventDetailsActivity extends BaseActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks{

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private Event eventDetails;
    int min_reg = 0;
    int max_reg = 0;
    private String TAG = "EventDetailsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        eventDetails = getIntent().getParcelableExtra("event_details");

        mToolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_menu_white_24dp);

        mTabLayout = (TabLayout) findViewById(R.id.events_tab_layout);
        mViewPager = (ViewPager) findViewById(R.id.events_pager);


        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        mViewPager.setAdapter(new SamplePagerAdapter());
        mTabLayout.setupWithViewPager(mViewPager);

        final ProgressDialog dialog = ProgressDialog.show(this, "",
                "Fetching data. Please wait...", true);

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(m_API_URL)
                .build();
        MoodIndigoClient methods = restAdapter.create(MoodIndigoClient.class);
        Callback callback = new Callback() {
            @Override
            public void success(Object o, Response response) {
                EventDetailsResponse c = (EventDetailsResponse) o;
                min_reg = c.getMin();
                max_reg = c.getMax();
                dialog.dismiss();
            }
            @Override
            public void failure(RetrofitError retrofitError) {
                String error = retrofitError.getMessage();
                Log.e(TAG, error);
                dialog.dismiss();
            }
        };
        methods.getEventDetails(eventDetails.id, callback);
    }

    @Override
    protected void onResume() {
        super.onResume();

        mToolbar.setTitle(eventDetails.name);
        mToolbar.setSubtitle(eventDetails.intro_short);
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
    /*if(position==1) {
        switchContent(ScheduleFragment.newInstance());
    } else if(position==2){
        startActivity(new Intent(this,MapsActivity.class));
    }*/
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

    public Toolbar getToolbar() {
        return mToolbar;
    }

    class SamplePagerAdapter extends PagerAdapter {
        public static final String TAG = "EventDetPagerAdapter";

        /**
         * @return the number of pages to display
         */
        @Override
        public int getCount() {
            return 4;
        }

        /**
         * @return true if the value returned from {@link #instantiateItem(ViewGroup, int)} is the
         * same object as the {@link View} added to the {@link ViewPager}.
         */
        @Override
        public boolean isViewFromObject(View view, Object o) {
            return o == view;
        }

        /**
         * Return the title of the item at {@code position}. This is important as what this method
         * returns is what is displayed in the {@link TabLayout}.
         * <p>
         * Here we construct one using the position value, but for real application the title should
         * refer to the item's contents.
         */
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position){
                case 0:
                    return "About";
                case 1:
                    return "Rules";
                case 2:
                    return "Prizes";
                case 3:
                    return "Register";
                default:
                    return "NULL";
            }
        }

        /**
         * Instantiate the {@link View} which should be displayed at {@code position}. Here we
         * inflate a layout from the apps resources and then change the text view to signify the position.
         */
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            // Inflate a new layout from our resources
            if (position==3) {
                LayoutInflater inflater = LayoutInflater.from(getBaseContext());
                View view = inflater.inflate(R.layout.event_registration, null, false);
                TextView tv = (TextView) view.findViewById(R.id.event_reg_textview);
                TextView tv2 = (TextView) view.findViewById(R.id.event_reg_mi_no);
                TextView tv3 = (TextView) view.findViewById(R.id.event_reg_min_participants);
                tv.setText(R.string.event_registration_message);
                tv2.setText("Your MI number is "+me.mi_no);
                tv3.setText("Minimum number of participants is "+min_reg);
                container.addView(view);
                Log.i(TAG, "instantiateItem() [position: " + position + "]");
                return view;
            }
            else {
                TextView view = new TextView(EventDetailsActivity.this);
                view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));
                switch (position) {
                    case 0:
                        view.setText(eventDetails.intro);
                        break;
                    case 1:
                        view.setText(eventDetails.rules);
                        break;
                    case 2:
                        view.setText(eventDetails.prizes);
                        break;
                    case 3:
                        view.setText("Fetching registration details..");

                }

                container.addView(view);
                Log.i(TAG, "instantiateItem() [position: " + position + "]");

                // Return the View
                return view;
            }

        }

        /**
         * Destroy the item from the {@link ViewPager}. In our case this is simply removing the
         * {@link View}.
         */
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
            Log.i(TAG, "destroyItem() [position: " + position + "]");
        }
    }
}

