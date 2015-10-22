package org.iitb.moodi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import org.iitb.moodi.api.User;

public class BaseActivity extends AppCompatActivity {

    public User me;
    public final String API_URL = "https://moodi.org";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        me = new User();
    }

}
