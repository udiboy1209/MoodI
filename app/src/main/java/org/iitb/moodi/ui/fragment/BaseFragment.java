package org.iitb.moodi.ui.fragment;

import android.app.Activity;
import android.support.v4.app.Fragment;

import org.iitb.moodi.R;
import org.iitb.moodi.ui.activity.MainActivity;

/**
 * Created by udiboy on 23/10/15.
 */
public class BaseFragment  extends Fragment{
    MainActivity mActivity;
    int mID =0;

    @Override
    public void onAttach(Activity activity) {
        if(activity instanceof MainActivity)
            mActivity=(MainActivity)activity;

        mActivity.onSectionAttached(mID);
        super.onAttach(activity);
    }
}
