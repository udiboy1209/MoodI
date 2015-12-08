package org.iitb.moodi;


import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import org.iitb.moodi.api.FriendFinderRespone;
import org.iitb.moodi.api.User;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class LocationTrackerService extends Service implements LocationListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{

    private static final String TAG = "LocationTracker";
    private static final long INTERVAL = 1000 * 60 * 5;
    private static final long FASTEST_INTERVAL = 1000 * 60 * 4;

    public final String API_URL = "https://moodi.org";
    public final String m_API_URL = "http://m.moodi.org";

    private boolean RUNNING=false;

    private User me;
    private SharedPreferences prefs;

    public FusedLocationProviderApi locApi = LocationServices.FusedLocationApi;
    public GoogleApiClient apiClient;
    public LocationRequest locRequest;
    public Location location;
    public String lastUpdateTime;

    private final IBinder mBinder = new LocalBinder();
    private OnUpdateListener mOnUpdateListener=null;

    private ArrayList<FriendFinderRespone.Friend> friends = new ArrayList<>();

    @Override
    public void onCreate(){
        super.onCreate();
        Log.d(TAG,"Create called");
        apiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        locRequest = new LocationRequest();
        locRequest.setInterval(INTERVAL);
        locRequest.setFastestInterval(FASTEST_INTERVAL);
        locRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        prefs=getSharedPreferences(getString(R.string.preferences),MODE_PRIVATE);

        Log.d(TAG,"Friend-Finder started");
        updateUserInfo();
        RUNNING=prefs.getBoolean(getString(R.string.ff_preference_running),false);

        if(RUNNING) connect();
    }

    public LocationTrackerService(){
    }

    public boolean startFriendFinder(){
        if(!RUNNING) {
            RUNNING=true;
            connect();
            return true;
        }
        return false;
    }

    public boolean stopFriendFinder(){
        if(RUNNING) {
            RUNNING=false;
            disconnect();
            if(mOnUpdateListener!=null)
                mOnUpdateListener.onUpdate(new ArrayList<FriendFinderRespone.Friend>());
            return true;
        }
        return false;
    }

    public boolean isFriendFinderRunning() {
        return RUNNING;
    }

    public void connect(){
        apiClient.connect();
    }

    public void disconnect(){
        apiClient.disconnect();
    }

    public Location getLastKnownLocation(){
        if(location==null){
            location = locApi.getLastLocation(apiClient);
            return location;
        } else {
            return location;
        }
    }

    public void startLocationUpdates() {

        LocationServices.FusedLocationApi.requestLocationUpdates(
                apiClient, locRequest, this);
        Log.d(TAG, "Location update started .......................");
    }

    public void stopLocationUpdates() {
        if(apiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(
                    apiClient, this);
        }
        Log.d(TAG, "Location update stopped .......................");
    }

    public Location getLocation(){ return location; }

    public double getLatitude(){
        return location.getLatitude();
    }

    public double getLongitude(){
        return location.getLongitude();
    }

    @Override
    public void onLocationChanged(Location location) {
        this.location = location;
        lastUpdateTime = DateFormat.getDateTimeInstance().format(new Date());

        Log.v(TAG, "Location: " + getLatitude() + "," + getLongitude() + ", Time: " + lastUpdateTime);

        if(me.mi_no!=null) {
            RestAdapter restAdapter = new RestAdapter.Builder()
                    .setEndpoint(m_API_URL)
                    .build();
            MoodIndigoClient methods = restAdapter.create(MoodIndigoClient.class);
            Callback callback = new Callback() {
                @Override
                public void success(Object o, Response response) {
                    FriendFinderRespone c = (FriendFinderRespone) o;

                    Log.d("FFSuccess", "friend count:"+c.friends.length);

                    friends.clear();
                    friends.addAll(Arrays.asList(c.friends));

                    if(mOnUpdateListener!=null) mOnUpdateListener.onUpdate(friends);
                }

                @Override
                public void failure(RetrofitError retrofitError) {
                    retrofitError.printStackTrace();
                }
            };
            methods.locationUpdate(me.mi_no, me.fbid, me.name, getLatitude()+","+getLongitude(),
                    mOnUpdateListener!=null, callback);
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.d(TAG, "onConnected - isConnected ...............: " + apiClient.isConnected());
        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "Connection suspended: ");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG, "Connection failed: " + connectionResult.toString());
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @Override
    public void onDestroy(){
        stopLocationUpdates();
        disconnect();

        prefs.edit().putBoolean(getString(R.string.ff_preference_running),RUNNING);

        super.onDestroy();

        Log.d(TAG, "Friend-Finder stopped");
        //Toast.makeText(this, "Friend-Finder stopped",Toast.LENGTH_LONG).show();
    }

    public ArrayList<FriendFinderRespone.Friend> getFriends(){
        return friends;
    }

    public void updateUserInfo(){
        try {
            me=new User(new JSONObject(
                    getSharedPreferences(getString(R.string.preferences),MODE_PRIVATE)
                            .getString("user_json","")));
            Log.d("LocationTrackerService", me.getJSON());
        }
        catch (Exception e) {
            e.printStackTrace();
            me = new User();
        }
    }

    public void setOnUpdateListener(OnUpdateListener ll){
        mOnUpdateListener=ll;
    }

    public void stopOnUpdateListener(){
        mOnUpdateListener=null;
    }

    public class LocalBinder extends Binder {
        public LocationTrackerService getService() {
            // Return this instance of LocalService so clients can call public methods
            return LocationTrackerService.this;
        }
    }

    public interface OnUpdateListener{
        void onUpdate(ArrayList<FriendFinderRespone.Friend> arr);
    }
}