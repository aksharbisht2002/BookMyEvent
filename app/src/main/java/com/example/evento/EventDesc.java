package com.example.evento;

import androidx.appcompat.app.AppCompatActivity;
import java.util.*;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.evento.Fragments.CheckOut;
import com.example.evento.Fragments.EventRestaurentCheckout;

import de.hdodenhof.circleimageview.CircleImageView;

public class EventDesc extends AppCompatActivity {

    private String heading,location,type;
    private int categoryPos,eventPos;
    TextView tHeading,tLocation,tType,day,month,aboutevent;
    LinearLayout datedisplay;

    ImageView backgroundImg,backBtn;

    CircleImageView calenderBtn;
    Button bookBtn;
    ListView desclist;

    private String[] about,price,desc;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_desc);
        Bundle bundle = getIntent().getExtras();
        heading = bundle.getString("heading");
        location = bundle.getString("location");
        type = bundle.getString("type");
        categoryPos = getIntent().getIntExtra("categoryPos",0);
        eventPos = getIntent().getIntExtra("eventPos",0);

        backgroundImg = findViewById(R.id.backgroundimg);
        tHeading = findViewById(R.id.heading);
        tLocation = findViewById(R.id.location);
        tType = findViewById(R.id.type);
        bookBtn = findViewById(R.id.bookBtn);
        datedisplay = findViewById(R.id.dateDisplay);
        day = findViewById(R.id.day);
        month = findViewById(R.id.month);
        backBtn = findViewById(R.id.backBtn);
        aboutevent = findViewById(R.id.aboutevent);
        desclist = findViewById(R.id.desclist);

        if (categoryPos == 0 ){
            about = getResources().getStringArray(R.array.feed_about);
            price = getResources().getStringArray(R.array.feed_price);
            if (eventPos ==0){
                desc = getResources().getStringArray(R.array.feed_one_desc);
            } else if (eventPos == 1) {
                desc = getResources().getStringArray(R.array.feed_two_desc);
            }else if (eventPos == 2) {
                desc = getResources().getStringArray(R.array.feed_three_desc);
            }else {
                desc = getResources().getStringArray(R.array.feed_four_desc);
            }
        }
        if (categoryPos == 1 ){
            about = getResources().getStringArray(R.array.food_about);
            price = getResources().getStringArray(R.array.food_price);
            if (eventPos ==0){
                desc = getResources().getStringArray(R.array.food_one_desc);
            } else if (eventPos == 1) {
                desc = getResources().getStringArray(R.array.food_two_desc);
            }else if (eventPos == 2) {
                desc = getResources().getStringArray(R.array.food_three_desc);
            }else {
                desc = getResources().getStringArray(R.array.food_four_desc);
            }
        }
        if (categoryPos == 2 ){
            about = getResources().getStringArray(R.array.concert_about);
            price = getResources().getStringArray(R.array.concert_price);
            if (eventPos ==0){
                desc = getResources().getStringArray(R.array.concert_one_desc);
            } else if (eventPos == 1) {
                desc = getResources().getStringArray(R.array.concert_two_desc);
            }else if (eventPos == 2) {
                desc = getResources().getStringArray(R.array.concert_three_desc);
            }else {
                desc = getResources().getStringArray(R.array.concert_four_desc);
            }
        }

        datedisplay.setVisibility(View.GONE);

        backgroundImg.setImageResource(getIntent().getIntExtra("Bkimg",0));
        tHeading.setText(heading);
        tLocation.setText(location);
        tType.setText(price[eventPos]);
        aboutevent.setText(about[eventPos]);

        desclist.setAdapter(new ArrayAdapter<String>(this,R.layout.listview_textview,R.id.textview,desc));

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(EventDesc.this,Home.class));
            }
        });
        bookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckOut checkOut = new CheckOut();
                EventRestaurentCheckout eventRestaurentCheckout = new EventRestaurentCheckout();
                Bundle b = new Bundle();
                b.putString("heading",heading);
                b.putString("price",price[eventPos]);
                checkOut.setArguments(b);
                eventRestaurentCheckout.setArguments(b);
                if (categoryPos!=0){
                    getSupportFragmentManager().beginTransaction().add(R.id.event_desc,eventRestaurentCheckout).commit();
                }else {
                    getSupportFragmentManager().beginTransaction().add(R.id.event_desc,checkOut).commit();
                }
            }
        });






    }
}