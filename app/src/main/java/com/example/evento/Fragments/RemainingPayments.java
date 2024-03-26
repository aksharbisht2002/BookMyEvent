package com.example.evento.Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextClock;
import android.widget.TextView;

import com.example.evento.Adapter.BookingAdapter;
import com.example.evento.Adapter.RemainingPaymentAdapter;
import com.example.evento.Calender;
import com.example.evento.HelperClass.BookingHelper;
import com.example.evento.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RemainingPayments extends Fragment {

    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Bookings");
    SharedPreferences preferences;
    RecyclerView rv;
    ImageView backbtn;
    TextView text;
    String CustomerId;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_remaining_payments, container, false);
        preferences = getActivity().getSharedPreferences("LoginPref", Context.MODE_PRIVATE);
         CustomerId = preferences.getString("CustomerId",null);

         rv=view.findViewById(R.id.rv);
         backbtn=view.findViewById(R.id.backBtn);
         text = view.findViewById(R.id.text);


        reference.child(CustomerId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()){
                    text.setVisibility(View.VISIBLE);
                    rv.setVisibility(View.INVISIBLE);
                }
                else {
                    text.setVisibility(View.INVISIBLE);
                    rv.setVisibility(View.VISIBLE);
                    ArrayList<BookingHelper>  Booklist = new ArrayList<BookingHelper>();
                    for (DataSnapshot data:
                            snapshot.getChildren()) {
                        BookingHelper bookingHelper = data.getValue(BookingHelper.class);
                        Booklist.add(bookingHelper);
                    }
                    RemainingPaymentAdapter adapter = new RemainingPaymentAdapter(Booklist,getActivity());
                    rv.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
                    adapter.notifyDataSetChanged();
                    rv.setAdapter(adapter);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = getParentFragmentManager().findFragmentById(R.id.linearLayout5);
                getActivity().getSupportFragmentManager().beginTransaction().remove(fragment).commit();
            }
        });




        return view;
    }

}