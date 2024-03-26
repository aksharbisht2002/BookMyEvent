package com.example.evento;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CalendarView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.evento.Adapter.BookingAdapter;
import com.example.evento.HelperClass.BookingHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Calender extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    CalendarView calendarView;
    RecyclerView rv;
    TextView textView;
    ProgressBar progressBar;
    SharedPreferences preferences;

    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Bookings");

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calender);
        bottomNavigationView = findViewById(R.id.bottom_nav);
        calendarView = findViewById(R.id.calendarView);
        rv=findViewById(R.id.rv);
        textView = findViewById(R.id.nodata);
        progressBar = findViewById(R.id.progress_bar);

        preferences = getSharedPreferences("LoginPref",MODE_PRIVATE);
        String CustomerId = preferences.getString("CustomerId",null);

        Menu Bottommenu = bottomNavigationView.getMenu();
        MenuItem item = Bottommenu.findItem(R.id.bottom_calender);
        item.setChecked(true);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if (item.getItemId()==R.id.bottom_home){
                    Intent i = new Intent(Calender.this, Home.class);
                    startActivity(i);
                    Toast.makeText(Calender.this, "Home", Toast.LENGTH_SHORT).show();
                }
                else if (item.getItemId()==R.id.bottom_calender) {
                    Toast.makeText(Calender.this, "Calender", Toast.LENGTH_SHORT).show();
                } else if (item.getItemId()==R.id.bottm_profile) {
                    Intent i = new Intent(Calender.this,UserProfile.class);
                    startActivity(i);
                    Toast.makeText(Calender.this, "Profile", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });



         progressBar.setVisibility(View.VISIBLE);
        reference.child(CustomerId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()){
                    textView.setVisibility(View.VISIBLE);
                    rv.setVisibility(View.INVISIBLE);
                    progressBar.setVisibility(View.INVISIBLE);
                }
                else {
                    progressBar.setVisibility(View.INVISIBLE);
                    textView.setVisibility(View.INVISIBLE);
                    rv.setVisibility(View.VISIBLE);
                  ArrayList<BookingHelper>  Booklist = new ArrayList<BookingHelper>();
                    for (DataSnapshot data:
                         snapshot.getChildren()) {
                        BookingHelper bookingHelper = data.getValue(BookingHelper.class);
                        Booklist.add(bookingHelper);
                    }
                    BookingAdapter adapter = new BookingAdapter(Booklist);
                    rv.setLayoutManager(new LinearLayoutManager(Calender.this,LinearLayoutManager.VERTICAL,false));
                    rv.setAdapter(adapter);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });


    }
}