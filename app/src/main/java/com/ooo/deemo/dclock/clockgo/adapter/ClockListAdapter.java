package com.ooo.deemo.dclock.clockgo.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.ooo.deemo.dclock.R;

import java.util.List;
import java.util.Map;

/**
 * Author by Deemo, Date on 2019/7/8.
 * Have a good day
 */
public class ClockListAdapter extends SimpleAdapter {


    /**
     * Constructor
     *
     * @param context  The context where the View associated with this SimpleAdapter is running
     * @param data     A List of Maps. Each entry in the List corresponds to one row in the list. The
     *                 Maps contain the data for each row, and should include all the entries specified in
     *                 "from"
     * @param resource Resource identifier of a view layout that defines the views for this list
     *                 item. The layout file should include at least those named views defined in "to"
     * @param from     A list of column names that will be added to the Map associated with each
     *                 item.
     * @param to       The views that should display column in the "from" parameter. These should all be
     *                 TextViews. The first N views in this list are given the values of the first N columns
     */
    public ClockListAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
        super(context, data, resource, from, to);

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {


        View v = super.getView(position, convertView, parent);

        TextView tv_clocktime = v.findViewById(R.id.tv_clocktime);
        TextView tv_clockstatus = v.findViewById(R.id.tv_clockstatus);



        return v;
    }
}
