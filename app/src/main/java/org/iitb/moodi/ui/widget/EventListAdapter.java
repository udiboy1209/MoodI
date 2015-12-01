package org.iitb.moodi.ui.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import net.steamcrafted.materialiconlib.MaterialDrawableBuilder;
import net.steamcrafted.materialiconlib.MaterialIconView;

import org.iitb.moodi.BaseApplication;
import org.iitb.moodi.R;
import org.iitb.moodi.api.Event;
import org.iitb.moodi.ui.activity.BaseActivity;

import java.util.Objects;

/**
 * Created by udiboy on 26/10/15.
 */
public class EventListAdapter extends ArrayAdapter<Event> implements View.OnClickListener{
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


        TextView index = (TextView) v.findViewById(R.id.event_list_item_index);
        if(index!=null) {
            index.setText(String.format("%02d",(position + 1)));
            if (e.genrebaap.equals("competitions")) {
                index.setBackgroundResource(R.drawable.list_icon_compi);
            } else if (e.genrebaap.equals("informals")) {
                index.setBackgroundResource(R.drawable.list_icon_informals);
            } else if (e.genrebaap.equals("concerts")) {
                index.setBackgroundResource(R.drawable.list_icon_concerts);
            } else if (e.genrebaap.equals("arts")) {
                index.setBackgroundResource(R.drawable.list_icon_arts);
            } else if (e.genrebaap.equals("proshows")) {
                index.setBackgroundResource(R.drawable.list_icon_proshows);
            }
        }
        //if(venue != null) venue.setText(e.venue);

        MaterialIconView fav = (MaterialIconView) v.findViewById(R.id.event_list_item_favourite);
        if(fav != null) {
            fav.setOnClickListener(this);
            fav.setTag(position);
            if (e.fav) {
                fav.setIcon(MaterialDrawableBuilder.IconValue.STAR);
                fav.setColorResource(R.color.fav_true);
            } else {
                fav.setIcon(MaterialDrawableBuilder.IconValue.STAR_OUTLINE);
                fav.setColorResource(R.color.fav_false);
            }
        }

        //Date time = new Date(e.time);

        //TextView time_hrs = (TextView) v.findViewById(R.id.event_list_item_time_hrs);
        //if(time_hrs != null) time_hrs.setText(""+time.getHours());

        return v;
    }



    @Override
    public boolean isEnabled(int position) {
        return false;
    }

    @Override
    public void onClick(View v) {
        MaterialIconView fav = (MaterialIconView) v;
        Event e = getItem((Integer) v.getTag());
        e.fav = !e.fav;
        if (e.fav) {
            fav.setIcon(MaterialDrawableBuilder.IconValue.STAR);
            fav.setColorResource(R.color.fav_true);
            BaseApplication.getDB().addEvent(e);
        } else {
            fav.setIcon(MaterialDrawableBuilder.IconValue.STAR_OUTLINE);
            fav.setColorResource(R.color.fav_false);
            BaseApplication.getDB().removeEvent(e);
        }
    }
}
