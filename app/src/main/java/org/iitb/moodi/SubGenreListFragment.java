package org.iitb.moodi;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;


public class SubGenreListFragment extends Fragment {

    private static final String ARG_GENRE_NAME = "genre_name";

    private MainActivity mAttachedActivity;

    private ArrayAdapter<String> mAdapter;

    public static SubGenreListFragment newInstance(String genre_name) {
        SubGenreListFragment fragment = new SubGenreListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_GENRE_NAME, genre_name);
        fragment.setArguments(args);
        return fragment;
    }

    public SubGenreListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter=new ArrayAdapter<>(mAttachedActivity, R.layout.text_list_item);

        if (getArguments() != null) {
            loadData(getArguments().getString(ARG_GENRE_NAME));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ListView view = (ListView) inflater.inflate(R.layout.fragment_sub_genre_list, container, false);

        view.setAdapter(mAdapter);
        return view;
    }

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        mAttachedActivity = (MainActivity) activity;
    }

    private void loadData(String genre_name){
        String filename = genre_name+".json";
        InputStream data_input=null;

        try {
            // Try to open from cache
            data_input=mAttachedActivity.openFileInput(filename);
        } catch (FileNotFoundException fnfe) {
            // Not found in cache, open from app assets
            try {
                data_input=mAttachedActivity.getAssets().open(filename);
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }

        if(data_input!=null){
            try {
                JSONArray list = new JSONArray(Util.buildStringFromIS(data_input));
                for(int i=0; i<list.length(); i++){
                    JSONObject elem = list.getJSONObject(i);

                    mAdapter.add(elem.optString("name"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if(mAdapter.getCount()==0)
            Toast.makeText(mAttachedActivity, "Error while loading data", Toast.LENGTH_LONG).show();
    }
}
