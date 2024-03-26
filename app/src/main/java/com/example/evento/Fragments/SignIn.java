package com.example.evento.Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.evento.Home;
import com.example.evento.HelperClass.LoginHelper;
import com.example.evento.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class SignIn extends Fragment {

    private static final int RC_SIGN_IN=101;
    AppCompatButton signup,ForgetPassword,login;
    ProgressBar progressBar;
    TextInputLayout Email,Pass;
    TextInputEditText email,password;
    CircleImageView googleLogin;
    ImageView facebookLogin;
   public String c_id;

     SharedPreferences preferences;
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
    FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_sign_in, container, false);
        signup=view.findViewById(R.id.sign_up_btn);
        ForgetPassword=view.findViewById(R.id.forget_password_btn);
        login=view.findViewById(R.id.login_btn);
        progressBar = view.findViewById(R.id.progress_bar);

        email=view.findViewById(R.id.Email);
        password=view.findViewById(R.id.password);

        Email=view.findViewById(R.id.field_email);
        Pass=view.findViewById(R.id.field_password);

        googleLogin = view.findViewById(R.id.login_google_btn);
        facebookLogin = view.findViewById(R.id.login_facebook_btn);

        mAuth=FirebaseAuth.getInstance();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this.getActivity(),gso);



        preferences = this.getActivity().getSharedPreferences("LoginPref",Context.MODE_PRIVATE);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!validateEmail() | !validatePassword()){
                    return;
                }
                else {
                    login.setVisibility(View.INVISIBLE);
                    progressBar.setVisibility(View.VISIBLE);
                    Email.setError(null);
                    Email.setErrorEnabled(false);
                    Pass.setError(null);
                    Pass.setErrorEnabled(false);
                       String UserEnterEmail = email.getText().toString();
                       String UserEnterPassword = password.getText().toString();

                       reference.addValueEventListener(new ValueEventListener() {
                           @Override
                           public void onDataChange(@NonNull DataSnapshot snapshot) {
                               Boolean getemail=false;
                               for (DataSnapshot data:
                                    snapshot.getChildren()) {
                                   if((data.child("email").getValue()).equals(UserEnterEmail)){
                                         getemail=true;
                                       if((data.child("password").getValue()).equals(UserEnterPassword)){
                                           login.setVisibility(View.VISIBLE);
                                           progressBar.setVisibility(View.INVISIBLE);
                                           c_id = data.child("c_id").getValue().toString();
                                           preferences.edit().putBoolean("Login",true).apply();
                                           preferences.edit().putString("CustomerId",c_id).apply();
                                             Intent i = new Intent(getActivity(),Home.class);
                                           Toast.makeText(getActivity(), "Login Successfully", Toast.LENGTH_LONG).show();
                                             startActivity(i);
                                        }else {
                                           Pass.setError("Wrong Password");
                                           login.setVisibility(View.VISIBLE);
                                           progressBar.setVisibility(View.INVISIBLE);
                                       }
                                   }
                               }if(!getemail){
                                   login.setVisibility(View.VISIBLE);
                                   progressBar.setVisibility(View.INVISIBLE);
                                   Email.setError("Email Not Found");
                               }
                           }

                           @Override
                           public void onCancelled(@NonNull DatabaseError error) {}
                       });
                }
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.Register,new SignUp()).commit();
            }
        });

        ForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.Register,new ForgetPassword()).commit();
            }
        });

        googleLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                login.setVisibility(View.INVISIBLE);
//                progressBar.setVisibility(View.VISIBLE);
                signIn();
            }
        });

         return view;
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent,100);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

            if (requestCode == 100  && resultCode == getActivity().RESULT_OK && data.getData()!=null ) {
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                try {
                    GoogleSignInAccount account = task.getResult(ApiException.class);
                    updateUI(account);
                } catch (ApiException e) {
                    Toast.makeText(getActivity(), "Error : "+e.getMessage(), Toast.LENGTH_SHORT).show();
              }
            }

    }

    private void updateUI(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(),null);
        mAuth=FirebaseAuth.getInstance();
        mAuth.signInWithCredential(credential).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    FirebaseUser user = mAuth.getCurrentUser();
                    Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();
                    UserDataFromGoogleToDB(user);
                }
               else {
                    Toast.makeText(getActivity(), "Login Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void UserDataFromGoogleToDB(FirebaseUser user) {
        preferences.edit().putBoolean("Login",true).apply();
        String UserName,ProfileImg,Email,Phone,CustomerId;
         UserName = user.getDisplayName();
         Email = user.getEmail();
         Phone = user.getPhoneNumber();
         ProfileImg = user.getPhotoUrl().toString();
         CustomerId=UserName.substring(0,4)+""+Phone.substring(6,10);

        LoginHelper loginHelper = new LoginHelper(CustomerId,UserName,Phone,Email,null,ProfileImg);
        reference.child(CustomerId).setValue(loginHelper);

        preferences.edit().putBoolean("Login",true).apply();
        preferences.edit().putString("CustomerId",CustomerId).apply();
        Intent i = new Intent(getActivity(),Home.class);
        i.putExtra("c_id",CustomerId);
        startActivity(i);

    }


    private boolean validateEmail() {
        String val = email.getText().toString();
        String emailpattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";


        if (val.isEmpty()) {
            Email.setError("Field can't be empty");
            return false;
        } else if (!val.matches(emailpattern)) {
            Email.setError("Invalide Email Address");
            return false;
        } else {
            Email.setError(null);
             Email.setErrorEnabled(false);
            return true;
        }
    }
    private boolean validatePassword() {
        String val = password.getText().toString();

        if (val.isEmpty()) {
            Pass.setError("Field can't be empty");
            return false;
        }else {
            Pass.setError(null);
            Pass.setErrorEnabled(false);
            return true;
        }
    }


}