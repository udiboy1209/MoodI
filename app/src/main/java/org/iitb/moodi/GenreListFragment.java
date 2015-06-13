package org.iitb.moodi;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class GenreListFragment extends Fragment {

    public static GenreListFragment newInstance() {
        GenreListFragment fragment = new GenreListFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
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
        return inflater.inflate(R.layout.fragment_genre_list, container, false);
    }

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        ((MainActivity)activity).onSectionAttached(1);
    }
}
