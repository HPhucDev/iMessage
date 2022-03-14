package com.example.imessage.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.imessage.R;
import com.example.imessage.databinding.ActivityPhonenumberBinding;
import com.hbb20.CountryCodePicker;


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
