package org.iitb.moodi.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import org.iitb.moodi.R;

import java.net.URLEncoder;

/**
 * Created by udiboy on 29/11/15.
 */
public class ShareActivity extends BaseActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
    }

    public void share(View v){

        switch (v.getId()){
            case R.id.share_button_facebook:
                Intent intent1 = new Intent();
                intent1.putExtra("android.intent.extra.TEXT", "http://moodi.org");
                intent1.setClassName("com.facebook.katana", "com.facebook.katana.activity.composer.ImplicitShareIntentHandler");
                intent1.setAction("android.intent.action.SEND");
                intent1.setType("text/plain");
                startActivity(intent1);
                return;
            case R.id.share_button_twitter:
                String tweetUrl =
                        String.format("https://twitter.com/intent/tweet?text=%s&url=%s",
                                URLEncoder.encode("Share Mood I app"), URLEncoder.encode("http://moodi.org"));
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(tweetUrl));
                startActivity(intent);
                return;
            case R.id.share_button_whatsapp:
                Intent sendIntent = new Intent();
                sendIntent.setPackage("com.whatsapp");
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
                return;
        }

        finish();
    }
}
