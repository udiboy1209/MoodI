package org.iitb.moodi.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.LinearLayout;

import org.iitb.moodi.R;
import org.iitb.moodi.ui.activity.MainActivity;
import org.iitb.moodi.ui.widget.ToolbarWidgetLayout;

public class BaseFragment  extends Fragment{
    MainActivity mActivity;
    InteractionListener mInteractionListener;
    int mID =0;

    private String mTitle;
    private int mColorResource;
    private int mBackgroundResource;

    @Override
    public void onAttach(Activity activity) {
        if(activity instanceof MainActivity)
            mActivity=(MainActivity)activity;

        mActivity.onSectionAttached(mID);
        super.onAttach(activity);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(mInteractionListener!=null)
            mInteractionListener.onFragmentLoaded(this);
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public void setColor(int mColorResource) {
        this.mColorResource = mColorResource;
    }

    public void setBackground(int mBackgroundResource) {
        this.mBackgroundResource = mBackgroundResource;
    }

    public void customizeToolbarLayout(ToolbarWidgetLayout toolbarLayout){
        toolbarLayout.getToolbar().setTitle(mTitle);
    }

    public void setInteractionListener(InteractionListener interactionListener) {
        this.mInteractionListener = interactionListener;
    }

    public interface InteractionListener{
        public void onFragmentLoaded(BaseFragment baseFragment);
    }
}
