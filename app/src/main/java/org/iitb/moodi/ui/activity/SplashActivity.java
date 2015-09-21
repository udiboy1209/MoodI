package org.iitb.moodi.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;

import org.iitb.moodi.MainActivity;
import org.iitb.moodi.R;

public class SplashActivity extends AppCompatActivity {

    private static final String TAG = "SplashActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Session session = Session.getActiveSession();

        if(session!=null && session.isOpened()) {
            findViewById(R.id.fb_login_button).setVisibility(View.GONE);
            startActivity(new Intent(this, MainActivity.class));
        } else {
            findViewById(R.id.fb_login_button).setVisibility(View.VISIBLE);
        }

        ((LoginButton)findViewById(R.id.fb_login_button)).setReadPermissions("email");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("LoginDebug", "ActivityResult");
        Session.getActiveSession().onActivityResult(this, requestCode,
                resultCode, data);

        getBiodata();

        super.onActivityResult(requestCode, resultCode, data);
    }

    public void getBiodata(){
        Log.d("LoginDebug", "getBioData");
        Request.executeMeRequestAsync(Session.getActiveSession(), new Request.GraphUserCallback() {
            @Override
            public void onCompleted(GraphUser graphUser, Response response) {
                Log.i(TAG, "fbid : " + graphUser.getId() + " " + graphUser.getName() + " " + (String) graphUser.getProperty("email") + (String) graphUser.getProperty("gender"));
            }
        });
    }
}
