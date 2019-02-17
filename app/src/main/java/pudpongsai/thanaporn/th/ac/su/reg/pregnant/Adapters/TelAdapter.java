package pudpongsai.thanaporn.th.ac.su.reg.pregnant.Adapters;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import pudpongsai.thanaporn.th.ac.su.reg.pregnant.Details.TelDetail;
import pudpongsai.thanaporn.th.ac.su.reg.pregnant.PregnantUitli;
import pudpongsai.thanaporn.th.ac.su.reg.pregnant.R;

public class TelAdapter extends BaseAdapter {

    private ArrayList<TelDetail> list;
    private LayoutInflater mLayoutInflater;
    private Context mContext;
    private LinearLayout layout;

    public TelAdapter(ArrayList<TelDetail> list, Context c , LinearLayout layout) {
        this.list = list;
        this.mContext = c;
        this.layout = layout;
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


        View view = mLayoutInflater.inflate(R.layout.list_item_tel,parent,false);
        final Holder h = new Holder();

        // set id's
        h.txtNametel = (TextView)(view.findViewById(R.id.txtNametel));

        h.txtNametel.setText(list.get(position).getNameTel ());

        h.txtTel = (TextView)(view.findViewById(R.id.txtTel));
        h.txtTel.setText(list.get(position).getTel ());

        h.btnOptionTel = (ImageButton)(view.findViewById(R.id.btnOptionTel));
        h.btnOptionTel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PregnantUitli pregnantUitli = new PregnantUitli();
                pregnantUitli.popupEditDelTel(layout, mContext, list.get(position));
            }
        });

        return view;
    }


    private class Holder
    {
        TextView txtNametel;
        TextView txtTel;
        ImageButton btnOptionTel;

    }
}