package org.iitb.moodi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import org.iitb.moodi.api.User;

public class BaseActivity extends AppCompatActivity {

    public User me;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        me = new User();
    }

}
