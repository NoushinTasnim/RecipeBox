package com.example.quotepad.main.user.forgot_pass;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.quotepad.main.user.UserActivity;
import com.example.quotepad.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ForgetPasswordActivity extends AppCompatActivity {

    private Button btn;
    private TextInputLayout til;
    private ProgressDialog progressDialog;

    FirebaseDatabase rootNode;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        btn = findViewById(R.id.forgot_pass_btn_mail);
        til = findViewById(R.id.forgot_pass_user);

        progressDialog = new ProgressDialog(this);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rootNode = FirebaseDatabase.getInstance();
                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

                progressDialog.setMessage("Please wait while we get your information");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();
                //Get all the values

                String user = til.getEditText().getText().toString().trim();

                til.setErrorEnabled(false);

                if(TextUtils.isEmpty(user))
                {
                    til.setError(" Enter username or email");
                }
                else if (user.matches(emailPattern))
                {
                    FirebaseAuth.getInstance().sendPasswordResetEmail(user)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d(TAG, "Email sent.");
                                        Toast.makeText(ForgetPasswordActivity.this, "We've sent a password reset email to " + user, Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(ForgetPasswordActivity.this, UserActivity.class));
                                        finish();
                                    }
                                    else
                                    {
                                        Log.i(TAG, "onComplete: " + task.getException().toString());
                                        progressDialog.dismiss();
                                        til.setError("No user found");
                                        Toast.makeText(ForgetPasswordActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
                else{
                    reference = rootNode.getReference("emails");
                    Query checkUser = reference.orderByChild("username").equalTo(user);

                    checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()){
                                progressDialog.hide();
                                String mail = snapshot.child(user).child("email").getValue(String.class);

                                Log.i(TAG, "onDataChange: " + mail);

                                    if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                                        Log.i(TAG, "onDataChange: user");
                                        FirebaseAuth.getInstance().signOut();
                                    }

                                    FirebaseAuth.getInstance().sendPasswordResetEmail(mail)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Log.d(TAG, "Email sent.");
                                                        Toast.makeText(ForgetPasswordActivity.this, "We've sent a password reset email to " + mail, Toast.LENGTH_SHORT).show();
                                                        startActivity(new Intent(ForgetPasswordActivity.this, UserActivity.class));
                                                        finish();
                                                    }
                                                    else
                                                    {
                                                        Log.i(TAG, "onComplete: " + task.getException().toString());
                                                        Toast.makeText(ForgetPasswordActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });

                            }
                            else
                            {
                                progressDialog.dismiss();
                                til.setError("No user found");
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });
    }
}