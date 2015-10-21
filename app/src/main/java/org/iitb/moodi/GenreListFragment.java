package org.iitb.moodi;

import android.app.Activity;
import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;

import org.iitb.moodi.api.Genre;
import org.iitb.moodi.ui.GenreCard;

import java.io.LineNumberReader;
import java.lang.reflect.GenericArrayType;
import java.util.ArrayList;
import java.util.List;

public class GenreListFragment extends Fragment implements View.OnClickListener{
    private MainActivity mAttachedActivity;

    CardAdapter mAdapter;
    RecyclerView rvList;
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
        View view = inflater.inflate(R.layout.fragment_genre_list, container, false);
        rvList = (RecyclerView) view.findViewById(R.id.rv_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rvList.setLayoutManager(layoutManager);
        List<Genre> items = new ArrayList<>();

        items.add(new Genre("Arts & Ideas", 0xFFFF0000));
        items.add(new Genre("Competitions", 0xFF00FF00));
        items.add(new Genre("Informals", 0xFF0000FF));
        items.add(new Genre("Concerts", 0xFFF0F000));
        items.add(new Genre("Proshows", 0xAABADAFF));

        mAdapter = new CardAdapter(items);
        rvList.setAdapter(mAdapter);
        Log.d("GenreList", "onCreateView");
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

    class CardAdapter extends RecyclerView.Adapter<CardAdapter.ItemHolder> {
        List<Genre> mItems = new ArrayList<>();
        
        public CardAdapter(List<Genre> items) {
            this.mItems = items;
        }

        public Genre getItem(int position) {
            return mItems.get(position);
        }
        
        @Override
        public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.view_genre_card, parent, false);
            return new ItemHolder(view);
        }

        @Override
        public void onBindViewHolder(ItemHolder holder, int position) {
            Genre genre = getItem(position);
            holder.tvTitle.setText(genre.title);
            holder.ivImage.setColorFilter(genre.color, PorterDuff.Mode.MULTIPLY);
        }

        @Override
        public int getItemCount() {
            return mItems.size();
        }

        public void add(Genre genre) {
            mItems.add(genre);
        }
        
        class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
            ImageView ivImage;
            TextView tvTitle;
            
            public ItemHolder(View itemView) {
                super(itemView);
                ivImage = (ImageView) itemView.findViewById(R.id.genre_card_color);
                tvTitle = (TextView) itemView.findViewById(R.id.genre_card_title);
                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {
                //TODO
            }
        }
    }
}
