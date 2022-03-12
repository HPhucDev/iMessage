package com.example.imessage;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.imessage.databinding.ActivityPhonenumberBinding;
import com.hbb20.CountryCodePicker;

import java.util.zip.Inflater;


public class PhoneNumberActivity extends AppCompatActivity {

    ActivityPhonenumberBinding binding;
    String countryCode;
    CountryCodePicker mcountrycodepicker;
    String phoneNumberInTextBox;
    String phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityPhonenumberBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide(); //hide the title bar


        mcountrycodepicker=findViewById(R.id.countrycodepicker);//get code country
        countryCode=mcountrycodepicker.getSelectedCountryCodeWithPlus();

        mcountrycodepicker.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                countryCode=mcountrycodepicker.getSelectedCountryCodeWithPlus();
            }
        });


        binding.continueButtonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PhoneNumberActivity.this, OTPActivity.class);
                phoneNumberInTextBox=binding.phoneNumberTextBoxView.getText().toString().substring(1);//get string in text box without first character
                phoneNumber=countryCode+phoneNumberInTextBox;
                intent.putExtra("phoneNumber",phoneNumber);
                startActivity(intent);
                finish();
            }
        });

    }

}
