package org.iitb.moodi.ui.activity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.iitb.moodi.BaseActivity;
import org.iitb.moodi.MoodIndigoClient;
import org.iitb.moodi.R;
import org.iitb.moodi.api.CityList;
import org.iitb.moodi.api.College;

import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class RegistrationActivity extends BaseActivity implements AdapterView.OnItemSelectedListener {

    CityList.City[] cities;
    College[] colleges;
    Spinner gender, city, college, year;
    String[] gender_choices = new String[] {"Male","Female", "Gender"};
    String[] year_choices = new String[] {"First","Second","Third","Fourth","Fifth"};
    private static final String TAG = "RegistrationActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        // Working on Gender Spinner
        gender = (Spinner) findViewById(R.id.reg_gender);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, gender_choices);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gender.setAdapter(adapter);

        // Working on Year of Study Spinner
        year = (Spinner) findViewById(R.id.reg_year);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, year_choices);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        year.setAdapter(adapter2);

        // Getting city details
        setCities();

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

    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
        if (parent.getId()==R.id.reg_city) {
            int city_number = (int)parent.getSelectedItemId();
            setColleges(cities[city_number].getId());
        }
    }

    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void setCities() {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(API_URL)
                .build();
        MoodIndigoClient methods = restAdapter.create(MoodIndigoClient.class);
        Callback callback = new Callback() {
            @Override
            public void success(Object o, Response response) {
                CityList citylist = (CityList)o;
                cities = citylist.getCityList();
            }
            @Override
            public void failure(RetrofitError retrofitError) {
                String error = retrofitError.getMessage();
                Toast.makeText(getBaseContext(),error,Toast.LENGTH_SHORT).show();
            }
        };
        methods.getCities(callback);
    }

    public void setColleges(int city_id) {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(API_URL)
                .build();
        MoodIndigoClient methods = restAdapter.create(MoodIndigoClient.class);
        Callback callback = new Callback() {
            @Override
            public void success(Object o, Response response) {
                //CityList citylist = (CityList)o;
                //colleges = citylist.getCityList();
            }
            @Override
            public void failure(RetrofitError retrofitError) {
                String error = retrofitError.getMessage();
                Toast.makeText(getBaseContext(),error,Toast.LENGTH_SHORT).show();
            }
        };
        methods.getColleges(""+city_id,callback);
    }

}
