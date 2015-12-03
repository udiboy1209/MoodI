package org.iitb.moodi.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.iitb.moodi.BaseApplication;
import org.iitb.moodi.R;
import org.iitb.moodi.api.Event;
import org.iitb.moodi.ui.widget.CircularDPView;
import org.iitb.moodi.ui.widget.EventListAdapter;


public class ProfileActivity extends BaseActivity implements View.OnClickListener {
    EventListAdapter favEvents;
    LinearLayout eventList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        favEvents = new EventListAdapter(this,R.layout.list_item_fav_event, this);
        eventList = (LinearLayout) findViewById(R.id.profile_fav_event_list);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_menu_white_24dp);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);

        ((CircularDPView)findViewById(R.id.profile_dp)).setProfileId(me.fbid);
        ((TextView)findViewById(R.id.profile_name)).setText(me.name);
        ((TextView)findViewById(R.id.profile_mi_no)).setText(me.mi_no);

        mColorPrimary=R.drawable.toolbar_shadow_gradient;
        mColorPrimaryDark=R.color.colorDark;
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateData();
        updateList();

        mToolbar.setTitle("MI Profile");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
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

    public void updateData(){
        favEvents.clear();
        favEvents.addAll(BaseApplication.getDB().getEvents());
    }

    public void updateList(){
        eventList.removeAllViews();
        for(int i=0; i<favEvents.getCount(); i++){
            eventList.addView(favEvents.getView(i, null, eventList));
        }
    }

    @Override
    public void onClick(View v) {
        Event e = favEvents.getItem((Integer)v.getTag());

        Intent i = new Intent(this, EventDetailsActivity.class);
        i.putExtra("event_details",e);
        startActivity(i);
    }
}
