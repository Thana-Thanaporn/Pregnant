package pudpongsai.thanaporn.th.ac.su.reg.pregnant.Adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;

import pudpongsai.thanaporn.th.ac.su.reg.pregnant.Details.TotalWeekDetail;
import pudpongsai.thanaporn.th.ac.su.reg.pregnant.Details.UserDetail;
import pudpongsai.thanaporn.th.ac.su.reg.pregnant.ProfileMenuActivity.TotalNoteActivity;
import pudpongsai.thanaporn.th.ac.su.reg.pregnant.R;

public class TotalAdapter extends BaseAdapter {

    private ArrayList<TotalWeekDetail> list;
    private LayoutInflater mLayoutInflater;
    private Context mContext;

    public TotalAdapter(ArrayList<TotalWeekDetail> list, Context c ) {
        this.list = list;
        this.mContext = c;
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


        View view = mLayoutInflater.inflate(R.layout.list_item_total,parent,false);
        final Holder h = new Holder();

        // set id's
        h.txtWeek = (TextView)(view.findViewById(R.id.txtWeek));

        h.txtWeek.setText("สัปดาห์ที่ " + list.get(position).getWeek ());

        h.txtTotal = (TextView)(view.findViewById(R.id.txtTotal));
        h.txtTotal.setText("จำนวน " + list.get(position).getTotalNote () + " บันทึก");

        h.chkWeek = (CheckBox)(view.findViewById(R.id.chkWeek));

        h.chkWeek.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if(isChecked){
                    UserDetail.totalNote.get(position).setStatus(true);
                }
                if(!isChecked){
                    UserDetail.totalNote.get(position).setStatus(false);
                }
                TotalNoteActivity.countSelectNote();
            }
        });


        return view;
    }


    private class Holder
    {
        TextView txtWeek;
        TextView txtTotal;
        CheckBox chkWeek;

    }
}