package org.iitb.moodi.ui.activity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.iitb.moodi.api.CityList;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

import org.iitb.moodi.BaseActivity;
import org.iitb.moodi.R;

public class RegistrationActivity extends BaseActivity {

    Spinner gender;
    private static final String TAG = "RegistrationActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        String[] items = new String[] {"Male","Female"};
        gender = (Spinner) findViewById(R.id.reg_gender);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gender.setAdapter(adapter);

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(API_URL)
                .build();
        MoodIndigoClient methods = restAdapter.create(MoodIndigoClient.class);
        Callback callback = new Callback() {
            @Override
            public void success(Object o, Response response) {
                CityList cities = (CityList)o;
                Toast.makeText(getBaseContext(),""+cities.getCityList().length,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                String error = retrofitError.getMessage();
                Toast.makeText(getBaseContext(),error,Toast.LENGTH_SHORT).show();
            }
        };
        methods.getCities(callback);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_registration, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public String getEditText(int id){
        return ((EditText)findViewById(id)).getText().toString();
    }

    public void setEditText(String text, int id){
        ((EditText)findViewById(id)).setText(text);
    }

    public boolean validateData() {
        boolean check = true;
        String emptyErrorMsg = getResources().getString(R.string.empty_error);
        int[] checksArray = {R.id.reg_name,R.id.reg_city, R.id.reg_email, R.id.reg_year,
                             R.id.reg_dob,R.id.reg_phone,R.id.reg_college};
        // Checking whether edit texts are empty
        for(int id : checksArray) {
            if (getEditText(id).equals("")) {
                ((EditText)findViewById(id)).setError(emptyErrorMsg);
                check = false;
            }
        }
        if(check) {
            if(!Patterns.EMAIL_ADDRESS.matcher(((EditText)findViewById(R.id.reg_email)).getText()).matches()){
                ((EditText)findViewById(R.id.reg_email)).setError("Invalid email");
                check= false;
            }

            String phone = ((EditText)findViewById(R.id.reg_phone)).getText().toString();
            String phoneregex = "^[7-9][0-9]{9}$";
            if(!phone.matches(phoneregex)) {
                ((EditText)findViewById(R.id.reg_phone)).setError("Please type 10 digit phone number");
                check= false;
            }
        }
        return check;
    }
    public void saveData(View view) {
        if (!validateData())
            return;
    }

}
