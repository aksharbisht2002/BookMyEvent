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
import android.widget.Toast;

import com.example.evento.HelperClass.LoginHelper;
import com.example.evento.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignUp extends Fragment {
    Button loginbtn;
    AppCompatButton SignUpbtn;
    TextInputEditText name, phone, email, password;
    TextInputLayout fieldname, fieldphone,fieldemail,fieldpass;

    FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
    FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
    DatabaseReference reference=firebaseDatabase.getReference("Users");


    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        loginbtn = view.findViewById(R.id.login_btn);
        SignUpbtn = view.findViewById(R.id.sign_up_btn);

        name = view.findViewById(R.id.name);
        phone = view.findViewById(R.id.phone);
        email = view.findViewById(R.id.email);
        password = view.findViewById(R.id.password);

        fieldname = view.findViewById(R.id.field_name);
        fieldphone = view.findViewById(R.id.field_phone);
        fieldemail = view.findViewById(R.id.field_email);
        fieldpass = view.findViewById(R.id.field_password);




        SignUpbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registeruser(view);
            }
        });
        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.Register, new SignIn()).commit();
            }
        });


        return view;
    }


    public void registeruser(View view){
        if(!validateName() | !validatePhone() | !validateEmail() | !validatePassword()){
            return;
        }
        String Email = email.getText().toString();
        reference.orderByChild("email").equalTo(Email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    fieldemail.setError("Email Already Exist");
                }else {
                    fieldemail.setError(null);
                    fieldemail.setErrorEnabled(false);
                    String Name = name.getText().toString();
                    String Phone = phone.getText().toString();
                    String Email = email.getText().toString();
                    String PassWord = password.getText().toString();
                    String CustomerId=Name.substring(0,4)+""+Phone.substring(6,10);

                    LoginHelper loginHelper = new LoginHelper(CustomerId,Name,Phone,Email,PassWord,null);
                    reference.child(CustomerId).setValue(loginHelper);
                    Toast.makeText(getActivity(),"Registration Complete",Toast.LENGTH_LONG).show();
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.Register, new SignIn()).commit();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }


    private boolean validateName() {
        String val = name.getText().toString();
        if (val.isEmpty()) {
            fieldname.setError("Field can't be empty");
            return false;
        } else if (val.length()< 4) {
            fieldname.setError("Atleast 4 Characters");
            return false;

        } else {
            fieldname.setError(null);
            fieldname.setErrorEnabled(false);
            return true;
        }
    }
    private boolean validatePhone() {
        String val = phone.getText().toString();
        String noWhiteSpace = "\\A\\w{4,20}\\z";
        if (val.isEmpty()) {
            fieldphone.setError("Field can't be empty");
            return false;
        } else if (val.length()!=10) {
            fieldphone.setError("Enter a valid number");
             return false;
        } else if (!val.matches(noWhiteSpace)) {
            fieldphone.setError("WhiteSpace Not Allow");
            return  false;
        } else {
            fieldphone.setError(null);
            fieldphone.setErrorEnabled(false);
            return true;
        }
    }
    private boolean validateEmail() {
        String val = email.getText().toString();
        String emailpattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (val.isEmpty()) {
            fieldemail.setError("Field can't be empty");
            return false;
        } else if (!val.matches(emailpattern)) {
            fieldemail.setError("Invalide Email Address");
            return false;
        } else {
            fieldemail.setError(null);
            fieldemail.setErrorEnabled(false);
            return true;
        }
    }
    private boolean validatePassword() {
        String val = password.getText().toString();
        String passwordVerification ="^" +
                //"(?=.*[0-9])" +         //at least 1 digit
                //"(?=.*[a-z])" +         //at least 1 lower case letter
                //"(?=.*[A-Z])" +         //at least 1 upper case letter
                "(?=.*[a-zA-Z])" +      //any letter
                "(?=.*[@#$%^&+=])" +    //at least 1 special character
                "(?=\\S+$)" +           //no white spaces
                ".{4,}" +               //at least 4 characters
                "$";
        if (val.isEmpty()) {
            fieldpass.setError("Field can't be empty");
            return false;
        } else if (!val.matches(passwordVerification)) {
            fieldpass.setError("Password is weak");
            return false;
        }else {
            fieldpass.setError(null);
            fieldpass.setErrorEnabled(false);
            return true;
        }
    }



}