package com.example.imessage.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.imessage.databinding.ActivityOtpBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mukesh.OnOtpCompletionListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class OTPActivity extends AppCompatActivity {


    ActivityOtpBinding binding;
    FirebaseAuth auth;
    String verificationID;
    ProgressDialog dialog;
    String created = "false";
    String phone = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityOtpBinding.inflate(getLayoutInflater());

        getSupportActionBar().hide(); //hide the title bar
        setContentView(binding.getRoot()); // show view

        dialog = new ProgressDialog(this);
        dialog.setMessage("Sending OTP...");
        dialog.setCancelable(false);
        dialog.show();

        String phoneNumber = getIntent().getStringExtra("phoneNumber");// get phoneNumber from PhoneNumberActivity
        binding.phoneNumberTextView.setText("Verify " + phoneNumber);

        auth = FirebaseAuth.getInstance();
        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(auth)
                .setPhoneNumber(phoneNumber)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(OTPActivity.this)
                .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {

                    }

                    @Override
                    public void onCodeSent(@NonNull String verifyID, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        super.onCodeSent(verifyID, forceResendingToken);
                        dialog.dismiss();
                        verificationID=verifyID;

                        InputMethodManager imm = (InputMethodManager)  getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                        binding.otpView.requestFocus();
                    }
                }).build();
        PhoneAuthProvider.verifyPhoneNumber(options);

        binding.otpView.setOtpCompletionListener(new OnOtpCompletionListener() {
            @Override
            public void onOtpCompleted(String otp) {
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationID, otp);

                auth.signInWithCredential(credential).addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            DatabaseReference database = FirebaseDatabase.getInstance().getReference();
                            DatabaseReference userRef = database.child("users");
                            userRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DataSnapshot> task) {

                                    if (task.isSuccessful()) {
                                        List<String> users = new ArrayList<>();
                                        List<String> userNames = new ArrayList<>();
                                        for (DataSnapshot ds : task.getResult().getChildren()) {
                                            String sub = ds.child("phoneNumber").getValue(String.class);
                                            String user = ds.child("name").getValue(String.class);
                                            if(!users.contains(sub)) {
                                                users.add(sub);
                                            }
                                            if(!userNames.contains(user)){
                                                userNames.add(user);
                                            }
                                        }
                                        for (int i =0;i< users.size();i++){
                                            Log.i("userPhone",users.get(i));
                                            if(users.get(i).equals(phoneNumber)){
                                                created="true";
                                                String userNameLoggined = userNames.get(i);
                                                Intent intent = new Intent(OTPActivity.this, MainActivity.class);
                                                intent.putExtra("created",created);
                                                intent.putExtra("userNameLoggined",userNameLoggined);
                                                startActivity(intent);
                                                finishAffinity();
                                            }
                                        }
                                        if(created.equals("false")){
                                            created = "false";
                                            Intent intent = new Intent(OTPActivity.this, MainActivity.class);
                                            intent.putExtra("created",created);

                                            Intent intent1 = new Intent(OTPActivity.this, CreateProfileActivity.class);
                                            startActivity(intent1);
                                            finishAffinity();
                                        }
                                    } else {
                                        Log.d("TAG", task.getException().getMessage()); //Don't ignore potential errors!

                                    }
                                }
                            });
                        } else {
                            Toast.makeText(OTPActivity.this, "Failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });


    }



}