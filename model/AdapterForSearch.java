package com.mahediapps.model;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mahediapps.voucherrecording.R;


public class AdapterForSearch extends BaseAdapter {

    Context context;
    Typeface myTypeface;
    String searchArray[];

    public AdapterForSearch(Context context, String as[]) {
        this.context = context;
        searchArray = as;
        myTypeface = Typeface.createFromAsset(context.getAssets(), "fonts/CARDIF__.TTF");
    }

    public int getCount() {
        return searchArray.length;
    }

    public Object getItem(int i) {
        return null;
    }

    public long getItemId(int i) {
        return 0L;
    }

    private class MyViewHolder {
        TextView tv;

        MyViewHolder(View view) {
            tv = (TextView) view.findViewById(R.id.tv_list_item_search);
        }
    }

    public View getView(int i, View convertView, ViewGroup viewgroup) {
        View row = convertView;
        MyViewHolder holder = null;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.li_search, null);
            holder = new MyViewHolder(row);
            row.setTag(holder);

        } else {
            holder = (MyViewHolder) row.getTag();
        }

        holder.tv.setTypeface(myTypeface);
        holder.tv.setText(searchArray[i]);
        return row;
    }
}
