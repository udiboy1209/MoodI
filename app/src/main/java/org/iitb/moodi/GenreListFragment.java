package org.iitb.moodi;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.LineNumberReader;

public class GenreListFragment extends Fragment implements View.OnClickListener{
    private MainActivity mAttachedActivity;

    public static GenreListFragment newInstance() {
        GenreListFragment fragment = new GenreListFragment();

        return fragment;
    }

    public GenreListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        LinearLayout view = (LinearLayout) inflater.inflate(R.layout.fragment_genre_list, container, false);

        for(int i=0; i<view.getChildCount(); i++)
            view.getChildAt(i).setOnClickListener(this);

        return view;
    }

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        mAttachedActivity=(MainActivity)activity;
        mAttachedActivity.onSectionAttached(1);
    }

    @Override
    public void onClick(View v) {
        mAttachedActivity.loadSubGenreList(((TextView)v).getText().toString());
    }
}
