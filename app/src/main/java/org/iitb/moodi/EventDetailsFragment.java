package org.iitb.moodi;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class EventDetailsFragment extends Fragment {

    private FragmentTabHost tabHost;
    public static EventDetailsFragment newInstance() {
        EventDetailsFragment fragment = new EventDetailsFragment();

        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_event_details, container, false);

        tabHost = (FragmentTabHost) v.findViewById(android.R.id.tabhost);
        tabHost.setup(getActivity(), getChildFragmentManager(), android.R.id.tabcontent);
        Bundle arg1 = new Bundle();
        arg1.putInt("Arg for Frag1", 1);
        tabHost.addTab(tabHost.newTabSpec("Tab1").setIndicator("About"),
                EventDetailsHelperFragment.class, arg1);
        Bundle arg2 = new Bundle();
        arg2.putInt("Arg for Frag2", 2);
        tabHost.addTab(tabHost.newTabSpec("Tab2").setIndicator("Rules"),
                EventDetailsHelperFragment.class, arg2);
        Bundle arg3 = new Bundle();
        arg3.putInt("Arg for Frag3", 3);
        tabHost.addTab(tabHost.newTabSpec("Tab3").setIndicator("Prizes"),
                EventDetailsHelperFragment.class, arg2);
        Bundle arg4 = new Bundle();
        arg4.putInt("Arg for Frag4", 3);
        tabHost.addTab(tabHost.newTabSpec("Tab4").setIndicator("Register"),
                EventDetailsHelperFragment.class, arg2);

        return tabHost;
    }
}
