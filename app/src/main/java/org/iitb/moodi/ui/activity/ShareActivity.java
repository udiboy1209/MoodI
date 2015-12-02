package org.iitb.moodi.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

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
        String url = "https://play.google.com/store/apps/details?id=org.iitb.moodi";
        String content = "Hey! I am enjoying using the official Mood Indigo app! It's time you have it too! To get live updates of all events, the entire Mood Indigo schedule and a lot more interactive activities download the app from the play store ";

        switch (v.getId()){
            case R.id.share_button_facebook:
                /*Intent intent1 = new Intent();
                intent1.putExtra("android.intent.extra.TEXT", url);
                intent1.setClassName("com.facebook.katana", "com.facebook.katana.activity.composer.ImplicitShareIntentHandler");
                intent1.setAction("android.intent.action.SEND");
                intent1.setType("text/plain");
                startActivity(intent1);*/

                ShareLinkContent link = new ShareLinkContent.Builder()
                        .setContentUrl(Uri.parse(url))
                        .setContentTitle(content)
                        .setContentDescription(content)
                        .build();
                ShareDialog.show(this,link);
                break;
            case R.id.share_button_twitter:
                String tweetUrl =
                        String.format("https://twitter.com/intent/tweet?text=%s&url=%s",
                                URLEncoder.encode(content), URLEncoder.encode(url));
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(tweetUrl));
                startActivity(intent);
                break;
            case R.id.share_button_whatsapp:
                Intent sendIntent = new Intent();
                sendIntent.setPackage("com.whatsapp");
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, content+url);
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
                break;
        }

        finish();
    }

    public void close(View v){
        finish();
    }
}
