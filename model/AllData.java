package com.mahediapps.model;


import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.mahediapps.voucherrecording.R;

public class AllData {

    public static int menuIcon[] = {

           R.mipmap.ic_transaction, R.mipmap.ic_income, R.mipmap.ic_expense,
           R.mipmap.ic_category, R.mipmap.ic_statistics, R.mipmap.ic_settings, R.mipmap.icon_more_app
    };


    public static String menuList[] = {
            "View Transaction", "View Income", "View Expense", "View Classified Expense", "Statistics", "Settings", "More App"
    };

    public static String searchList[] = { "Search By Day", "Search By Month", "Search By Previous Days"};


    public static void setTextWatcher(final Context context, EditText edittext, final TextView textView) {
        edittext.addTextChangedListener(new TextWatcher() {

            private boolean flag;

            public void afterTextChanged(Editable editable) {
            }

            public void beforeTextChanged(CharSequence charsequence, int i, int j, int k) {
                if (charsequence.length() == 0) {
                    flag = true;
                    return;
                } else {
                    flag = false;
                    return;
                }
            }

            public void onTextChanged(CharSequence charsequence, int i, int j, int k) {
                if (charsequence.length() == 0) {
                    Animation animation1 = AnimationUtils.loadAnimation(context, R.anim.hint_slide_down);
                    textView.startAnimation(animation1);
                    textView.setVisibility(View.INVISIBLE);
                    return;
                }
                if (charsequence.length() == 1 && flag) {
                    Animation animation2 = AnimationUtils.loadAnimation(context, R.anim.hint_slide_up);
                    textView.startAnimation(animation2);
                }
                textView.setVisibility(View.VISIBLE);
            }

        });
    }

    
}
