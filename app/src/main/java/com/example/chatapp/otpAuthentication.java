package com.example.chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

public class otpAuthentication extends AppCompatActivity {

    //variable declaration

    TextView mchangenumber;
    EditText mgetotp;
    Button mverifyOtp;
    String enteredotp;

    FirebaseAuth firebaseAuth;
    ProgressBar mprogressbarforotp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_authentication);

        mchangenumber=findViewById(R.id.changeNumber);
        mverifyOtp=findViewById(R.id.verifyOtp);
        mgetotp=findViewById(R.id.getOtp);
        mprogressbarforotp=findViewById(R.id.progressBarforotp);

        firebaseAuth=FirebaseAuth.getInstance();

        mchangenumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(otpAuthentication.this,MainActivity.class);
                startActivity(intent);
            }
        });

        mverifyOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enteredotp=mgetotp.getText().toString();
                if(enteredotp.isEmpty()){
                    Toast.makeText(getApplicationContext(),"Enter your Otp first",Toast.LENGTH_LONG).show();
                }
                else{
                    mprogressbarforotp.setVisibility(View.VISIBLE);
                    String codereceived=getIntent().getStringExtra("otp");
                    PhoneAuthCredential credential= PhoneAuthProvider.getCredential(codereceived,enteredotp);
                    signInWithPhoneAuthCredential(credential);

                }
            }
        });

    }


    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential){

        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    mprogressbarforotp.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(), "Login Sucessfull", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(otpAuthentication.this,setProfile.class);
                    startActivity(intent);
                    finish();

                }else{
                    if(task.getException() instanceof FirebaseAuthInvalidCredentialsException){
                        mprogressbarforotp.setVisibility(View.INVISIBLE);
                        Toast.makeText(getApplicationContext(),"Login Failed",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }

}