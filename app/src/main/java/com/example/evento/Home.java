package com.example.evento;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.evento.Adapter.EventsAdapter;
import com.example.evento.Adapter.catagoryAdapter;
import com.example.evento.Fragments.info;
import com.example.evento.Interface.UpdateEventView;
import com.example.evento.Models.EventsModel;
import com.example.evento.Models.catagoryModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, UpdateEventView {
    RecyclerView catagory,events;
    EditText searchText;
    LinearLayout profileBtn;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    BottomNavigationView bottomNavigationView;
    SharedPreferences preferences ;
    EventsAdapter eventsAdapter;
    CircleImageView editprofilepic,homeprofilepic, cardprofilepic;
    String c_id , Prefuri;
    private  Uri imageUri=null;
    private  boolean isPicChange = false;

    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) { 
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        searchText = findViewById(R.id.searchInputBox);
        catagory = findViewById(R.id.recyclerView1);
        events = findViewById(R.id.recyclerView2);
        profileBtn = findViewById(R.id.profileIcon);
        homeprofilepic = findViewById(R.id.profilepic);

        drawerLayout = findViewById(R.id.drawerlayout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);
        bottomNavigationView = findViewById(R.id.bottom_nav);
        preferences = getSharedPreferences("LoginPref",MODE_PRIVATE);

         c_id =preferences.getString("CustomerId",null);
         Prefuri= preferences.getString("Uri",null);

        if (Prefuri==null){
            imageUri=null;
            homeprofilepic.setImageResource(R.drawable.baseline_person_24);

        }else {
            imageUri = Uri.parse(String.valueOf(Prefuri));
            homeprofilepic.setImageURI(imageUri);
        }
        Menu Bottommenu = bottomNavigationView.getMenu();
        MenuItem item = Bottommenu.findItem(R.id.bottom_home);
        item.setChecked(true);

        setSupportActionBar(toolbar);
        navigationView.bringToFront();
        bottomNavigationView.bringToFront();
        navigationView.setCheckedItem(R.id.nav_home);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                drawerLayout,toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId()==R.id.bottom_home){
                    Toast.makeText(Home.this, "Home", Toast.LENGTH_SHORT).show();
                }
                else if (item.getItemId()==R.id.bottom_calender) {
                    Intent i = new Intent(Home.this, Calender.class);
                    startActivity(i);
                    Toast.makeText(Home.this, "Calender", Toast.LENGTH_SHORT).show();
                } else if (item.getItemId()==R.id.bottm_profile) {
                    Intent i = new Intent(Home.this,UserProfile.class);
                    startActivity(i);
                    Toast.makeText(Home.this, "Profile", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });


        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               popUpDialog();
            }
        });


        ArrayList<catagoryModel> items = new ArrayList<catagoryModel>();
        items.add(new catagoryModel(R.drawable.flash, "My Feed"));
        items.add(new catagoryModel(R.drawable.cutlery, "Food"));
        items.add(new catagoryModel(R.drawable.musical_note, "concerts"));

        catagoryAdapter catagoryAdapter = new catagoryAdapter(items,this,this);
        catagory.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        catagory.setAdapter(catagoryAdapter);

        events.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

    }

    @Override
    public void callBack(int position, ArrayList<EventsModel> items) {
       eventsAdapter = new EventsAdapter(items,position);
       eventsAdapter.notifyDataSetChanged();
       events.setAdapter(eventsAdapter);
    }
    private void popUpDialog() {
        CardView cardView = findViewById(R.id.UserPeofileFragment);
        View view = LayoutInflater.from(Home.this).inflate(R.layout.fragment_card_user_profile,cardView);
        ImageView close = view.findViewById(R.id.closeBtn);
        AppCompatButton logout = view.findViewById(R.id.logoutBtn);
        AppCompatButton edit = view.findViewById(R.id.editBtn);
        TextView username = view.findViewById(R.id.username);
        TextView useremail = view.findViewById(R.id.emailid);
        cardprofilepic = view.findViewById(R.id.profilepic);
        if (imageUri!=null){
            cardprofilepic.setImageURI(imageUri);
        }else {
            cardprofilepic.setImageResource(R.drawable.baseline_person_24);
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(Home.this);
        builder.setView(view);
        final  AlertDialog alertDialog = builder.create();
         reference.orderByChild("c_id").equalTo(c_id).addListenerForSingleValueEvent(new ValueEventListener() {
             @Override
             public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String name = snapshot.child(c_id).child("name").getValue(String.class);
                    String email = snapshot.child(c_id).child("email").getValue(String.class);

                    username.setText(name);
                    useremail.setText(email);
             }
             @Override
             public void onCancelled(@NonNull DatabaseError error) {}
         });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Home.this, "Log out", Toast.LENGTH_LONG).show();
                preferences.edit().putBoolean("Login",false).apply();
                Intent i =new Intent(Home.this,Register.class);
                alertDialog.dismiss();
                startActivity(i);
            }
        });


        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                isPicChange=false;
                CardView cardView1 = findViewById(R.id.UserPeofileFragment);
                View view1 = LayoutInflater.from(Home.this).inflate(R.layout.editprofile_layout,cardView1);
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

                AlertDialog.Builder builder1 = new AlertDialog.Builder(Home.this);
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
                        if (editText.getText().toString().isEmpty() && isPicChange==false){
                            Toast.makeText(Home.this, "Nothing change ", Toast.LENGTH_LONG).show();
                            alertDialog1.dismiss();
                            alertDialog.dismiss();
                        }else{
                            String text = editText.getText().toString();
                            if (!text.isEmpty()){
                                reference.child(c_id).child("name").setValue(text);
                            }
                            if (isPicChange){
                                homeprofilepic.setImageURI(imageUri);
                            }
                            Toast.makeText(Home.this, "save successfully ", Toast.LENGTH_SHORT).show();
                            alertDialog1.dismiss();
                            alertDialog.dismiss();
                        }

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

         if (alertDialog.getWindow()!=null){
             alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
         }
         alertDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == RESULT_OK && data!=null){
            imageUri = data.getData();
            Picasso.get().load(imageUri).into(editprofilepic);
            String uri = imageUri.toString();
            preferences.edit().putString("Uri",uri).apply();
            isPicChange = true;
        }
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();
            finish();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.nav_home) {
        } else if (itemId == R.id.nav_profile) {
            Intent i = new Intent(Home.this, UserProfile.class);
            startActivity(i);
        } else if (itemId == R.id.nav_info) {
          getSupportFragmentManager().beginTransaction().add(R.id.drawerlayout,new info()).commit();
        } else if (itemId == R.id.nav_setting) {
            Intent i = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            i.setData(Uri.parse("package:com.example.evento"));
            startActivity(i);
            Toast.makeText(this, "Setting", Toast.LENGTH_SHORT).show();
        } else if (itemId == R.id.nav_logout) {
            Toast.makeText(Home.this, "Log out", Toast.LENGTH_LONG).show();
            preferences.edit().putBoolean("Login",false).apply();
            Intent i =new Intent(Home.this,Register.class);
            startActivity(i);
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


}