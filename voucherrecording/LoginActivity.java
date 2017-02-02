package com.mahediapps.voucherrecording;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class LoginActivity extends Activity implements View.OnClickListener {

    private TextView tvTitle1, tvTitle2;
    private Button bLogin;
    private CheckBox cbRememberUserId;
    private EditText etPassword;
    private EditText etUserId;
    private String givenPassword, givenUserId, savePassword, saveUserId;
    private String mobileNumber;
    private Intent intent;
    private SharedPreferences profile;
    private TextView tvForgotPassword;


    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_login);
        initialize();

        profile = getSharedPreferences("profile", 0);
        saveUserId = profile.getString("user_id", "");
        savePassword = profile.getString("login_password", "");
        intent = new Intent(this, MainActivity.class);

        tvTitle1 = (TextView)findViewById(R.id.tvTitle1LA);
        tvTitle2 = (TextView)findViewById(R.id.tvTitle2LA);
        android.view.animation.Animation animation1 = AnimationUtils.loadAnimation(this, R.anim.left_to_right);
        android.view.animation.Animation animation2 = AnimationUtils.loadAnimation(this, R.anim.right_to_left);
        tvTitle1.setAnimation(animation1);
        tvTitle2.setAnimation(animation2);
        tvTitle1.startAnimation(animation1);
        tvTitle2.startAnimation(animation2);
        tvTitle1.setVisibility(View.VISIBLE);
        tvTitle2.setVisibility(View.VISIBLE);

        if (profile.getInt("user_id_checked", 0) == 1) {
            cbRememberUserId.setChecked(true);
            etUserId.setText(saveUserId);
            etUserId.setEnabled(false);
        }
        if (profile.getInt("first_time", 0) == 0) {
            startActivity(new Intent(this, CreateAccount.class));
        }

        bLogin.setOnClickListener(this);
        tvForgotPassword.setOnClickListener(this);

        etPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    loginButtonFunction();
                }
                return false;
            }
        });
    }


    private void initialize() {
        etUserId = (EditText) findViewById(R.id.etUserIdLA);
        etPassword = (EditText) findViewById(R.id.etPasswordLA);
        bLogin = (Button) findViewById(R.id.bLoginLA);
        tvForgotPassword = (TextView) findViewById(R.id.tvForgotPasswordLA);
        cbRememberUserId = (CheckBox) findViewById(R.id.cbRemeberUserIdLA);
    }


    public void onClick(final View view) {
        switch (view.getId()) {
            case R.id.bLoginLA: {
                loginButtonFunction();
                break;
            }

            case R.id.tvForgotPasswordLA: {
                mobileNumber = profile.getString("mobile_number", "");
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
                builder.setTitle("Sms backup password");
                builder.setMessage("Are you sure to proceed ?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialoginterface, int i) {
                        try {
                            SmsManager.getDefault().sendTextMessage(mobileNumber, null,
                                    (new StringBuilder("Your Voucher Recording App Login User Id:  "))
                                            .append(saveUserId).append(" \n Login Password: ")
                                            .append(savePassword).toString(), null, null);

                            Toast.makeText(getApplicationContext(), "SMS send successfully !!", Toast.LENGTH_SHORT).show();

                            return;

                        } catch (Exception ex) {
                            Toast.makeText(getApplicationContext(), "SMS faild to send !!", Toast.LENGTH_SHORT).show();
                        }


                    }

                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialoginterface, int i) {

                    }

                });
                builder.show();
                return;
            }
        }
    }


    protected void onPause() {
        super.onPause();
        finish();
    }


    private void loginButtonFunction() {
        givenUserId = etUserId.getText().toString();
        givenPassword = etPassword.getText().toString();

        if (savePassword.equals(givenPassword) && saveUserId.equals(givenUserId)) {
            SharedPreferences.Editor edit = profile.edit();

            if (cbRememberUserId.isChecked()) {
                edit.putInt("user_id_checked", 1);
            } else {
                edit.putInt("user_id_checked", 0);
            }
            edit.commit();
            startActivity(intent);
        } else {
            Toast.makeText(this, "Wrong User Id or Password. Try Again !!", Toast.LENGTH_SHORT).show();
        }
    }
}
