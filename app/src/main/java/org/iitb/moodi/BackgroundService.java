package org.iitb.moodi;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import org.iitb.moodi.api.Event;
import org.iitb.moodi.api.FriendFinderRespone;
import org.iitb.moodi.api.User;
import org.iitb.moodi.ui.activity.MapsActivity;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class BackgroundService extends Service implements LocationListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{

    private static final String TAG = "BackgroundService";
    private static final long INTERVAL = 1000 * 60;
    private static final long FASTEST_INTERVAL = 1000 * 30;

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
    private Notification mFFNotification;
    private NotificationManager mNotificationManager;

    private ArrayList<Event> notifEvents=new ArrayList<>();
    private Handler notifHandle;

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

        Log.d(TAG,"Background service started");
        updateUserInfo();
        RUNNING=prefs.getBoolean(getString(R.string.ff_preference_running),false);

        notifHandle = new Handler(getMainLooper());

        Intent resultIntent = new Intent(this, MapsActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent ffPI =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        mFFNotification = new NotificationCompat.Builder(this)
                .setContentTitle("Friend-Finder is active")
                .setContentInfo("View the location of your friends on the map")
                .setSmallIcon(R.drawable.ic_friend_finder)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.ic_launcher))
                .setAutoCancel(false)
                .setColor(getResources().getColor(R.color.colorSecondary))
                .setContentIntent(ffPI)
                .build();
        mFFNotification.flags |= Notification.FLAG_NO_CLEAR | Notification.FLAG_ONGOING_EVENT;

        mNotificationManager=(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if(RUNNING) startFriendFinder();
    }

    public BackgroundService(){
    }

    public boolean startFriendFinder(){
        Log.d("FriendFinder", "ff started");
        RUNNING=true;
        connect();
        prefs.edit().putBoolean(getString(R.string.ff_preference_running),RUNNING).apply();
        mNotificationManager.notify(007, mFFNotification);
        return true;
    }

    public boolean stopFriendFinder(){
        Log.d("FriendFinder", "ff stopped");
        RUNNING=false;
        disconnect();
        mNotificationManager.cancel(007);
        if(mOnUpdateListener!=null)
            mOnUpdateListener.onUpdate(new ArrayList<FriendFinderRespone.Friend>());
        prefs.edit().putBoolean(getString(R.string.ff_preference_running),RUNNING).apply();
        return true;
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
        super.onDestroy();

        Log.d(TAG, "Background service stopped");
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
            Log.d("BackgroundService", me.getJSON());
        }
        catch (Exception e) {
            e.printStackTrace();
            me = new User();
        }
    }

    public void setOnUpdateListener(OnUpdateListener ll){
        mOnUpdateListener=ll;
        //if(mOnUpdateListener!=null) mOnUpdateListener.onUpdate(friends);
    }

    public void stopOnUpdateListener(){
        mOnUpdateListener=null;
    }

    public void updateEventNotifications() {
        notifEvents.clear();
    }

    public class LocalBinder extends Binder {
        public BackgroundService getService() {
            // Return this instance of LocalService so clients can call public methods
            return BackgroundService.this;
        }
    }

    public interface OnUpdateListener{
        void onUpdate(ArrayList<FriendFinderRespone.Friend> arr);
    }
}