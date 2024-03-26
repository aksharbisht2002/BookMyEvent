package com.example.evento;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.evento.Fragments.RemainingPayments;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;


public class UserProfile extends AppCompatActivity {
    DrawerLayout drawerLayout;
    ImageView backbtn,editBtn;
    TextView switchAccount,LogOut;
    TextView username, useremail;
    BottomNavigationView bottomNavigationView;
    CircleImageView editprofilepic,homeProfilePic;
    SharedPreferences preferences;
    ConstraintLayout payment,booking,security;

    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
    String CustomerId,prefUri;
    Uri imageUri=null;
    boolean isPicChange=false;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        backbtn = findViewById(R.id.backBtn);
        editBtn = findViewById(R.id.editBtn);
        switchAccount = findViewById(R.id.switchAccountBtn);
        LogOut = findViewById(R.id.logoutBtn);
        bottomNavigationView =findViewById(R.id.bottom_nav);
        homeProfilePic = findViewById(R.id.profilepic);
        payment = findViewById(R.id.payment);
        booking = findViewById(R.id.booking);
        security = findViewById(R.id.security);
        username =findViewById(R.id.UserName);
        useremail=findViewById(R.id.UserEmail);

        preferences = getSharedPreferences("LoginPref",MODE_PRIVATE);

        CustomerId = preferences.getString("CustomerId",null);
        prefUri= preferences.getString("Uri",null);
        if (prefUri==null){
            imageUri = null;
            homeProfilePic.setImageResource(R.drawable.baseline_person_24);
        }
        else {
            imageUri = Uri.parse(String.valueOf(prefUri));
            homeProfilePic.setImageURI(imageUri);
        }
        Menu menu = bottomNavigationView.getMenu();
        MenuItem item = menu.findItem(R.id.bottm_profile);
        item.setChecked(true);

        reference.orderByChild("c_id").equalTo(CustomerId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name = snapshot.child(CustomerId).child("name").getValue(String.class);
                String email = snapshot.child(CustomerId).child("email").getValue(String.class);

                username.setText(name);
                useremail.setText(email);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId()==R.id.bottom_home){
                    Intent i = new Intent(UserProfile.this,Home.class);
                    startActivity(i);
                    Toast.makeText(UserProfile.this, "Home", Toast.LENGTH_SHORT).show();
                }
                else if (item.getItemId()==R.id.bottom_calender) {
                    Intent i = new Intent(UserProfile.this, Calender.class);
                    startActivity(i);
                    Toast.makeText(UserProfile.this, "Calender", Toast.LENGTH_SHORT).show();
                } else if (item.getItemId()==R.id.bottm_profile) {

                    Toast.makeText(UserProfile.this, "Profile", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isPicChange=false;
                CardView cardView = findViewById(R.id.UserPeofileFragment);
                View view1 = LayoutInflater.from(UserProfile.this).inflate(R.layout.editprofile_layout,cardView);
                ImageView close = view1.findViewById(R.id.closeBtn);
                AppCompatButton save= view1.findViewById(R.id.saveBtn);
                ImageView editPic = view1.findViewById(R.id.changepicBtn);
                EditText editText = view1.findViewById(R.id.username);
                 editprofilepic = view1.findViewById(R.id.profilepic);


                if (imageUri==null){
                    editprofilepic.setImageResource(R.drawable.baseline_person_24);
                }
                else {
                    editprofilepic.setImageURI(imageUri);
                }

                AlertDialog.Builder builder1 = new AlertDialog.Builder(UserProfile.this);
                builder1.setView(view1);
                final  AlertDialog alertDialog1 = builder1.create();
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog1.dismiss();
                    }
                });
                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        editText.setError(null);
                        String text = editText.getText().toString();
                        if (!editText.getText().toString().isEmpty()){
                            reference.child(CustomerId).child("name").setValue(text);
                        }
                        if (isPicChange){
                            homeProfilePic.setImageURI(imageUri);
                        }
                        Toast.makeText(UserProfile.this, "save successfully", Toast.LENGTH_SHORT).show();
                        alertDialog1.dismiss();
                    }
                });
                editPic.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent();
                        i.setType("image/*");
                        i.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(i,0);
                    }
                });
                if (alertDialog1.getWindow()!=null){
                    alertDialog1.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                }
                alertDialog1.show();
            }
        });

        switchAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(UserProfile.this, "Log out", Toast.LENGTH_LONG).show();
                preferences.edit().putBoolean("Login",false).apply();
                Intent i =new Intent(UserProfile.this,Register.class);
                startActivity(i);
            }
        });

        LogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(UserProfile.this, "Log out", Toast.LENGTH_LONG).show();
                preferences.edit().putBoolean("Login",false).apply();
                Intent i =new Intent(UserProfile.this,Register.class);
                startActivity(i);
            }
        });

        payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportFragmentManager().beginTransaction().add(R.id.linearLayout5,new RemainingPayments()).commit();
            }
        });
        booking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UserProfile.this,Calender.class));
            }
        });
        security.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent i = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
               i.setData(Uri.parse("package:com.example.evento"));
               startActivity(i);
            }
        });

    }
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == RESULT_OK && data!=null){
            imageUri = data.getData();
            editprofilepic.setImageURI(imageUri);
            isPicChange = true;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}