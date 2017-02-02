package com.mahediapps.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mahediapps.voucherrecording.R;

import java.util.ArrayList;

public class AdapterForEachVoucherCategory extends BaseAdapter {

    private Context context;
    private ArrayList<String> voucherNote;
    private ArrayList<String> voucherAmount;
    private ArrayList<String> voucherDate;
    private ArrayList<String> voucherId;

    public AdapterForEachVoucherCategory(Context context,
                                         ArrayList<String> voucherNote,
                                         ArrayList<String> voucherAmount,
                                         ArrayList<String> voucherDate,
                                         ArrayList<String> voucherId) {

        this.context = context;
        this.voucherNote = voucherNote;
        this.voucherAmount = voucherAmount;
        this.voucherDate = voucherDate;
        this.voucherId = voucherId;
    }

    public int getCount()
    {
        return voucherId.size();
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
            row = layoutinflater.inflate(R.layout.li_each_voucher_category, null);
            holder = new ViewHolder(row);
            row.setTag(holder);

        }else{
            holder = (ViewHolder) row.getTag();
        }

        (holder.tvSerial).setText((new StringBuilder(String.valueOf(i + 1))).toString());
        (holder.tvNote).setText(voucherNote.get(i));
        (holder.tvId).setText(voucherId.get(i));
        (holder.tvAmount).setText(voucherAmount.get(i));
        (holder.tvDate).setText(voucherDate.get(i));

        return row;
    }


    private class ViewHolder {

        public TextView tvId, tvSerial, tvNote, tvAmount, tvDate;

        public ViewHolder(View view) {
            tvId = (TextView) view.findViewById(R.id.li_tvIdVoucherEVC);
            tvSerial = (TextView) view.findViewById(R.id.li_tvSerialNoVoucherEVC);
            tvNote = (TextView) view.findViewById(R.id.li_tvNoteVoucherEVC);
            tvAmount = (TextView) view.findViewById(R.id.li_tvAmountVoucherEVC);
            tvDate = (TextView) view.findViewById(R.id.li_tvDateEVC);
        }
    }

}
