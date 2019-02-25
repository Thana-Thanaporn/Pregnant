package pudpongsai.thanaporn.th.ac.su.reg.pregnant.Adapters;


import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import pudpongsai.thanaporn.th.ac.su.reg.pregnant.CalendarMenuActivity.EventActivity;
import pudpongsai.thanaporn.th.ac.su.reg.pregnant.Details.EventDetail;
import pudpongsai.thanaporn.th.ac.su.reg.pregnant.Details.UserDetail;
import pudpongsai.thanaporn.th.ac.su.reg.pregnant.PregnantUitli;
import pudpongsai.thanaporn.th.ac.su.reg.pregnant.R;

public class EventAdapter extends BaseAdapter {

    private ArrayList<EventDetail> list;
    private LayoutInflater mLayoutInflater;
    private LinearLayout layout;
    private Long day;
    private Context mContext;

    public EventAdapter(ArrayList<EventDetail> list, Context c,LinearLayout layout , long l) {
        this.list = list;
        this.mContext = c;
        this.layout = layout;
        this.day =l;
        mLayoutInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {


        View view = mLayoutInflater.inflate(R.layout.list_item_event,parent,false);
        final Holder h = new Holder();

        // set id's
        h.txtTime = (Button)(view.findViewById(R.id.txtTime));
        h.txtPlace = (TextView)(view.findViewById(R.id.txtPlace));
        h.txtTopic = (TextView)(view.findViewById(R.id.txtTopic));
        h.txtDetail = (TextView)(view.findViewById(R.id.txtDetail));
        h.btnOptionEvent = (ImageButton)(view.findViewById(R.id.btnOptionEvent));

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(list.get(position).getDate());


        SimpleDateFormat formatTime = new SimpleDateFormat("HH : mm");

        h.txtTopic.setText(list.get(position).getTopic ());
        h.txtPlace.setText(list.get(position).getPlace ());
        h.txtDetail.setText(list.get(position).getDetail ());
        h.txtTime.setText(formatTime.format(calendar.getTime())+" น." );

        h.btnOptionEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PregnantUitli.popupEditDelEvent(layout,mContext,list.get(position),day);

            }
        });

        return view;
    }


    private class Holder
    {
        Button txtTime;
        TextView txtPlace;
        TextView txtTopic;
        TextView txtDetail;
        ImageButton btnOptionEvent;

    }
}