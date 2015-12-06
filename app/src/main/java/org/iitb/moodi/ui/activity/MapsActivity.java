package org.iitb.moodi.ui.activity;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
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

import org.iitb.moodi.LocationTrackerService;
import org.iitb.moodi.MoodIndigoClient;
import org.iitb.moodi.R;
import org.iitb.moodi.Util;
import org.iitb.moodi.api.EventDetailsResponse;
import org.iitb.moodi.api.FriendFinderRespone;
import org.iitb.moodi.api.VenueResponse;
import org.iitb.moodi.api.VenueResponse.Venue;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MapsActivity extends BaseActivity implements OnMapReadyCallback, ServiceConnection,
        LocationTrackerService.OnUpdateListener {

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

    ProgressDialog dialog=null;
    private ArrayList<Venue> venues = new ArrayList<>();
    private LocationTrackerService mLocationTracker;

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


        if(!LocationTrackerService.RUNNING) {
            new AlertDialog.Builder(this)
                    .setTitle("Switch on Friend-Finder?")
                    .setMessage("Friend-Finder helps you view the locations of your friends on the map. " +
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

                    startService(new Intent(MapsActivity.this, LocationTrackerService.class));
                    bindService(new Intent(MapsActivity.this, LocationTrackerService.class), MapsActivity.this, Context.BIND_AUTO_CREATE);
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
            loadMap();
            bindService(new Intent(MapsActivity.this, LocationTrackerService.class), MapsActivity.this, Context.BIND_AUTO_CREATE);
        }
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
        } else if(item.getItemId() == R.id.action_ff){
            if(mLocationTracker!=null){
                mLocationTracker.stopOnUpdateListener();
                unbindService(this);
                mLocationTracker=null;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng iitb = new LatLng(19.13124,72.91649);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(iitb,15.3F));

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
                    Log.d(TAG, v.venue_name+" : "+v.location);
                    IdMarker m = new IdMarker(mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(v.getLat(),v.getLng()))
                            .title(v.venue_name)
                            .icon(getIcon(v.venue_id))),
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
                finish();
            }
        };
        methods.getVenues(callback);
    }

    private BitmapDescriptor getIcon(String venue_id) {
        int venueType = getVenueType(venue_id);
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

    private int getVenueType(String venue_id) {
        int id = Integer.valueOf(venue_id);
        switch (id){
            default:
                return TYPE_EVENTS;
        }
    }

    public void filterMap(View v){
        int type = TYPE_EVENTS;
        switch(v.getId()){
            case R.id.map_filter_acco:
                type=TYPE_ACCO;
                break;
            case R.id.map_filter_food:
                type=TYPE_ACCO;
                break;
            case R.id.map_filter_aid:
                type=TYPE_ACCO;
                break;
            case R.id.map_filter_events:
                type=TYPE_ACCO;
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
        mLocationTracker = ((LocationTrackerService.LocalBinder)service).getService();
        mLocationTracker.setOnUpdateListener(this);
        mLocationTracker.updateUserInfo();
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        mLocationTracker = null;
    }

    @Override
    public void onUpdate(ArrayList<FriendFinderRespone.Friend> arr) {
        Log.d("MapsActivity", "onUpdate:"+arr.size());
        if(arr.size()>0) Log.d("MapsActivity", "onUpdateData:"+arr.get(0).location);
        for(FriendFinderRespone.Friend f : arr) {
            boolean found=false;
            for (IdMarker m : friend_markers) {
                if(m.id.equals(f.fbid)){
                    m.marker.setPosition(new LatLng(f.getLat(),f.getLng()));
                    Log.d("MapsActivity", "onUpdate: found existing marker");
                    found=true;
                    break;
                }
            }

            if(!found){
                Log.d("MapsActivity", "onUpdate: created new marker");
                friend_markers.add(new IdMarker(
                    mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(f.getLat(),f.getLng()))
                        /*.icon(BitmapDescriptorFactory.fromBitmap(Util.drawableToBitmap(TextDrawable.builder()
                                .beginConfig()
                                    .bold()
                                    .textColor(0xFF000000)
                                    .fontSize(20)
                                .endConfig()
                                .buildRound(f.initials(), 0xFF818181))))*/
                        .title(f.name)),f.fbid));
            }
        }
    }

    private class IdMarker{
        Marker marker;
        String id;

        public IdMarker(Marker m, String i){
            marker=m;id=i;
        }
    }
}
