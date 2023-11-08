package com.example.dapm_food;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.hbb20.CountryCodePicker;

public class DeliveryLoginPhone extends AppCompatActivity {

    TextInputLayout phonenumb;
    EditText num;
    Button sendotp, signinemail;
    TextView txtsignup;
    CountryCodePicker cpp;
    FirebaseAuth FAuth;
    String numberr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_login_phone);

        num = (EditText) findViewById(R.id.Dphonenumber);
        sendotp = (Button) findViewById(R.id.Sendotp);
        cpp = (CountryCodePicker) findViewById(R.id.countrycode);
        cpp.setDefaultCountryUsingNameCode("VN");
        cpp.resetToDefaultCountry();
        signinemail = (Button) findViewById(R.id.DbtnEmail);
        txtsignup = (TextView) findViewById(R.id.Signupif);

        FAuth = FirebaseAuth.getInstance();

        if (isvalid()) {
            sendotp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    numberr = num.getText().toString().trim();
                    String phonenumber = cpp.getSelectedCountryCodeWithPlus() + numberr;
                    Intent b = new Intent(DeliveryLoginPhone.this, DeliverySendOtp.class);
                    b.putExtra("phonenumber", phonenumber);
                    startActivity(b);
                    finish();

                }
            });
        }


        txtsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a = new Intent(DeliveryLoginPhone.this, DeliveryRegisteration.class);
                startActivity(a);
                finish();
            }
        });

        signinemail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent em = new Intent(DeliveryLoginPhone.this, DeliveryLoginEmail.class);
                startActivity(em);
                finish();
            }
        });

    }

    public boolean isvalid() {
        phonenumb.setErrorEnabled(false);
        phonenumb.setError("");
        boolean isValidPhone = false,isvalid = false;
        if (TextUtils.isEmpty(numberr)) {
            phonenumb.setErrorEnabled(true);
            phonenumb.setError("Số điện thoại không được để trống");
        }
        else {
            if (numberr.length() < 10) {
                phonenumb.setErrorEnabled(true);
                phonenumb.setError("Số điện thoại không tồn tại");
            } else if (numberr.length() > 11) {
                phonenumb.setErrorEnabled(true);
                phonenumb.setError("Số điện thoại không tồn tại");
            }
            else {isValidPhone = true;}
        }
        isvalid = (isValidPhone) ? true : false;
        return isvalid;
    }




}
