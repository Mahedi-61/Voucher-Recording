package com.mahediapps.voucherrecording;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mahediapps.model.AllData;
import com.mahediapps.model.DatabaseHelper;


public class AddExpenseActivity extends ActionBarActivity {

    private Toolbar mToolbar;
    private Button bSaveAE;
    private TextView tvCategoryAE, tvAmountAE, tvNoteAE;
    private static EditText etDateAE, etCategoryAE, etAmountAE, etNoteAE;
    private static int nDay, nMonth, nYear;
    private static String dayId, monthId, sTime, sDate;
    private DatabaseHelper dbHelper;


    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_add_expense);
        mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        mToolbar.setTitle("Add New Voucher");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initialize();

        etDateAE.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                showDatePickerDialog();
            }
        });


        etCategoryAE.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                final ArrayList<String> alCategory = dbHelper.getAllContentFromTable3();

                final Dialog dialog = new Dialog(AddExpenseActivity.this);
                dialog.setContentView(R.layout.dl_category_list);
                dialog.setTitle("Select a Category");
                ListView lvCategoryList = (ListView) dialog.findViewById(R.id.lvCategoryListAEA);
                lvCategoryList.setAdapter(new ArrayAdapter<>(AddExpenseActivity.this, android.R.layout.simple_list_item_1, alCategory));
                dialog.show();

                lvCategoryList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        etCategoryAE.setText(alCategory.get(position));
                        dialog.dismiss();
                    }
                });
            }
        });//end of onClickListener method


        bSaveAE.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                if (etCategoryAE.getText().toString().equals("")) {
                    Toast.makeText(AddExpenseActivity.this, "Please enter a voucher category", Toast.LENGTH_SHORT).show();

                } else if (etNoteAE.getText().toString().equals("")) {
                    Toast.makeText(AddExpenseActivity.this, "Please enter a voucher note", Toast.LENGTH_SHORT).show();

                } else if (etAmountAE.getText().toString().equals("")) {
                    Toast.makeText(AddExpenseActivity.this, "Please enter a voucher amount", Toast.LENGTH_SHORT).show();

                } else {
                    saveVoucher();
            }
            }
        });
    }


    public void initialize() {
        dbHelper = new DatabaseHelper(this);

        tvCategoryAE = (TextView) findViewById(R.id.tvCategoryAE);
        tvNoteAE = (TextView) findViewById(R.id.tvNoteAE);
        tvAmountAE = (TextView) findViewById(R.id.tvAmountAE);
        etDateAE = (EditText) findViewById(R.id.etDateAE);
        etCategoryAE = (EditText) findViewById(R.id.etCategoryAE);
        etNoteAE = (EditText) findViewById(R.id.etNoteAE);
        etAmountAE = (EditText) findViewById(R.id.etAmountAE);
        bSaveAE = (Button) findViewById(R.id.bSaveAE);

        AllData.setTextWatcher(this, etCategoryAE, tvCategoryAE);
        AllData.setTextWatcher(this, etAmountAE, tvAmountAE);
        AllData.setTextWatcher(this, etNoteAE, tvNoteAE);

        Calendar calendar = Calendar.getInstance();
        setDate(calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH), calendar.get(Calendar.YEAR));
    }



    private static void setDate(int day, int month, int year) {
        nDay = day; nMonth = month + 1; nYear = year;
        String sDay = String.valueOf(nDay);
        String sMonth = String.valueOf(nMonth);

        Calendar cl = Calendar.getInstance();

        if (nDay < 10) {
            sDay = (new StringBuilder("0")).append(nDay).toString();
        }
        if (nMonth < 10) {
            sMonth = (new StringBuilder("0")).append(nMonth).toString();
        }

        etDateAE.setText(((new StringBuilder(sDay)).append(" - ").append(sMonth).
                append(" - ").append(nYear)).toString());

        sDate = etDateAE.getText().toString();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd - MM - yyyy");
        try {
            Date date = dateFormat.parse(sDate);
            cl.setTime(date);

        } catch (ParseException parseException) {
            parseException.printStackTrace();
        }


        dayId = DateFormat.format("EE, dd MMMM yyyy", cl).toString();
        monthId = DateFormat.format("MMMM yyyy", cl).toString();
    }



    private void  saveVoucher(){
        HashMap<String, String> dailyVoucher = new HashMap<>();
        sTime = DateFormat.format("hh:mm a", Calendar.getInstance()).toString();

        dailyVoucher.put("day_id", dayId);
        dailyVoucher.put("month_id", monthId);
        dailyVoucher.put("day", String.valueOf(nDay));
        dailyVoucher.put("month", String.valueOf(nMonth));
        dailyVoucher.put("year_id", String.valueOf(nYear));
        dailyVoucher.put("time", sTime);
        dailyVoucher.put("date", sDate);
        dailyVoucher.put("expense_category", etCategoryAE.getText().toString());
        dailyVoucher.put("expense_amount", etAmountAE.getText().toString());
        dailyVoucher.put("expense_note", etNoteAE.getText().toString());

        dbHelper.insertRowInTable1(dailyVoucher);
        etCategoryAE.setText("");
        etAmountAE.setText("");
        etNoteAE.setText("");
        Toast.makeText(this, dayId, Toast.LENGTH_LONG).show();
    }



    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
            startActivity(new Intent(this, ViewExpenseActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, ViewExpenseActivity.class));
    }

    public void showDatePickerDialog() {
        DialogFragment dateFragment = new DatePickerFragment();
        dateFragment.show(getFragmentManager(), "datePicker");
    }


    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            setDate(day, month, year);
        }
    }
}


