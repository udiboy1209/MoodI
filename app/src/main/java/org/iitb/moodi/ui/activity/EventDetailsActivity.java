package org.iitb.moodi.ui.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.iitb.moodi.R;
import org.iitb.moodi.api.Event;
import org.iitb.moodi.ui.fragment.NavigationDrawerFragment;

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

            TextView view = new TextView(EventDetailsActivity.this);
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));
            switch (position){
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
                    view.setText(eventDetails.registration);
                    break;
            }

            container.addView(view);

            Log.i(TAG, "instantiateItem() [position: " + position + "]");

            // Return the View
            return view;
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

