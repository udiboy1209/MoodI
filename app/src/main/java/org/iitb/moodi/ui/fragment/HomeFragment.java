package org.iitb.moodi.ui.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.iitb.moodi.R;
import org.iitb.moodi.ui.activity.MainActivity;

/**
 * Created by udiboy on 22/10/15.
 */
public class HomeFragment extends Fragment {
    MainActivity mActivity;

    public static HomeFragment newInstance() {
        Bundle args = new Bundle();

        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main, container, false);
        return v;
    }

    @Override
    public void onAttach(Activity activity) {
        if(activity instanceof MainActivity)
            mActivity=(MainActivity)activity;

        mActivity.onSectionAttached(R.layout.fragment_main);
        super.onAttach(activity);
    }
}
