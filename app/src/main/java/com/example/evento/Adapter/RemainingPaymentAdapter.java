package com.example.evento.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.evento.Fragments.RemainingPayments;
import com.example.evento.HelperClass.BookingHelper;
import com.example.evento.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class RemainingPaymentAdapter extends RecyclerView.Adapter<RemainingPaymentAdapter.CardViewHolder> {
    ArrayList<BookingHelper> items;
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Bookings");
    SharedPreferences preferences;
    Activity activity;
    public RemainingPaymentAdapter(ArrayList<BookingHelper> items, Activity activity){
        this.items = items;
        this.activity = activity;
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.remaining_payment_card,parent,false);
        CardViewHolder viewHolder = new CardViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, @SuppressLint("RecyclerView") int position) {
        BookingHelper helper = items.get(position);
        preferences = activity.getSharedPreferences("LoginPref", Context.MODE_PRIVATE);
       String CustomerId = preferences.getString("CustomerId",null);
        holder.heading.setText(helper.getHeading());
        holder.from.setText("From : "+helper.getFrom());
        holder.to.setText("To : "+helper.getTo());
        holder.paid.setText("Paid : "+helper.getPaid_amount());
        holder.remaining.setText("Remaining : "+helper.getRemaining_amount());
        holder.pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                items.remove(position);
                reference.child(CustomerId).child(helper.getHeading()).setValue(null);
                notifyDataSetChanged();
            }
        });

    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    public class CardViewHolder extends RecyclerView.ViewHolder{
         TextView heading,from,to,paid,remaining;
         Button pay;
        public CardViewHolder(@NonNull View itemView) {
            super(itemView);
            heading = itemView.findViewById(R.id.heading);
            from = itemView.findViewById(R.id.from);
            to = itemView.findViewById(R.id.to);
            paid = itemView.findViewById(R.id.paid);
            remaining = itemView.findViewById(R.id.reaming);
            pay = itemView.findViewById(R.id.pay);
        }
    }

}

