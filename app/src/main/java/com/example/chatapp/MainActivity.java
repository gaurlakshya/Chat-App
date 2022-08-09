package com.example.chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hbb20.CountryCodePicker;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    EditText mgetPhoneNumber;

    Button msendOtp;
    CountryCodePicker mcountrycodePicker;
    String countryCode;
    String phoneNumber;

    FirebaseAuth firebaseAuth;
    ProgressBar mprogressBar;

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallsBack;
    String codeSent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mcountrycodePicker=findViewById(R.id.countrycodepicker);
        msendOtp=findViewById(R.id.sendotp);
        mgetPhoneNumber=findViewById(R.id.getphonenumber);
        mprogressBar=findViewById(R.id.progressBar);
        firebaseAuth=FirebaseAuth.getInstance();
        countryCode=mcountrycodePicker.getDefaultCountryCodeWithPlus();

        mcountrycodePicker.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                countryCode=mcountrycodePicker.getDefaultCountryCodeWithPlus();
            }
        });

        msendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number;
                number=mgetPhoneNumber.getText().toString();
                if(number.isEmpty())
                {
                    Toast.makeText(getApplicationContext(),"Please Enter Your Number ..",Toast.LENGTH_LONG).show();

                }else if(number.length()<10){
                    Toast.makeText(getApplicationContext(),"Please Enter Correct Number ..",Toast.LENGTH_LONG).show();

                }else{
                    mprogressBar.setVisibility(View.VISIBLE);
                    phoneNumber=countryCode+number;

                    PhoneAuthOptions options =
                            PhoneAuthOptions.newBuilder(firebaseAuth)
                                    .setPhoneNumber(phoneNumber)       // Phone number to verify
                                    .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                                    .setActivity(MainActivity.this)                 // Activity (for callback binding)
                                    .setCallbacks(mCallsBack)          // OnVerificationStateChangedCallbacks
                                    .build();
                    PhoneAuthProvider.verifyPhoneNumber(options);

                }
            }
        });


        mCallsBack=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                //for auto completion of the code
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {

            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                Toast.makeText(getApplicationContext(), "Otp is sent", Toast.LENGTH_SHORT).show();
                mprogressBar.setVisibility(View.INVISIBLE);
                codeSent=s;

                Intent intent=new Intent(MainActivity.this,otpAuthentication.class);
                intent.putExtra("otp",codeSent);
                startActivity(intent);

            }
        };

    }

    @Override
    protected void onStart() {
        super.onStart();
        if(FirebaseAuth.getInstance().getCurrentUser()!=null){
            Intent intent=new Intent(MainActivity.this,chatActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }
}