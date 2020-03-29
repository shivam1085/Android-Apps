package com.example.blooddonationapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.io.Serializable;

public class otpverification extends AppCompatActivity {
    EditText putotp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpverification);
        putotp = findViewById(R.id.otpget);
    }

    public void checkotp(View view)
    {
        String sentotp = getIntent().getExtras().getString("otp");

        String otpget = putotp.getText().toString();
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(sentotp, otpget);
        signInWithPhoneAuthCredential(credential);
    }
    private void signInWithPhoneAuthCredential( PhoneAuthCredential credential )
    {
        Register.mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            Toast.makeText(getApplicationContext(),"you are verified",Toast.LENGTH_SHORT).show();
                            Intent  intlogin = new Intent(otpverification.this,Login.class);
                            startActivity(intlogin);
                        }
                        else
                        {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(getApplicationContext(),"auth faild",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }
}
