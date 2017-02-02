package com.mahediapps.voucherrecording;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.mahediapps.adapter.AdapterForDailyVoucher;
import com.mahediapps.adapter.AdapterForMonthlyVoucher;
import com.mahediapps.model.VoucherCalculation;

import java.util.ArrayList;
import java.util.Calendar;

public class ViewIncomeActivity extends ActionBarActivity {

    private Toolbar mToolbar;
    private Calendar calendar;
    private ImageButton ibNext, ibPrevious;
    private ListView lvVoucherList;
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
        setContentView(R.layout.activity_view_income);

        mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("View Income");

        initialize();
        int number = profile.getInt("vi_rb", 1);

        if(number == 1){
            setMonthlyVocuherList();
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

                    case R.id.rbMonthlyVI: {
                        setMonthlyVocuherList();
                        a = 1;
                        break;
                    }

                    case R.id.rbYearlyVI: {
                        setYearlyVocuherList();
                        a = 2;
                        break;
                    }
                }//end of switch

                editor.putInt("vi_rb", a);
                editor.commit();
            }
        }); //end of onCheckChangedListener

        lvVoucherList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(ViewIncomeActivity.this, EditExpenseActivity.class);
                ArrayList<String> allIdsInList = new ArrayList<>();

               if(rbMonthly.isChecked()){
                    allIdsInList = vouCal.getVoucherIdPerMonth(monthId);

                }else if(rbYearly.isChecked()){
                    allIdsInList = vouCal.getVoucherIdPerYear(yearId);
                }
                i.putExtra("id", allIdsInList.get(position));
                startActivity(i);
                finish();
            }
        });
    }


    private void initialize() {
        vouCal = new VoucherCalculation(this);
        profile = getSharedPreferences("profile", 0);
        editor = profile.edit();

        lvVoucherList = (ListView) findViewById(R.id.lvExpenseVI);
        rbSelector = (RadioGroup) findViewById(R.id.rbSelectorVI);
        rbMonthly = (RadioButton) rbSelector.findViewById(R.id.rbMonthlyVI);
        rbYearly = (RadioButton) rbSelector.findViewById(R.id.rbYearlyVI);
        ibPrevious = (ImageButton) findViewById(R.id.ibPreviousExpenseVI);
        ibNext = (ImageButton) findViewById(R.id.ibNextExpenseVI);
        tvTitle = (TextView) findViewById(R.id.tvTitleVI);
        tvTotalAmount = (TextView) findViewById(R.id.tvTotalAmountVI);

        calendar = Calendar.getInstance();
    }


    //monthly voucher method --- 4
    private void setMonthlyVocuherList(){
        calendar = Calendar.getInstance();
        monthId = DateFormat.format("MMMM yyyy", calendar).toString();
        tvTitle.setText(monthId);

        lvVoucherList.setAdapter(new AdapterForMonthlyVoucher(this,
                vouCal.getVoucherNotePerMonth(monthId),
                vouCal.getVoucherAmountPerMonth(monthId),
                vouCal.getVoucherCategoryPerMonth(monthId),
                vouCal.getVoucherDatePerMonth(monthId),
                vouCal.getVoucherIdPerMonth(monthId)));

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
        monthId = DateFormat.format("MMMM yyyy", calendar).toString();
        tvTitle.setText(monthId);

        lvVoucherList.setAdapter(new AdapterForMonthlyVoucher(this,
                vouCal.getVoucherNotePerMonth(monthId),
                vouCal.getVoucherAmountPerMonth(monthId),
                vouCal.getVoucherCategoryPerMonth(monthId),
                vouCal.getVoucherDatePerMonth(monthId),
                vouCal.getVoucherIdPerMonth(monthId)));

        tvTotalAmount.setText(vouCal.getTotalAmountVoucherPerMonth(monthId));
        tvTotalAmount.append("/=");
    }


    private void setNextMonth() {
        if (calendar.get(Calendar.MONTH) == calendar.getActualMaximum(Calendar.MONTH)) {
            calendar.set(calendar.get(Calendar.YEAR) + 1, calendar.getActualMinimum(Calendar.MONTH), 1);
            return;

        } else {
            calendar.add(Calendar.MONTH, 1);
            return;
        }
    }


    private void setPreviousMonth() {
        if (calendar.get(Calendar.MONTH) == calendar.getActualMinimum(Calendar.MONTH)) {
            calendar.set(calendar.get(Calendar.YEAR) - 1, calendar.getActualMaximum(Calendar.MONTH), 31);
            return;
        } else {
            calendar.add(Calendar.MONTH, -1);
            return;
        }
    }


    //yearly voucher method --- 2
    private void setYearlyVocuherList(){
        calendar = Calendar.getInstance();
        yearId = DateFormat.format("yyyy", calendar).toString();
        tvTitle.setText(yearId);

        lvVoucherList.setAdapter(new AdapterForMonthlyVoucher(this,
                vouCal.getVoucherNotePerYear(yearId),
                vouCal.getVoucherAmountPerYear(yearId),
                vouCal.getVoucherCategoryPerYear(yearId),
                vouCal.getVoucherDatePerYear(yearId),
                vouCal.getVoucherIdPerYear(yearId)));

        tvTotalAmount.setText(vouCal.getTotalAmountVoucherPerYear(yearId));
        tvTotalAmount.append("/=");

        ibPrevious.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                calendar.add(Calendar.YEAR, -1);
                refreshYearlyVoucherList();
            }
        });
        ibNext.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                calendar.add(Calendar.YEAR, 1);
                refreshYearlyVoucherList();
            }
        });
    }


    private void refreshYearlyVoucherList() {
        yearId = DateFormat.format("yyyy", calendar).toString();
        tvTitle.setText(yearId);

        lvVoucherList.setAdapter(new AdapterForMonthlyVoucher(this,
                vouCal.getVoucherNotePerYear(yearId),
                vouCal.getVoucherAmountPerYear(yearId),
                vouCal.getVoucherCategoryPerYear(yearId),
                vouCal.getVoucherDatePerYear(yearId),
                vouCal.getVoucherIdPerYear(yearId)));

        tvTotalAmount.setText(vouCal.getTotalAmountVoucherPerYear(yearId));
        tvTotalAmount.append("/=");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
         super.onCreateOptionsMenu(menu);
         getMenuInflater().inflate(R.menu.view_income, menu);
         return true;
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_income_add){
            startActivity(new Intent(this, AddIncomeActivity.class));
            finish();

        }else if(item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}
