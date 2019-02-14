package pudpongsai.thanaporn.th.ac.su.reg.pregnant.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import pudpongsai.thanaporn.th.ac.su.reg.pregnant.R;

/**
 * Created by younyuan on 07-Feb-19.
 */

public class weekAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<String> mData;
    private LayoutInflater mInflater;

    public weekAdapter(Context context, ArrayList<String> data) {
        mInflater = LayoutInflater.from(context);
        mContext = context;
        mData = data;
    }
    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.spinner_text_item, parent, false);
            holder.txtWeek = (TextView) convertView.findViewById(R.id.txtWeek);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txtWeek.setText(mData.get(position));

        convertView.setTag(holder);

        return convertView;
    }

    public class ViewHolder {
        TextView txtWeek;
    }
}
