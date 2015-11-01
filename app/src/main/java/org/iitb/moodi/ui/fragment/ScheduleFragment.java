package org.iitb.moodi.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.iitb.moodi.R;

/**
 * Created by udiboy on 22/10/15.
 */
public class ScheduleFragment extends BaseFragment {
    public static ScheduleFragment newInstance() {
        Bundle args = new Bundle();

        ScheduleFragment fragment = new ScheduleFragment();
        fragment.mID = R.layout.fragment_schedule;
        fragment.setArguments(args);
        fragment.setTitle("Schedule");
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_schedule, container, false);
        return v;
    }
}
