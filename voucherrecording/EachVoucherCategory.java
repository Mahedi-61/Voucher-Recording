package com.mahediapps.voucherrecording;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.mahediapps.adapter.AdapterForEachVoucherCategory;
import com.mahediapps.model.VoucherCalculation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class EachVoucherCategory extends ActionBarActivity {

    private Toolbar mToolbar;
    private Calendar calendar;
    private ImageButton ibNext, ibPrevious;
    private ListView lvVoucherCategoryList;
    private TextView tvTitle, tvTotalAmount, tvMonthOrYear;
    private VoucherCalculation vouCal;
    private String monthId, yearId;
    private ArrayList<String> monthlyCategoryList, yearCategoryList;
    private int nCategoryNumber;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_each_voucher_category);

        mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("View Classified Expense");

        initialize();


        if (getIntent().getIntExtra("month_year", 1) == 1) {
            setEachVoucherCategoryListForMonth();

        } else {
            Object obj = new SimpleDateFormat("yyyy");
            try {
                yearId = getIntent().getStringExtra("year_id");
                obj = ((SimpleDateFormat) (obj)).parse(yearId);
                calendar.setTime(((java.util.Date) (obj)));

            } catch (ParseException parseexception) {
                parseexception.printStackTrace();
            }
        }


        lvVoucherCategoryList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }


    private void initialize() {
        vouCal = new VoucherCalculation(this);
        calendar = Calendar.getInstance();

        lvVoucherCategoryList = (ListView) findViewById(R.id.lvExpenseEVC);
        ibPrevious = (ImageButton) findViewById(R.id.ibPreviousExpenseEVC);
        ibNext = (ImageButton) findViewById(R.id.ibNextExpenseEVC);
        tvTitle = (TextView) findViewById(R.id.tvTitleEVC);
        tvTotalAmount = (TextView) findViewById(R.id.tvTotalAmountEVC);
        tvMonthOrYear = (TextView) findViewById(R.id.tvMonthYearNameEVC);
    }


    private void setEachVoucherCategoryListForMonth() {
        monthId = getIntent().getStringExtra("month_id");
        Object obj = new SimpleDateFormat("MMMM yyyy");
        try {
            obj = ((SimpleDateFormat) (obj)).parse(monthId);
            calendar.setTime(((java.util.Date) (obj)));
        } catch (ParseException parseexception) {
            parseexception.printStackTrace();
        }

        tvMonthOrYear.setText(monthId);
        monthlyCategoryList = vouCal.getTotalUsedVoucherCategoryPerMonth(monthId);
        nCategoryNumber = getIntent().getIntExtra("category_position", 1);
        tvTitle.setText(monthlyCategoryList.get(nCategoryNumber));

        refreshMonthlyCategoryList();

        ibNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nCategoryNumber == monthlyCategoryList.size() - 1) {
                    nCategoryNumber = 0;
                } else nCategoryNumber++;

                refreshMonthlyCategoryList();
            }
        });

        ibPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nCategoryNumber == 0) {
                    nCategoryNumber = monthlyCategoryList.size() - 1;
                } else nCategoryNumber--;

                refreshMonthlyCategoryList();
            }
        });
    }


    private void refreshMonthlyCategoryList() {
        tvTitle.setText(monthlyCategoryList.get(nCategoryNumber));
        lvVoucherCategoryList.setAdapter(new AdapterForEachVoucherCategory(this,
                vouCal.getNotePerVoucherCategoryPerMonth(monthId, monthlyCategoryList.get(nCategoryNumber)),
                vouCal.getAmountPerVoucherCategoryPerMonth(monthId, monthlyCategoryList.get(nCategoryNumber)),
                vouCal.getDatePerVoucherCategoryPerMonth(monthId, monthlyCategoryList.get(nCategoryNumber)),
                vouCal.getIdPerVoucherCategoryPerMonth(monthId, monthlyCategoryList.get(nCategoryNumber))));

        tvTotalAmount.setText((vouCal.getTotalAmountPerVoucherCategoryPerMonth(monthId)).get(nCategoryNumber));
        tvTotalAmount.append("/=");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}
