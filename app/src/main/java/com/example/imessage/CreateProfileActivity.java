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


public class CreateProfileActivity extends AppCompatActivity {

    Button setUpProfileButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        getSupportActionBar().hide(); //hide the title bar
        setContentView(R.layout.activity_createprofile);

         setUpProfileButton = findViewById(R.id.setUpProfileButtonView); //Tìm lại button
         setUpProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(CreateProfileActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

}