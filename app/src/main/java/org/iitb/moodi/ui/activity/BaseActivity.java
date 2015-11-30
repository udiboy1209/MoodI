package org.iitb.moodi.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import org.iitb.moodi.BaseApplication;
import org.iitb.moodi.DatabaseHandler;
import org.iitb.moodi.R;
import org.iitb.moodi.api.CityResponse;
import org.iitb.moodi.api.User;
import org.json.JSONException;
import org.json.JSONObject;

public class BaseActivity extends AppCompatActivity {

    public User me;
    public CityResponse.City[] cities;
    public final String API_URL = "https://moodi.org";
    public final String m_API_URL = "http://m.moodi.org";
    SharedPreferences prefs;
    DatabaseHandler db;
    private static final String TAG = "BaseActivity";
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        me = new User();
        prefs=getSharedPreferences(getString(R.string.preferences), MODE_PRIVATE);
        db=BaseApplication.getDB();
        try {
            me=new User(new JSONObject(prefs.getString("user_json","")));
            Log.d("MyAct", me.getJSON());
        }
        catch (Exception e) {
            e.printStackTrace();
            me = new User();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch(id){
            case R.id.action_faq:
                String url = "http://faq.moodi.org";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
                return true;
            case R.id.action_share:
                startActivity(new Intent(this, ShareActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public Toolbar getToolbar() {
        return mToolbar;
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

    public int getColorFromGenre(String genre){
        if(genre.equals("competitions"))
            return getResources().getColor(R.color.color_compi);
        else if(genre.equals("informals"))
            return getResources().getColor(R.color.color_informals);
        else if(genre.equals("concerts"))
            return getResources().getColor(R.color.color_concerts);
        else if(genre.equals("proshows"))
            return getResources().getColor(R.color.color_proshows);
        else if(genre.equals("arts"))
            return getResources().getColor(R.color.color_arts);
        else
            return getResources().getColor(R.color.color_compi);
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

    public int getIconResource(int id){
        switch(id){
            case 1:
                return R.drawable.list_icon_compi;
            case 2:
                return R.drawable.list_icon_informals;
            case 3:
                return R.drawable.list_icon_concerts;
            case 4:
                return R.drawable.list_icon_proshows;
            case 5:
                return R.drawable.list_icon_arts;
        }
        return R.drawable.list_icon_compi;
    }

    public void navigateTo(int p){
        if(p==-1){
            startActivity(new Intent(this,ProfileActivity.class));
        } else if(p==1){
            startActivity(new Intent(this,ScheduleActivity.class));
        }
    }
}
