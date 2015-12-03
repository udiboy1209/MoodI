package org.iitb.moodi.ui.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.iitb.moodi.MoodIndigoClient;
import org.iitb.moodi.R;
import org.iitb.moodi.api.CityResponse;
import org.iitb.moodi.api.CollegeResponse;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class RegistrationActivity extends BaseActivity implements AdapterView.OnItemSelectedListener {

    CityResponse.City[] mCityList;
    CollegeResponse.College[] mCollegeList;
    Spinner genderSpinner, citySpinner, collegeSpinner, yearSpinner;

    private static final String TAG = "RegistrationActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        mToolbar=(Toolbar) findViewById(R.id.toolbar);

        Bundle extras = getIntent().getExtras();
        if (extras!=null) {
            setEditText(extras.getString("name"),R.id.reg_name);
            setEditText(extras.getString("email"),R.id.reg_email);
        }
        // Working on Gender Spinner
        String[] gender_choices = new String[] {"gender","male","female"};
        genderSpinner = (Spinner) findViewById(R.id.reg_gender);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, gender_choices);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(adapter);

        // Working on Year of Study Spinner
        String[] year_choices = new String[] {"First","Second","Third","Fourth","Fifth"};
        yearSpinner = (Spinner) findViewById(R.id.reg_year);
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, year_choices);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearSpinner.setAdapter(adapter);

        // Getting city details
        citySpinner = (Spinner) findViewById(R.id.reg_city);
        adapter = new ArrayAdapter<String>(getBaseContext(),
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        citySpinner.setAdapter(adapter);
        adapter.add("Fetching cities...");
        citySpinner.setOnItemSelectedListener(this);

        // Getting college choices
        collegeSpinner = (Spinner) findViewById(R.id.reg_college);
        adapter = new ArrayAdapter<String>(getBaseContext(),
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter.add("Please select your city");
        collegeSpinner.setAdapter(adapter);
        collegeSpinner.setEnabled(false);

        setCities();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mToolbar.setTitle("Register");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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
        int[] checksArray = {R.id.reg_name, R.id.reg_email, R.id.reg_dob, R.id.reg_phone};
        // Checking whether edit texts are empty
        for(int id : checksArray) {
            if (getEditText(id).equals("")) {
                Log.d(TAG,"Empty text field error");
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

            String dob = ((EditText)findViewById(R.id.reg_dob)).getText().toString();
            String dobregex = "^(?:(?:31(\\/|-|\\.)(?:0?[13578]|1[02]))\\1|(?:(?:29|30)(\\/|-|\\.)(?:0?[1,3-9]|1[0-2])\\2))(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$|^(?:29(\\/|-|\\.)0?2\\3(?:(?:(?:1[6-9]|[2-9]\\d)?(?:0[48]|[2468][048]|[13579][26])|(?:(?:16|[2468][048]|[3579][26])00))))$|^(?:0?[1-9]|1\\d|2[0-8])(\\/|-|\\.)(?:(?:0?[1-9])|(?:1[0-2]))\\4(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$";
            if(!dob.matches(dobregex)) {
                ((EditText)findViewById(R.id.reg_dob)).setError("Invalid Date of Birth, enter DD-MM-YYYY");
                check= false;
            }

            ArrayAdapter cityAdapter = (ArrayAdapter) citySpinner.getAdapter();
            ArrayAdapter collegeAdapter = (ArrayAdapter) collegeSpinner.getAdapter();
            if (cityAdapter.getCount()==1) {
                Toast.makeText(getBaseContext(),R.string.city_spinner_error,Toast.LENGTH_LONG).show();
                check=false;
            }
            else if (citySpinner.getSelectedItemId()!=0 && collegeAdapter.getCount()==1) {
                Toast.makeText(getBaseContext(), R.string.college_spinner_error, Toast.LENGTH_LONG).show();
                check = false;
            }

        }
        return check;
    }

    public void saveData(View view) {
        if (!validateData())
            return;
        Bundle extras = getIntent().getExtras();
        me.fbid=extras.getString("fbid");
        me.name=getEditText(R.id.reg_name);
        me.email=getEditText(R.id.reg_email);
        me.dob=(getEditText(R.id.reg_dob)).replace('/','-').replace('.','-');
        me.phone=getEditText(R.id.reg_phone);
        me.gender=(String)((Spinner)findViewById(R.id.reg_gender)).getSelectedItem();
        me.year_study = (String)((Spinner)findViewById(R.id.reg_year)).getSelectedItem();
        int city_number = (int)((Spinner)findViewById(R.id.reg_city)).getSelectedItemId();
        me.city_id = mCityList[city_number].getId();
        int college_number = (int)((Spinner)findViewById(R.id.reg_college)).getSelectedItemId();
        me.clg_id = mCollegeList[college_number].getId();


        SharedPreferences.Editor spe = prefs.edit();

        spe.putString("user_json", me.getJSON());
        Log.d(TAG,me.getJSON());
        spe.commit();

        setResult(RESULT_OK);
        finish();
    }

    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
        int city_number = (int)parent.getSelectedItemId();
        if (citySpinner.getCount()!=1) {
            ArrayAdapter collegeAdapter = (ArrayAdapter) collegeSpinner.getAdapter();
            collegeAdapter.clear();
            collegeAdapter.add("Fetching colleges...");
            citySpinner.setEnabled(false);
            setColleges(mCityList[city_number].getId());
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
                CityResponse c = (CityResponse)o;
                mCityList = c.getCityList();
                ArrayAdapter cityAdapter = (ArrayAdapter) citySpinner.getAdapter();
                cityAdapter.clear();
                for (CityResponse.City city_obj : mCityList) {
                    cityAdapter.add(city_obj.getName());
                }
                collegeSpinner.setEnabled(true);
            }
            @Override
            public void failure(RetrofitError retrofitError) {
                String error = retrofitError.getMessage();
                Log.e(TAG, error);
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
                CollegeResponse c = (CollegeResponse)o;
                mCollegeList = c.getCollegeList();
                ArrayAdapter collegeAdapter = (ArrayAdapter) collegeSpinner.getAdapter();
                collegeAdapter.clear();
                for (CollegeResponse.College college_obj : mCollegeList) {
                    collegeAdapter.add(college_obj.getName());
                }
                citySpinner.setEnabled(true);
            }
            @Override
            public void failure(RetrofitError retrofitError) {
                String error = retrofitError.getMessage();
                Log.e(TAG,error);
            }
        };
        methods.getColleges(""+city_id,callback);
    }

}
