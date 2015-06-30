package org.iitb.moodi;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;

import org.iitb.moodi.api.Genre;
import org.iitb.moodi.ui.GenreCard;

import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.List;

public class GenreListFragment extends Fragment implements View.OnClickListener{
    private MainActivity mAttachedActivity;

    CardAdapter mAdapter;

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
        GridView view = (GridView) inflater.inflate(R.layout.fragment_genre_list, container, false);

        view.setAdapter(mAdapter);

        Log.d("GenreList", "onCreateView");
        return view;
    }

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        mAttachedActivity=(MainActivity)activity;
        mAttachedActivity.onSectionAttached(1);

        mAdapter = new CardAdapter(mAttachedActivity);

        mAdapter.add(new Genre("Arts & Ideas", 0xFFFF0000));
        mAdapter.add(new Genre("Competitions", 0xFF00FF00));
        mAdapter.add(new Genre("Informals", 0xFF0000FF));
        mAdapter.add(new Genre("Concerts", 0xFFF0F000));
        mAdapter.add(new Genre("Proshows", 0xAABADAFF));
    }

    @Override
    public void onClick(View v) {
        mAttachedActivity.loadSubGenreList(((TextView)v).getText().toString());
    }

    class CardAdapter extends ArrayAdapter<Genre> {
        public CardAdapter(Context context) {
            super(context, android.R.layout.simple_list_item_1);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            GenreCard card = new GenreCard(mAttachedActivity);


            card.setTitle(getItem(position).title);
            card.setColor(getItem(position).color);


            Log.d("GenreList", "getView : "+getItem(position).title);

            return card;
        }
    }
}
