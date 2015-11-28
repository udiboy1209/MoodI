package org.iitb.moodi.ui.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.iitb.moodi.R;
import org.iitb.moodi.api.Event;

/**
 * Created by udiboy on 26/10/15.
 */
public class EventListAdapter extends ArrayAdapter<Event>{
    private int mLayoutID;
    private View.OnClickListener mOCL;

    public EventListAdapter(Context context, int resource) {
        super(context, resource);

        mLayoutID = resource;
        mOCL=null;
    }

    public EventListAdapter(Context context, int resource, View.OnClickListener ocl) {
        super(context, resource);

        mLayoutID = resource;
        mOCL=ocl;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Event e = getItem(position);
        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(mLayoutID, null);
        }
        v.setTag(position);

        if(mOCL != null)
            v.setOnClickListener(mOCL);

        TextView name = (TextView) v.findViewById(R.id.event_list_item_name);
        if(name != null) name.setText(e.name);

        TextView description = (TextView) v.findViewById(R.id.event_list_item_description);
        if(description != null) description.setText(e.intro_short);


        TextView venue = (TextView) v.findViewById(R.id.event_list_item_venue);
        //if(venue != null) venue.setText(e.venue);

        //Date time = new Date(e.time);

        //TextView time_hrs = (TextView) v.findViewById(R.id.event_list_item_time_hrs);
        //if(time_hrs != null) time_hrs.setText(""+time.getHours());

        return v;
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }
}
