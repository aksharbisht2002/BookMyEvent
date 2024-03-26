package com.example.evento.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.evento.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class NewPassword extends Fragment {

   TextInputLayout newPasswordField,confirmPasswordField;
   TextInputEditText newPassword,confirmPassword;
   AppCompatButton submit;
   ProgressBar progressBar,progressBar2;
   LinearLayout layout;
   TextView emailtextview;
   String c_id,email,phone;
    String val;
   DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");


    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_new_password, container, false);

        submit=view.findViewById(R.id.submit_btn);
        progressBar=view.findViewById(R.id.progress_bar);

        newPasswordField= view.findViewById(R.id.field_newpassword);
        confirmPasswordField = view.findViewById(R.id.field_confirmpassword);

        newPassword = view.findViewById(R.id.newpassword);
        confirmPassword = view.findViewById(R.id.confirmpassword);
        emailtextview=  view.findViewById(R.id.changepassForMail);

        phone=getArguments().getString("Phone_Key");
        c_id=getArguments().getString("Cid_Key");
        email = getArguments().getString("Email_Key");

          emailtextview.setText(email);

      submit.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              if(!ValidCheck()){
                  return;
              }
              submit.setVisibility(View.INVISIBLE);
              progressBar.setVisibility(View.VISIBLE);

               val = newPassword.getText().toString().trim();

              reference.orderByChild("c_id").equalTo(c_id).addListenerForSingleValueEvent(new ValueEventListener() {
                  @Override
                  public void onDataChange(@NonNull DataSnapshot snapshot) {
                      reference.child(c_id).child("password").setValue(val);
                  }

                  @Override
                  public void onCancelled(@NonNull DatabaseError error) {

                  }
              });
              Toast.makeText(getActivity(), "Password Reset Successfully", Toast.LENGTH_LONG).show();
              getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.Register,new SignIn()).commit();
          }
      });

        return view;
    }

    private boolean ValidCheck() {
        String val1 = newPassword.getText().toString().trim();
        String val2 = confirmPassword.getText().toString().trim();
        if(val1.isEmpty() | val2.isEmpty()){
            newPasswordField.setError("can't be empty");
            confirmPasswordField.setError("can't be empty");
            return false;
        }
        else if(!val1.equals(val2)){
            confirmPasswordField.setError("Field didn't match");
            return false;
        }
        else {
            newPasswordField.setError(null);
            newPasswordField.setErrorEnabled(false);
            confirmPasswordField.setError(null);
            confirmPasswordField.setErrorEnabled(false);
            return true;
        }

    }


}