package org.iitb.moodi.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import org.iitb.moodi.api.CityResponse;
import org.iitb.moodi.api.User;

public class BaseActivity extends AppCompatActivity {

    public User me;
    public CityResponse.City[] cities;
    public final String API_URL = "https://moodi.org";
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        me = new User();
    }

    public Toolbar getToolbar() {
        return mToolbar;
    }
}
