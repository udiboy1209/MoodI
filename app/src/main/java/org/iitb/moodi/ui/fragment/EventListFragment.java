package org.iitb.moodi.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import org.iitb.moodi.R;

/**
 * Created by udiboy on 23/10/15.
 */
public class EventListFragment extends BaseFragment {
    TabLayout mTabLayout;
    ViewPager mViewPager;

    public static EventListFragment newInstance() {
        
        Bundle args = new Bundle();
        
        EventListFragment fragment = new EventListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_event_list, container, false);
        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Get the ViewPager and set it's PagerAdapter so that it can display items
        mViewPager = (ViewPager) view.findViewById(R.id.viewpager);
        mViewPager.setAdapter(new SamplePagerAdapter());
        // Give the SlidingTabLayout the ViewPager, this must be done AFTER the ViewPager has had
        // it's PagerAdapter set.
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        View v = mActivity.getLayoutInflater().inflate(R.layout.widget_tab_view,mActivity.toolbarContainer, false);
        mTabLayout=(TabLayout)v.findViewById(R.id.widget_tab_layout);
        mTabLayout.setupWithViewPager(mViewPager);

        mActivity.customizeToolbar(R.drawable.splash_bg, "Event List", v);
        super.onActivityCreated(savedInstanceState);
    }

    class SamplePagerAdapter extends PagerAdapter {
        public static final String TAG = "EventListPagerAdapter";

        /**
         * @return the number of pages to display
         */
        @Override
        public int getCount() {
            return 3;
        }

        /**
         * @return true if the value returned from {@link #instantiateItem(ViewGroup, int)} is the
         * same object as the {@link View} added to the {@link ViewPager}.
         */
        @Override
        public boolean isViewFromObject(View view, Object o) {
            return o == view;
        }

        /**
         * Return the title of the item at {@code position}. This is important as what this method
         * returns is what is displayed in the {@link TabLayout}.
         * <p>
         * Here we construct one using the position value, but for real application the title should
         * refer to the item's contents.
         */
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Dance";
                case 1:
                    return "Art";
                case 2:
                    return "Theatre";
                default:
                    return "Genre";
            }
        }

        /**
         * Instantiate the {@link View} which should be displayed at {@code position}. Here we
         * inflate a layout from the apps resources and then change the text view to signify the position.
         */
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            // Inflate a new layout from our resources
            ListView view = new ListView(container.getContext());
            // Add the newly created View to the ViewPager
            container.addView(view);

            Log.i(TAG, "instantiateItem() [position: " + position + "]");

            // Return the View
            return view;
        }

        /**
         * Destroy the item from the {@link ViewPager}. In our case this is simply removing the
         * {@link View}.
         */
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
            Log.i(TAG, "destroyItem() [position: " + position + "]");
        }
    }
}
