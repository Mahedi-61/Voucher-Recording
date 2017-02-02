package com.mahediapps.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mahediapps.voucherrecording.R;

import java.util.ArrayList;

public class AdapterForClassifiedExpense extends BaseAdapter {

    private Context context;
    private ArrayList<String> voucherAmount;
    private ArrayList<String> voucherCategory;

    public AdapterForClassifiedExpense(Context context,
                                       ArrayList<String> voucherAmount,
                                       ArrayList<String> voucherCategory) {

        this.context = context;
        this.voucherAmount = voucherAmount;
        this.voucherCategory = voucherCategory;
    }

    public int getCount()
    {
        return voucherCategory.size();
    }


    public Object getItem(int i)
    {
        return null;
    }


    public long getItemId(int i)
    {
        return 0L;
    }


    public View getView(int i, View view, ViewGroup viewgroup) {

        LayoutInflater layoutinflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View row = view;
        ViewHolder holder;

        if (row == null) {
            row = layoutinflater.inflate(R.layout.li_classified_expense, null);
            holder = new ViewHolder(row);
            row.setTag(holder);

        }else{
            holder = (ViewHolder) row.getTag();
        }

        (holder.tvAmount).setText(voucherAmount.get(i));
        (holder.tvCategory).setText(voucherCategory.get(i));

        return row;
    }


    private class ViewHolder {

        public TextView tvCategory, tvAmount;

        public ViewHolder(View view) {
            tvAmount = (TextView) view.findViewById(R.id.li_tvAmountCE);
            tvCategory = (TextView) view.findViewById(R.id.li_tvVoucherCategoryCE);
        }
    }

}
