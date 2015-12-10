package org.iitb.moodi.ui.activity;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.IBinder;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.iitb.moodi.BackgroundService;
import org.iitb.moodi.MoodIndigoClient;
import org.iitb.moodi.R;
import org.iitb.moodi.api.Event;
import org.iitb.moodi.api.EventsResponse;
import org.iitb.moodi.api.Genre;
import org.iitb.moodi.ui.fragment.NavigationDrawerFragment;
import org.iitb.moodi.ui.widget.EventListAdapter;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class EventsActivity extends BaseActivity implements
        View.OnClickListener, ViewPager.OnPageChangeListener, ServiceConnection{

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private EventsResponse eventsData;
    private ArrayList<EventListAdapter> eventLists;

    private ImageView mIconView;
    private float lastScroll;

    private Typeface tabFont;
    private BackgroundService mService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);

        tabFont = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Medium.ttf");

        mToolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);

        mTabLayout = (TabLayout) findViewById(R.id.events_tab_layout);
        mViewPager = (ViewPager) findViewById(R.id.events_pager);

        mIconView = (ImageView) findViewById(R.id.events_tab_icon);

        mIconView.setBackgroundResource(getIconResource(getIntent().getIntExtra("id",0)));
        mTabLayout.setBackgroundResource(getGradient(getIntent().getIntExtra("id",0)));

        ((ImageView)findViewById(R.id.events_img)).setImageResource(getBackgroundResource(getIntent().getIntExtra("id",0)));

        loadEventsData(getIntent().getIntExtra("id",0));

        mColorPrimary=R.drawable.toolbar_shadow_gradient;
        mColorPrimaryDark=R.color.colorDark;

        bindService(new Intent(this, BackgroundService.class), this, BIND_AUTO_CREATE);
    }

    @Override
    protected void onPause() {
        //mService.postEventNotifications();
        super.onPause();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    public void loadEventsData(int id){
        final ProgressDialog dialog = ProgressDialog.show(this, "",
                "Fetching data. Please wait...", true);

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(API_URL)
                .build();
        MoodIndigoClient methods = restAdapter.create(MoodIndigoClient.class);
        Callback callback = new Callback() {
            @Override
            public void success(Object o, Response response) {
                eventsData = (EventsResponse) o;
                dialog.dismiss();
                Log.d("EventFectResponse", eventsData.id + " " + eventsData.genres.length);

                eventLists = new ArrayList<>();
                for (Genre genre : eventsData.genres) {
                    EventListAdapter adapter = new EventListAdapter(EventsActivity.this, R.layout.list_item_event, EventsActivity.this);
                    adapter.addAll(genre.events);
                    eventLists.add(adapter);

                    for(Event e: genre.events) {
                        Log.d("EventsLoad", "Loading : "+e.name);
                        if (db.findEvent(e.id) != null) {
                            e.fav = true;
                            Log.d("EventsLoad", "Updating : "+e.name);
                            db.updateEvent(e);
                        }
                    }
                }

                mViewPager.setAdapter(new SamplePagerAdapter());
                mViewPager.addOnPageChangeListener(EventsActivity.this);

                mTabLayout.setupWithViewPager(mViewPager);
                changeTabsFont();

                mToolbar.setTitle(getToolbarTitle(eventsData.name));

                mIconView.setVisibility(View.VISIBLE);
                mIconView.setImageResource(getPageIcon(eventsData.genres[0].name_short));
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                retrofitError.printStackTrace();
                dialog.dismiss();
                //Toast.makeText(EventsActivity.this, "Error fetching data", Toast.LENGTH_LONG).show();
                showErrorDialog("Can't fetch data, Please check your internet connection");

            }
        };
        methods.getEvents(id, callback);
    }

    public Toolbar getToolbar() {
        return mToolbar;
    }

    private void changeTabsFont() {
        ViewGroup vg = (ViewGroup) mTabLayout.getChildAt(0);
        int tabsCount = vg.getChildCount();
        for (int j = 0; j < tabsCount; j++) {
            ViewGroup vgTab = (ViewGroup) vg.getChildAt(j);
            int tabChildsCount = vgTab.getChildCount();
            for (int i = 0; i < tabChildsCount; i++) {
                View tabViewChild = vgTab.getChildAt(i);
                if (tabViewChild instanceof TextView) {
                    ((TextView) tabViewChild).setTypeface(tabFont, Typeface.NORMAL);
                }
            }
        }
    }

    @Override
    public void onClick(View view) {
        int position = (Integer)view.getTag();
        Log.d("ItemClickListener","Pos:"+position);

        Intent i = new Intent(this, EventDetailsActivity.class);
        i.putExtra("event_details",eventsData.genres[mViewPager.getCurrentItem()].events[position]);
        startActivity(i);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        Log.d("EventsActivity","onPageScrolled : "+position+","+positionOffset+","+positionOffsetPixels);

        mIconView.setScaleX(Math.abs(positionOffset-0.5F)*2);
        mIconView.setScaleY(Math.abs(positionOffset-0.5F)*2);

        if((lastScroll-0.5)<0 && (positionOffset-0.5) > 0){
            mIconView.setImageResource(getPageIcon(eventsData.genres[position+1].name_short));
        }

        if((lastScroll-0.5)>0 && (positionOffset-0.5) < 0){
            mIconView.setImageResource(getPageIcon(eventsData.genres[position].name_short));
        }

        lastScroll=positionOffset;
    }

    @Override
    public void onPageSelected(int position) {
        //Log.d("EventsActivity","onPageSelected : "+position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        //Log.d("EventsActivity","onPageScrollStateChanged : "+state);
    }

    private int getPageIcon(String page){
        // COMPI ---->
        if(page.equalsIgnoreCase("dance")){
            return R.drawable.icon_compi_dance;
        } else if(page.equalsIgnoreCase("dada")){
            return R.drawable.icon_compi_design;
        } else if(page.equalsIgnoreCase("dram")){
            return R.drawable.icon_compi_drama;
        } else if(page.equalsIgnoreCase("finearts")){
            return R.drawable.icon_compi_art;
        } else if(page.equalsIgnoreCase("lifestyle")){
            return R.drawable.icon_compi_sports;
        } else if(page.equalsIgnoreCase("lit")){
            return R.drawable.icon_compi_lit;
        } else if(page.equalsIgnoreCase("music")){
            return R.drawable.icon_compi_music;
        } else if(page.equalsIgnoreCase("speaking")){
            return R.drawable.icon_compi_speaking_arts;
        }
        // INFORMALS ---->
        else if(page.equalsIgnoreCase("hysteria")){
            return R.drawable.icon_informals_hysteria;
        } else if(page.equalsIgnoreCase("StrategyGames")){
            return R.drawable.icon_informals_whack_out;
        } else if(page.equalsIgnoreCase("streets")){
            return R.drawable.icon_informals_streets;
        } else if(page.equalsIgnoreCase("Audiencecentric")){
            return R.drawable.icon_informals_ice_breakers;
        } else if(page.equalsIgnoreCase("icebreakers")){
            return R.drawable.icon_informals_ice_breakers;
        } else if(page.equalsIgnoreCase("Lounges")){
            return R.drawable.icon_informals_lounges;
        } else if(page.equalsIgnoreCase("adventurezone")){
            return R.drawable.icon_informals_adventure;
        } else if(page.equalsIgnoreCase("Casinos")){
            return R.drawable.icon_informals_casino;
        } else if(page.equalsIgnoreCase("Lifesizegames")){
            return R.drawable.icon_informals_lifesize_games;
        }

        // ARTS ---->
        else if(page.equalsIgnoreCase("foodfest")){
            return R.drawable.icon_arts_food_fest;
        } else if(page.equalsIgnoreCase("workshops")){
            return R.drawable.icon_arts_workshops;
        } else if(page.equalsIgnoreCase("NightFleaMarket")){
            return R.drawable.icon_arts_night_flea;
        } else if(page.equalsIgnoreCase("music")){
            return R.drawable.icon_arts_music_arena;
        } else if(page.equalsIgnoreCase("iart")){
            return R.drawable.icon_arts_interactive_arts;
        }

        // PROSHOWS ---->
        else if(page.equalsIgnoreCase("music")){
            return R.drawable.icon_proshows_imf;
        } else if(page.equalsIgnoreCase("humorfest")){
            return R.drawable.icon_proshows_humorfest;
        } else if(page.equalsIgnoreCase("fringe")){
            return R.drawable.icon_proshows_fringe_fest;
        } else if(page.equalsIgnoreCase("afternites")){
            return R.drawable.icon_proshows_afternights;
        } else if(page.equalsIgnoreCase("Litfest")){
            return R.drawable.icon_proshows_litfest;
        } else if(page.equalsIgnoreCase("Theatrefest")){
            return R.drawable.icon_proshows_theatre;
        }

        // CONCERTS ---->
        else if(page.equalsIgnoreCase("edm")){
            return R.drawable.icon_concerts_edm;
        } else if(page.equalsIgnoreCase("popular")){
            return R.drawable.icon_concerts_popular_nite;
        } else if(page.equalsIgnoreCase("nostalgia")){
            return R.drawable.icon_concerts_classical;
        }


        else {
            return R.drawable.icon_informals_ice_breakers;
        }
    }

    public int getGradient(int id){
        switch(id){
            case 1:
                return R.drawable.tabs_gradient_compi;
            case 2:
                return R.drawable.tabs_gradient_informals;
            case 3:
                return R.drawable.tabs_gradient_concerts;
            case 4:
                return R.drawable.tabs_gradient_arts;
            case 5:
                return R.drawable.tabs_gradient_proshows;
        }
        return R.drawable.tabs_gradient_compi;
    }

    public String getToolbarTitle(String genre){
            if(genre.equals("competitions"))
                return "Competitions";
            else if(genre.equals("informals"))
                return "Informals";
            else if(genre.equals("concerts"))
                return "Concerts";
            else if(genre.equals("proshows"))
                return "Proshows";
            else if(genre.equals("arts"))
                return "Arts and Workshops";
            else
                return "Competitions";
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        mService = ((BackgroundService.LocalBinder)service).getService();
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        mService=null;
    }

    class SamplePagerAdapter extends PagerAdapter {
        public static final String TAG = "EventListPagerAdapter";

        /**
         * @return the number of pages to display
         */
        @Override
        public int getCount() {
            return eventsData.genres.length;
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
            return eventsData.genres[position].name;
        }

        /**
         * Instantiate the {@link View} which should be displayed at {@code position}. Here we
         * inflate a layout from the apps resources and then change the text view to signify the position.
         */
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            // Inflate a new layout from our resources
            ListView view = new ListView(container.getContext(),null,R.style.EventListView);
            view.setAdapter(eventLists.get(position));

            // Add the newly created View to the ViewPager
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
