package com.example.evento.Fragments;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.evento.R;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

public class ForgetPassword extends Fragment {

    EditText PhoneNumber;
    AppCompatButton submit;
    ProgressBar progressBar;

    private Boolean IsFromDB=false;
    private  String  phone;

    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_forget_password, container, false);
        PhoneNumber = view.findViewById(R.id.phone);
        submit = view.findViewById(R.id.submit_btn);
        progressBar = view.findViewById(R.id.progress_bar);


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validateNumber()) {
                    return;
                }

                phone=  PhoneNumber.getText().toString();
                reference.orderByChild("phone").equalTo(phone).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            PhoneNumber.setError(null);
                            progressBar.setVisibility(View.VISIBLE);
                            submit.setVisibility(View.INVISIBLE);
                            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                                    "+91" +phone,
                                    60,
                                    TimeUnit.SECONDS,
                                    getActivity(),
                                    new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                                        @Override
                                        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                            progressBar.setVisibility(View.INVISIBLE);
                                            submit.setVisibility(View.VISIBLE);
                                        }

                                        @Override
                                        public void onVerificationFailed(@NonNull FirebaseException e) {
                                            progressBar.setVisibility(View.INVISIBLE);
                                            submit.setVisibility(View.VISIBLE);
                                            Toast.makeText(getActivity(), "error : "+e.getMessage(), Toast.LENGTH_LONG).show();
                                        }

                                        @Override
                                        public void onCodeSent(@NonNull String str, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                            super.onCodeSent(str, forceResendingToken);
                                            progressBar.setVisibility(View.INVISIBLE);
                                            submit.setVisibility(View.VISIBLE);
                                            OTPverify otp = new OTPverify();
                                            Bundle bundle = new Bundle();
                                            bundle.putString("Phone_Key",phone);
                                            bundle.putString("OTP_Key",str);
                                            otp.setArguments(bundle);
                                            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.Register,otp).commit();
                                        }
                                    }
                            );
                        }
                        else {
                            PhoneNumber.setError("Enter a valid number");
                            progressBar.setVisibility(View.VISIBLE);
                            submit.setVisibility(View.INVISIBLE);
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });

        return view;
    }

    private boolean validateNumber() {
        if (!PhoneNumber.getText().toString().trim().isEmpty()) {
            if ((PhoneNumber.getText().toString().trim()).length() == 10) {
                    return true;
            }
        }
      else{
            PhoneNumber.setError("Enter a valid number");
            return false;
        }
      return false;
    }


}