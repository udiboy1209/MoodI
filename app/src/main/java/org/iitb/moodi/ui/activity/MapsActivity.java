package org.iitb.moodi.ui.activity;

import android.app.ProgressDialog;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

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

import org.iitb.moodi.MoodIndigoClient;
import org.iitb.moodi.R;
import org.iitb.moodi.Util;
import org.iitb.moodi.api.EventDetailsResponse;
import org.iitb.moodi.api.VenueResponse;
import org.iitb.moodi.api.VenueResponse.Venue;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MapsActivity extends BaseActivity implements OnMapReadyCallback {

    private static final int TYPE_ACCO=1;
    private static final int TYPE_AID=2;
    private static final int TYPE_FOOD=3;
    private static final int TYPE_EVENTS=4;

    private static final String TAG = "MapsActivity";
    private GoogleMap mMap;
    private FloatingActionMenu mFilterMenu;
    private Toolbar mToolbar;

    ArrayList<Marker> markers=new ArrayList<>();

    ProgressDialog dialog=null;
    private ArrayList<Venue> venues = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mFilterMenu = (FloatingActionMenu)findViewById(R.id.map_filter_menu);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);

        setSupportActionBar(mToolbar);

        dialog = ProgressDialog.show(this, "",
                "Loading Map...", true);
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
                    Marker m = mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(v.getLat(),v.getLng()))
                            .title(v.venue_name)
                            .icon(getIcon(v.venue_id))
                            .snippet(v.venue_type!=null?v.venue_type:TYPE_EVENTS+""));
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
        switch(v.getId()){
            case R.id.map_filter_acco:
                for(Marker m : markers){
                    if(m.getSnippet().equals(TYPE_ACCO+""))
                        m.setVisible(true);
                    else
                        m.setVisible(false);
                }
                break;
            case R.id.map_filter_food:
                for(Marker m : markers){
                    if(m.getSnippet().equals(TYPE_FOOD+""))
                        m.setVisible(true);
                    else
                        m.setVisible(false);
                }
                break;
            case R.id.map_filter_aid:
                for(Marker m : markers){
                    if(m.getSnippet().equals(TYPE_AID+""))
                        m.setVisible(true);
                    else
                        m.setVisible(false);
                }
                break;
            case R.id.map_filter_events:
                for(Marker m : markers){
                    if(m.getSnippet().equals(TYPE_EVENTS+""))
                        m.setVisible(true);
                    else
                        m.setVisible(false);
                }
                break;

            case R.id.map_filter_clear:
                for(Marker m : markers){
                    m.setVisible(true);
                }
                break;
        }

        mFilterMenu.close(true);
    }
}
