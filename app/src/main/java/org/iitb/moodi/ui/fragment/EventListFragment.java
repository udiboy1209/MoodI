package org.iitb.moodi.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

/**
 * Created by udiboy on 23/10/15.
 */
public class EventListFragment extends BaseFragment {
    public static EventListFragment newInstance() {
        
        Bundle args = new Bundle();
        
        EventListFragment fragment = new EventListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
    }
}
