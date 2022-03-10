package com.example.imessage;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;


public class OTPActivity extends AppCompatActivity {

    Button verifyButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        getSupportActionBar().hide(); //hide the title bar
        setContentView(R.layout.activity_otp);

        verifyButton = findViewById(R.id.verifyButtonView); //Tìm lại button
        verifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(OTPActivity.this, CreateProfileActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

}