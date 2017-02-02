package com.mahediapps.voucherrecording;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.mahediapps.adapter.AdapterForClassifiedExpense;
import com.mahediapps.adapter.AdapterForDailyVoucher;
import com.mahediapps.adapter.AdapterForMonthlyVoucher;
import com.mahediapps.model.DatabaseHelper;
import com.mahediapps.model.VoucherCalculation;

import java.util.ArrayList;
import java.util.Calendar;

public class ClassifiedExpenseActivity extends ActionBarActivity {

    private Toolbar mToolbar;
    private Calendar monthCalendar, yearCalendar;
    private ImageButton ibNext, ibPrevious;
    private ListView lvVoucherCategoryList;
    private RadioButton  rbMonthly, rbYearly;
    private RadioGroup rbSelector;
    private TextView tvTitle, tvTotalAmount;
    private VoucherCalculation vouCal;
    private SharedPreferences profile;
    private SharedPreferences.Editor editor;
    private String  monthId, yearId;
    private int a;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classified_expense);

        mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("View Classified Expense");

        initialize();
        int number = profile.getInt("ce_rb", 1);

        if(number == 1){
            setMonthlyVocuherCategoryList();
            rbMonthly.setChecked(true);
        }
        else if(number == 2) {
            setYearlyVocuherList();
            rbYearly.setChecked(true);
        }


        rbSelector.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rbMonthlyCE: {
                        setMonthlyVocuherCategoryList();
                        a = 1;
                        break;
                    }

                    case R.id.rbYearlyCE: {
                        setYearlyVocuherList();
                        a = 2;
                        break;
                    }
                }//end of switch

                editor.putInt("ce_rb", a);
                editor.commit();
            }
        }); //end of onCheckChangedListener

        lvVoucherCategoryList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(ClassifiedExpenseActivity.this, EachVoucherCategory.class);
                int number = 0;
                int nMonthOrYear = 0;

                if(rbMonthly.isChecked()){
                    number = position;
                    nMonthOrYear = 1;

                }else if(rbYearly.isChecked()){
                    number = position;
                    nMonthOrYear = 2;
                }

                i.putExtra("category_position", number);
                i.putExtra("month_id", monthId);
                i.putExtra("year_id", yearId);
                i.putExtra("month_year", nMonthOrYear);
                startActivity(i);
                finish();
            }
        });
    }


    private void initialize() {
        vouCal = new VoucherCalculation(this);
        profile = getSharedPreferences("profile", 0);
        editor = profile.edit();

        lvVoucherCategoryList = (ListView) findViewById(R.id.lvExpenseCE);
        rbSelector = (RadioGroup) findViewById(R.id.rbSelectorCE);
        rbMonthly = (RadioButton) rbSelector.findViewById(R.id.rbMonthlyCE);
        rbYearly = (RadioButton) rbSelector.findViewById(R.id.rbYearlyCE);
        ibPrevious = (ImageButton) findViewById(R.id.ibPreviousExpenseCE);
        ibNext = (ImageButton) findViewById(R.id.ibNextExpenseCE);
        tvTitle = (TextView) findViewById(R.id.tvTitleCE);
        tvTotalAmount = (TextView) findViewById(R.id.tvTotalAmountCE);

        yearCalendar = Calendar.getInstance();
        monthCalendar = Calendar.getInstance();
    }
    

    //monthly voucher method --- 4
    private void setMonthlyVocuherCategoryList(){
        monthId = DateFormat.format("MMMM yyyy", monthCalendar).toString();
        tvTitle.setText(monthId);

        lvVoucherCategoryList.setAdapter(new AdapterForClassifiedExpense(this,
                vouCal.getTotalAmountPerVoucherCategoryPerMonth(monthId),
                vouCal.getTotalUsedVoucherCategoryPerMonth(monthId)));

        tvTotalAmount.setText(vouCal.getTotalAmountVoucherPerMonth(monthId));
        tvTotalAmount.append("/=");

        ibPrevious.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                setPreviousMonth();
                refreshMonthlyVoucherList();
            }
        });
        ibNext.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                setNextMonth();
                refreshMonthlyVoucherList();
            }
        });
    }


    private void refreshMonthlyVoucherList() {
        monthId = DateFormat.format("MMMM yyyy", monthCalendar).toString();
        tvTitle.setText(monthId);

        lvVoucherCategoryList.setAdapter(new AdapterForClassifiedExpense(this,
                vouCal.getTotalAmountPerVoucherCategoryPerMonth(monthId),
                vouCal.getTotalUsedVoucherCategoryPerMonth(monthId)));

        tvTotalAmount.setText(vouCal.getTotalAmountVoucherPerMonth(monthId));
        tvTotalAmount.append("/=");
    }


    private void setNextMonth() {
        if (monthCalendar.get(Calendar.MONTH) == monthCalendar.getActualMaximum(Calendar.MONTH)) {
            monthCalendar.set(monthCalendar.get(Calendar.YEAR) + 1, monthCalendar.getActualMinimum(Calendar.MONTH), 1);
            return;

        } else {
            monthCalendar.add(Calendar.MONTH, 1);
            return;
        }
    }


    private void setPreviousMonth() {
        if (monthCalendar.get(Calendar.MONTH) == monthCalendar.getActualMinimum(Calendar.MONTH)) {
            monthCalendar.set(monthCalendar.get(Calendar.YEAR) - 1, monthCalendar.getActualMaximum(Calendar.MONTH), 31);
            return;
        } else {
            monthCalendar.add(Calendar.MONTH, -1);
            return;
        }
    }


    //yearly voucher method --- 2
    private void setYearlyVocuherList(){
        yearId = DateFormat.format("yyyy", yearCalendar).toString();
        tvTitle.setText(yearId);

        lvVoucherCategoryList.setAdapter(new AdapterForClassifiedExpense(this,
                vouCal.getTotalAmountPerVoucherCategoryPerYear(yearId),
                vouCal.getTotalUsedVoucherCategoryPerYear(yearId)));

        tvTotalAmount.setText(vouCal.getTotalAmountVoucherPerMonth(yearId));
        tvTotalAmount.append("/=");

        ibPrevious.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                yearCalendar.add(Calendar.YEAR, -1);
                refreshYearlyVoucherList();
            }
        });
        ibNext.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                yearCalendar.add(Calendar.YEAR, 1);
                refreshYearlyVoucherList();
            }
        });
    }


    private void refreshYearlyVoucherList() {
        yearId = DateFormat.format("yyyy", yearCalendar).toString();
        tvTitle.setText(yearId);

        lvVoucherCategoryList.setAdapter(new AdapterForClassifiedExpense(this,
                vouCal.getTotalAmountPerVoucherCategoryPerYear(yearId),
                vouCal.getTotalUsedVoucherCategoryPerYear(yearId)));

        tvTotalAmount.setText(vouCal.getTotalAmountVoucherPerMonth(yearId));
        tvTotalAmount.append("/=");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return  super.onCreateOptionsMenu(menu);
    }


    public boolean onOptionsItemSelected(MenuItem item) {
         if(item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}
