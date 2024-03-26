package com.example.evento.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.evento.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.nio.file.LinkOption;
import java.util.concurrent.TimeUnit;

public class OTPverify extends Fragment {

  TextInputEditText[] digit = new TextInputEditText[6];
  String OTPFromDB;
  TextView resend_otp_btn,displayNumber;
  AppCompatButton submit;
  ProgressBar progressBar;

   public String email,c_id,phone;
  DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_o_t_pverify, container, false);
        Bundle b = new Bundle();
        digit[0]=view.findViewById(R.id.otp_Digit1);
        digit[1]=view.findViewById(R.id.otp_Digit2);
        digit[2]=view.findViewById(R.id.otp_Digit3);
        digit[3]=view.findViewById(R.id.otp_Digit4);
        digit[4]=view.findViewById(R.id.otp_Digit5);
        digit[5]=view.findViewById(R.id.otp_Digit6);

        progressBar = view.findViewById(R.id.progress_bar);

        submit = view.findViewById(R.id.submit_btn);
        resend_otp_btn=view.findViewById(R.id.resend_otp_btn);
        displayNumber = view.findViewById(R.id.displaynumber);
        phone=getArguments().getString("Phone_Key");
        OTPFromDB = getArguments().getString("OTP_Key");
        displayNumber.setText("+91 "+phone);

        submit.setOnClickListener(new View.OnClickListener() {
                                      @Override
                                      public void onClick(View view) {
                                          String UserOTP = digit[0].getText().toString() +
                                                  digit[1].getText().toString() +
                                                  digit[2].getText().toString() +
                                                  digit[3].getText().toString() +
                                                  digit[4].getText().toString() +
                                                  digit[5].getText().toString();

                                          if (OTPFromDB != null) {
                                              submit.setVisibility(View.INVISIBLE);
                                              progressBar.setVisibility(View.VISIBLE);

                                              PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(
                                                      OTPFromDB, UserOTP
                                              );
                                              FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential)
                                                      .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                          @Override
                                                          public void onComplete(@NonNull Task<AuthResult> task) {
                                                              submit.setVisibility(View.VISIBLE);
                                                              progressBar.setVisibility(View.INVISIBLE);
                                                              if (task.isSuccessful()) {
                                                                  getEmailandCid();
                                                                   }else{
                                                                  Toast.makeText(getActivity(), " ERROR ", Toast.LENGTH_SHORT).show();
                                                              }
                                                          }
                                                      });
                                          }
                                      }
                                  });


                    resend_otp_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                                    "+91" + getArguments().getString("Phone_Key"),
                                    60,
                                    TimeUnit.SECONDS,
                                    getActivity(),
                                    new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                                        @Override
                                        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                        }

                                        @Override
                                        public void onVerificationFailed(@NonNull FirebaseException e) {
                                            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                                        }

                                        @Override
                                        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                            super.onCodeSent(s, forceResendingToken);
                                            OTPFromDB = s;
                                            Toast.makeText(getActivity(), "OTP Send Successfully", Toast.LENGTH_LONG).show();
                                        }
                                    }
                            );
                        }
                    });

                    ChangeFocus();

                    return view;
        }

  private void getEmailandCid(){

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data:
                     snapshot.getChildren()) {
                    if((data.child("phone").getValue()).equals(phone)){
                        c_id = data.child("c_id").getValue().toString();
                        email=data.child("email").getValue().toString();
                        NewPassword password = new NewPassword();
                        Bundle bundle = new Bundle();
                        bundle.putString("Phone_Key",phone);
                        bundle.putString("Cid_Key",c_id);
                        bundle.putString("Email_Key",email);
                        password.setArguments(bundle);
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.Register,password).commit();


                    }
                }


            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
  }

    private void ChangeFocus () {
        digit[0].addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().trim().isEmpty()) {
                    digit[1].requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        digit[1].addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().trim().isEmpty()) {
                    digit[2].requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        digit[2].addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().trim().isEmpty()) {
                    digit[3].requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        digit[3].addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().trim().isEmpty()) {
                    digit[4].requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        digit[4].addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().trim().isEmpty()) {
                    digit[5].requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

    }
}
