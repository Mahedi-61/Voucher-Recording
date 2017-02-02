package com.mahediapps.voucherrecording;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;


public class EditExpenseActivity extends ActionBarActivity {

    private Toolbar mToolbar;
    private Button bSaveAE, bDelete;
    private TextView tvCategoryAE, tvAmountAE, tvNoteAE;
    private static EditText etDateAE, etCategoryAE, etAmountAE, etNoteAE;
    private HashMap<String, String> allPreContent;
    private static int nDay, nMonth, nYear;
    private static String dayId, monthId, sTime, sDate;
    private DatabaseHelper dbHelper;
    private String sID;


    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_add_expense);
        mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        mToolbar.setTitle("Edit Voucher");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initialize();

        sID = getIntent().getStringExtra("id");
        initializeForDailyVoucher();

        etDateAE.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                showDatePickerDialog();
            }
        });


        etCategoryAE.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                final ArrayList<String> alCategory = dbHelper.getAllContentFromTable3();

                final Dialog dialog = new Dialog(EditExpenseActivity.this);
                dialog.setContentView(R.layout.dl_category_list);
                dialog.setTitle("Select a Category");
                ListView lvCategoryList = (ListView) dialog.findViewById(R.id.lvCategoryListAEA);
                lvCategoryList.setAdapter(new ArrayAdapter<>(EditExpenseActivity.this, android.R.layout.simple_list_item_1, alCategory));
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
                    Toast.makeText(EditExpenseActivity.this, "Please enter a voucher category", Toast.LENGTH_SHORT).show();

                } else if (etNoteAE.getText().toString().equals("")) {
                    Toast.makeText(EditExpenseActivity.this, "Please enter a voucher note", Toast.LENGTH_SHORT).show();

                } else if (etAmountAE.getText().toString().equals("")) {
                    Toast.makeText(EditExpenseActivity.this, "Please enter a voucher amount", Toast.LENGTH_SHORT).show();

                } else {
                    updateDaillyVoucher();
                    startActivity(new Intent(EditExpenseActivity.this, ViewExpenseActivity.class));
            }
            }
        });

        bDelete.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                dbHelper.deleteRowInTable1(sID);
                finish();
                startActivity(new Intent(EditExpenseActivity.this, ViewExpenseActivity.class));
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
        bSaveAE.setText("Update");
        bDelete = (Button) findViewById(R.id.bDeleteAE);
        bDelete.setVisibility(View.VISIBLE);

        AllData.setTextWatcher(this, etCategoryAE, tvCategoryAE);
        AllData.setTextWatcher(this, etAmountAE, tvAmountAE);
        AllData.setTextWatcher(this, etNoteAE, tvNoteAE);
    }


    public void initializeForDailyVoucher(){
        allPreContent = dbHelper.getARowFromTable1(sID);

        dayId = allPreContent.get(dbHelper.DAY_ID);
        monthId = allPreContent.get(dbHelper.MONTH_ID);

        nDay = Integer.parseInt(allPreContent.get(dbHelper.DAY));
        nMonth = Integer.parseInt(allPreContent.get(dbHelper.MONTH));
        nYear = Integer.parseInt(allPreContent.get(dbHelper.YEAR));

        etDateAE.setText(allPreContent.get(dbHelper.DATE));
        etCategoryAE.setText(allPreContent.get(dbHelper.EXPENSE_CATEGORY));
        etNoteAE.setText(allPreContent.get(dbHelper.EXPENSE_NOTE));
        etAmountAE.setText(allPreContent.get(dbHelper.EXPENSE_AMOUNT));
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



    private void updateDaillyVoucher(){
        HashMap<String, String> dailyVoucher = new HashMap<>();
        sTime = DateFormat.format("hh:mm a", Calendar.getInstance()).toString();

        dailyVoucher.put(dbHelper.ID,               sID);
        dailyVoucher.put(dbHelper.DAY_ID,           dayId);
        dailyVoucher.put(dbHelper.MONTH_ID,         monthId);
        dailyVoucher.put(dbHelper.DAY,              String.valueOf(nDay));
        dailyVoucher.put(dbHelper.MONTH,            String.valueOf(nMonth));
        dailyVoucher.put(dbHelper.YEAR,             String.valueOf(nYear));
        dailyVoucher.put(dbHelper.TIME,             sTime);
        dailyVoucher.put(dbHelper.DATE,             etDateAE.getText().toString());
        dailyVoucher.put(dbHelper.EXPENSE_CATEGORY, etCategoryAE.getText().toString());
        dailyVoucher.put(dbHelper.EXPENSE_AMOUNT,   etAmountAE.getText().toString());
        dailyVoucher.put(dbHelper.EXPENSE_NOTE,     etNoteAE.getText().toString());

        dbHelper.updateRowInTable1(dailyVoucher);
        finish();
        Toast.makeText(this, "Your voucher is sucessfully updated", Toast.LENGTH_SHORT).show();
    }



    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
            startActivity(new Intent(EditExpenseActivity.this, ViewExpenseActivity.class));
        }
        return super.onOptionsItemSelected(item);
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


