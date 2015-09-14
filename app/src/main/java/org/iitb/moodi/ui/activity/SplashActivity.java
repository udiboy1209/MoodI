package org.iitb.moodi.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.facebook.Session;
import com.facebook.widget.LoginButton;

import org.iitb.moodi.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Session session = Session.getActiveSession();

        if(session!=null && session.isOpened()) {
            findViewById(R.id.fb_login_button).setVisibility(View.GONE);

        } else {
            findViewById(R.id.fb_login_button).setVisibility(View.VISIBLE);
        }

        ((LoginButton)findViewById(R.id.fb_login_button)).setReadPermissions("email");
    }


}
