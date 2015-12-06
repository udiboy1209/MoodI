package org.iitb.moodi.ui.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.iitb.moodi.MoodIndigoClient;
import org.iitb.moodi.R;
import org.iitb.moodi.api.ResultResponse;
import org.iitb.moodi.api.VenueResponse;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class ResultActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    private static final String TAG = "ResultActivity";
    ArrayAdapter<String> suggestions;
    AutoCompleteTextView search;
    ArrayList<ResultResponse.Result> results=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_menu_white_24dp);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);

        suggestions = new ArrayAdapter<>(this,android.R.layout.simple_dropdown_item_1line);

        search = (AutoCompleteTextView)findViewById(R.id.result_event_list);
        search.setAdapter(suggestions);
        search.setThreshold(2);
        search.setOnItemClickListener(this);
        loadData();
    }

    @Override
    protected void onResume() {
        super.onResume();

        mToolbar.setTitle("Results");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    public void loadData(){
        final Dialog dialog= ProgressDialog.show(this,"","Fetching Results...",true);

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(m_API_URL)
                .build();
        MoodIndigoClient methods = restAdapter.create(MoodIndigoClient.class);
        Callback callback = new Callback() {
            @Override
            public void success(Object o, Response response) {
                ResultResponse vr = (ResultResponse) o;
                for (ResultResponse.Result v : vr.results){
                    suggestions.add(v.name);
                    results.add(v);
                }
                dialog.dismiss();
            }
            @Override
            public void failure(RetrofitError retrofitError) {
                String error = retrofitError.getMessage();
                Log.e(TAG, error);
                dialog.dismiss();
                showErrorDialog("Can't fetch data. Please check your internet connection!");
                finish();
            }
        };
        methods.getResults(callback);
    }

    public void reload(View v){
        results.clear();
        suggestions.clear();

        ((TextView)findViewById(R.id.result_first)).setText("");
        ((TextView)findViewById(R.id.result_second)).setText("");
        ((TextView)findViewById(R.id.result_third)).setText("");

        loadData();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.wtf(TAG, "onItemSelected");
        ((TextView)findViewById(R.id.result_first)).setText(results.get(position).first);
        ((TextView)findViewById(R.id.result_second)).setText(results.get(position).second);
        ((TextView)findViewById(R.id.result_third)).setText(results.get(position).third);
    }
}
