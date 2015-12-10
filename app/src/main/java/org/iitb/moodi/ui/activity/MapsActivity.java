package org.iitb.moodi.ui.activity;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.amulyakhare.textdrawable.TextDrawable;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import net.steamcrafted.materialiconlib.MaterialDrawableBuilder;

import org.iitb.moodi.BackgroundService;
import org.iitb.moodi.MoodIndigoClient;
import org.iitb.moodi.R;
import org.iitb.moodi.Util;
import org.iitb.moodi.api.FriendFinderRespone;
import org.iitb.moodi.api.VenueResponse;
import org.iitb.moodi.api.VenueResponse.Venue;
import org.iitb.moodi.ui.widget.InstantAutoComplete;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MapsActivity extends BaseActivity implements OnMapReadyCallback, ServiceConnection,
        BackgroundService.OnUpdateListener, AdapterView.OnItemClickListener {

    private static final int TYPE_ACCO=1;
    private static final int TYPE_AID=2;
    private static final int TYPE_FOOD=3;
    private static final int TYPE_EVENTS=4;

    private static final String TAG = "MapsActivity";
    private GoogleMap mMap;
    private FloatingActionMenu mFilterMenu;
    private Toolbar mToolbar;

    ArrayList<IdMarker> markers=new ArrayList<>();
    ArrayList<IdMarker> friend_markers=new ArrayList<>();
    ArrayAdapter<String> friend_suggestions;

    ProgressDialog dialog=null;
    private ArrayList<Venue> venues = new ArrayList<>();
    private BackgroundService mLocationTracker;
    private FloatingActionButton mFFControl;
    private InstantAutoComplete mFFSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        mFilterMenu = (FloatingActionMenu)findViewById(R.id.map_filter_menu);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);

        setSupportActionBar(mToolbar);

        mFFControl= (FloatingActionButton) findViewById(R.id.map_ff_control);
        mFFSearch = (InstantAutoComplete) findViewById(R.id.map_ff_search_bar);
        friend_suggestions=new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line);
        mFFSearch.setAdapter(friend_suggestions);
        mFFSearch.setOnItemClickListener(this);

        bindService(new Intent(MapsActivity.this, BackgroundService.class),
                MapsActivity.this,
                Context.BIND_AUTO_CREATE);
    }

    private void loadMap(){
        dialog = ProgressDialog.show(MapsActivity.this, "",
                "Loading Map...", true);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(MapsActivity.this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        mToolbar.setTitle("Map");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng iitb = new LatLng(19.13124,72.91649);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(iitb,15.3F));
        mMap.setMyLocationEnabled(true);

        dialog.setMessage("Fetching Data...");

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(m_API_URL)
                .build();
        MoodIndigoClient methods = restAdapter.create(MoodIndigoClient.class);
        Callback callback = new Callback() {
            @Override
            public void success(Object o, Response response) {
                VenueResponse vr = (VenueResponse) o;
                for (Venue v : vr.venues){
                    venues.add(v);
                    //Log.d(TAG, v.venue_name+" : "+v.location);
                    IdMarker m = new IdMarker(mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(v.getLat(),v.getLng()))
                            .title(v.venue_name)
                            .icon(getIcon(v.venue_type))),
                            v.venue_type!=null?v.venue_type:TYPE_EVENTS+"");
                    markers.add(m);
                }
                dialog.dismiss();
            }
            @Override
            public void failure(RetrofitError retrofitError) {
                String error = retrofitError.getMessage();
                Log.e(TAG, error);
                dialog.dismiss();
                showErrorDialog("Can't fetch data. Please check your internet connection!");
            }
        };
        methods.getVenues(callback);
    }

    private BitmapDescriptor getIcon(String venue_type) {
        int venueType = venue_type!=null?Integer.valueOf(venue_type):TYPE_EVENTS;
        switch(venueType){
            case TYPE_ACCO:
                return BitmapDescriptorFactory.fromBitmap(
                        Util.drawableToBitmap(MaterialDrawableBuilder
                                .with(this)
                                .setIcon(MaterialDrawableBuilder.IconValue.HOTEL)
                                .setColor(getResources().getColor(R.color.color_compi))
                                .setSizeDp(30)
                                .build()));
            case TYPE_AID:
                return BitmapDescriptorFactory.fromBitmap(
                        Util.drawableToBitmap(MaterialDrawableBuilder
                                .with(this)
                                .setIcon(MaterialDrawableBuilder.IconValue.HOSPITAL)
                                .setColor(getResources().getColor(R.color.color_concerts))
                                .setSizeDp(30)
                                .build()));
            case TYPE_FOOD:
                return BitmapDescriptorFactory.fromBitmap(
                        Util.drawableToBitmap(MaterialDrawableBuilder
                                .with(this)
                                .setIcon(MaterialDrawableBuilder.IconValue.FOOD)
                                .setColor(getResources().getColor(R.color.color_arts))
                                .setSizeDp(30)
                                .build()));
            case TYPE_EVENTS:
            default:
                return BitmapDescriptorFactory.fromBitmap(
                        Util.drawableToBitmap(MaterialDrawableBuilder
                                .with(this)
                                .setIcon(MaterialDrawableBuilder.IconValue.TICKET)
                                .setColor(getResources().getColor(R.color.color_informals))
                                .setSizeDp(30)
                                .build()));

        }
    }

    public void filterMap(View v){
        int type = TYPE_EVENTS;
        switch(v.getId()){
            case R.id.map_filter_acco:
                type=TYPE_ACCO;
                break;
            case R.id.map_filter_food:
                type=TYPE_FOOD;
                break;
            case R.id.map_filter_aid:
                type=TYPE_AID;
                break;
            case R.id.map_filter_events:
                type=TYPE_EVENTS;
                break;
            case R.id.map_filter_clear:
                for(IdMarker m : markers){
                    m.marker.setVisible(true);
                }
                mFilterMenu.close(true);
                return;
        }

        for(IdMarker m : markers){
            if(m.id.equals(type+""))
                m.marker.setVisible(true);
            else
                m.marker.setVisible(false);
        }
        mFilterMenu.close(true);
    }

    public void ffControl(View v){
        if(mLocationTracker!=null){
            if(mLocationTracker.isFriendFinderRunning()){
                mFFControl.setColorNormal(Color.GRAY);
                //findViewById(R.id.map_ff_search_container).setVisibility(View.GONE);
                mLocationTracker.stopFriendFinder();
            } else {
                mFFControl.setColorNormalResId(R.color.colorPrimary);
                //findViewById(R.id.map_ff_search_container).setVisibility(View.VISIBLE);
                mLocationTracker.startFriendFinder();
            }
        }
    }

    @Override
    protected void onDestroy() {
        if(mLocationTracker!=null) {
            mLocationTracker.stopOnUpdateListener();
            unbindService(this);
        }
        super.onDestroy();
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        mLocationTracker = ((BackgroundService.LocalBinder)service).getService();
        mLocationTracker.setOnUpdateListener(this);
        mLocationTracker.updateUserInfo();

        if(!mLocationTracker.isFriendFinderRunning()) {
            mFFControl.setColorNormal(Color.GRAY);
            new AlertDialog.Builder(this)
                    .setTitle("Switch on Friend-Finder?")
                    .setMessage("Friend-Finder helps you view the locations " +
                            "of your friends on the map. " +
                            "They can also view your location on their map. " +
                            "Note that your location will only be shared with your facebook " +
                            "friends who have also enabled this feature.")
                    .setCancelable(true)
                    .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface d, int which) {
                            d.cancel();
                        }
                    }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface d, int which) {
                            d.dismiss();
                            loadMap();
                            mFFControl.setColorNormalResId(R.color.colorPrimary);
                            mLocationTracker.startFriendFinder();
                        }
                    })
                    .setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface d) {
                            loadMap();
                        }
                    })
                    .show();
        } else {
            mFFControl.setColorNormalResId(R.color.colorPrimary);
            loadMap();
        }
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        mLocationTracker = null;
    }

    @Override
    public void onUpdate(ArrayList<FriendFinderRespone.Friend> arr) {
        Log.d("MapsActivity", "onUpdate:"+arr.size());
        friend_suggestions.clear();
        friend_markers.clear();

        for(FriendFinderRespone.Friend f : arr) {
            Log.d("MapsActivity", "onUpdate: created new marker for "+f.initials()+", "+f.name);
            friend_markers.add(new IdMarker(
                mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(f.getLat(),f.getLng()))
                    .icon(BitmapDescriptorFactory.
                            fromBitmap(Util.drawableToBitmap(TextDrawable.builder()
                            .beginConfig()
                                .bold()
                                .textColor(0xFF000000)
                                .fontSize(25)
                            .endConfig()
                            .buildRect(f.initials(), 0x00000000))))
                    .title(f.name)),f.fbid));
            friend_suggestions.add(f.name);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mMap.animateCamera(CameraUpdateFactory
                .newLatLngZoom(friend_markers.get(position).marker.getPosition(), 17));
    }

    private class IdMarker{
        Marker marker;
        String id;

        public IdMarker(Marker m, String i){
            marker=m;id=i;
        }
    }
}
