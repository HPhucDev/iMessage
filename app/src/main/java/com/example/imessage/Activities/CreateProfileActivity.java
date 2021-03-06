package com.example.imessage.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.imessage.Models.User;
import com.example.imessage.databinding.ActivityCreateprofileBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Date;
import java.util.HashMap;

public class CreateProfileActivity extends AppCompatActivity {
    ActivityCreateprofileBinding binding;
    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseStorage storage;
    Uri selectedImage;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateprofileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        dialog = new ProgressDialog(this);
        dialog.setMessage("Updating profile...");
        dialog.setCancelable(false);

        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        auth = FirebaseAuth.getInstance();

        getSupportActionBar().hide();

        binding.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 45);
            }
        });

        binding.setUpProfileButtonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = binding.fullNameTextBoxView.getText().toString();

                if(name.isEmpty()) {
                    binding.fullNameTextBoxView.setError("Please type a name");
                    return;
                }

                dialog.show();
                if(selectedImage != null) {
                    StorageReference reference = storage.getReference().child("Profiles").child(auth.getUid());
                    reference.putFile(selectedImage).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if(task.isSuccessful()) {
                                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        // chose a Character random from this String
                                        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                                                + "0123456789"
                                                + "abcdefghijklmnopqrstuvxyz";
                                        // create StringBuffer size of AlphaNumericString
                                        StringBuilder sb = new StringBuilder(163);
                                        for (int i = 0; i < 163; i++) {
                                            // generate a random number between
                                            // 0 to AlphaNumericString variable length
                                            int index
                                                    = (int)(AlphaNumericString.length()
                                                    * Math.random());
                                            // add Character one by one in end of sb
                                            sb.append(AlphaNumericString
                                                    .charAt(index));
                                        }
                                        String token =  sb.toString();
                                        String imageUrl = uri.toString();



                                        String uid = auth.getUid();
                                        String phone = auth.getCurrentUser().getPhoneNumber();
                                        String name = binding.fullNameTextBoxView.getText().toString();
                                        String userNameLoggined =  name;
                                        User user = new User(uid, name, phone, imageUrl,token);

                                        database.getReference()
                                                .child("users")
                                                .child(uid)
                                                .setValue(user)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        dialog.dismiss();
                                                        Intent intent = new Intent(CreateProfileActivity.this, MainActivity.class);
                                                        intent.putExtra("userNameLoggined",userNameLoggined);
                                                        startActivity(intent);
                                                        finish();
                                                    }
                                                });
                                    }
                                });
                            }
                        }
                    });
                } else {

                    // chose a Character random from this String
                    String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                            + "0123456789"
                            + "abcdefghijklmnopqrstuvxyz";
                    // create StringBuffer size of AlphaNumericString
                    StringBuilder sb = new StringBuilder(163);
                    for (int i = 0; i < 163; i++) {
                        // generate a random number between
                        // 0 to AlphaNumericString variable length
                        int index
                                = (int)(AlphaNumericString.length()
                                * Math.random());
                        // add Character one by one in end of sb
                        sb.append(AlphaNumericString
                                .charAt(index));
                    }
                    String token =  sb.toString();
                    String uid = auth.getUid();
                    String phone = auth.getCurrentUser().getPhoneNumber();

                    String userNameLoggined =  name;
                    User user = new User(uid, name, phone, "No Image",token);

                    database.getReference()
                            .child("users")
                            .child(uid)
                            .setValue(user)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    dialog.dismiss();
                                    Intent intent = new Intent(CreateProfileActivity.this, MainActivity.class);
                                    intent.putExtra("userNameLoggined",userNameLoggined);
                                    startActivity(intent);
                                    finish();
                                }
                            });
                }

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(data != null) {
            if(data.getData() != null) {
                Uri uri = data.getData(); // filepath
                FirebaseStorage storage = FirebaseStorage.getInstance();
                long time = new Date().getTime();
                StorageReference reference = storage.getReference().child("Profiles").child(time+"");
                reference.putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if(task.isSuccessful()) {
                            reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String filePath = uri.toString();
                                    HashMap<String, Object> obj = new HashMap<>();
                                    obj.put("image", filePath);
                                    database.getReference().child("users")
                                            .child(FirebaseAuth.getInstance().getUid())
                                            .updateChildren(obj).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                        }
                                    });
                                }
                            });
                        }
                    }
                });


                binding.imageView.setImageURI(data.getData());
                selectedImage = data.getData();
            }
        }
    }
}