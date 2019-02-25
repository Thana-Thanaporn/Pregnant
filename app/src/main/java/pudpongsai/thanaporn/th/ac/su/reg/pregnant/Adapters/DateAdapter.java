package pudpongsai.thanaporn.th.ac.su.reg.pregnant.Adapters;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import pudpongsai.thanaporn.th.ac.su.reg.pregnant.Details.TelDetail;
import pudpongsai.thanaporn.th.ac.su.reg.pregnant.Details.UserDetail;
import pudpongsai.thanaporn.th.ac.su.reg.pregnant.PregnantUitli;
import pudpongsai.thanaporn.th.ac.su.reg.pregnant.R;

public class DateAdapter extends BaseAdapter {

    private ArrayList<EventDetail> list;
    private LayoutInflater mLayoutInflater;
    private Context mContext;
    private Long day;

    public DateAdapter(ArrayList<EventDetail> list, Context c ,Long l) {
        this.list = list;
        this.mContext = c;
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


        View view = mLayoutInflater.inflate(R.layout.list_item_date,parent,false);
        final Holder h = new Holder();

        // set id's
        h.txtTime = (Button)(view.findViewById(R.id.txtTime));
        h.txtDate = (TextView)(view.findViewById(R.id.txtDate));
        h.txtTopic = (TextView)(view.findViewById(R.id.txtTopic));
        h.boxDate = (LinearLayout)(view.findViewById(R.id.boxDate));

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(list.get(position).getDate());
        Log.d("date list", String.valueOf(calendar.getTimeInMillis()));
        SimpleDateFormat formatDay = new SimpleDateFormat("dd MMMM",new Locale("th","TH"));
        SimpleDateFormat formatTime = new SimpleDateFormat("HH : mm");
        h.txtTopic.setText(list.get(position).getTopic ());
        h.txtDate.setText(formatDay.format(calendar.getTime())+" " + (calendar.get(Calendar.YEAR)+543));
        h.txtTime.setText(formatTime.format(calendar.getTime())+" น." );

        h.boxDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(mContext, EventActivity.class);
                Bundle mBundle = new Bundle();
                mBundle.putLong("Date", day);
                mIntent.putExtras(mBundle);

                mContext.startActivity(mIntent);

            }
        });

        return view;
    }


    private class Holder
    {
        Button txtTime;
        TextView txtDate;
        TextView txtTopic;
        LinearLayout boxDate;

    }
}