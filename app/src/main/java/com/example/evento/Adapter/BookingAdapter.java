package com.example.evento.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.evento.HelperClass.BookingHelper;
import com.example.evento.R;

import java.util.ArrayList;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.BookingViewHolder>{

     private ArrayList<BookingHelper> Booklist;

      public BookingAdapter(ArrayList<BookingHelper> Booklist){
          this.Booklist = Booklist;
      }

    @NonNull
    @Override
    public BookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_card_layout,parent,false);
        BookingViewHolder bookingViewHolder = new BookingViewHolder(view);
        return bookingViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull BookingViewHolder holder, int position) {
          BookingHelper bookingHelper = Booklist.get(position);
          holder.from.setText("From : "+bookingHelper.getFrom());
          holder.to.setText("To : "+bookingHelper.getTo());
          holder.heading.setText(bookingHelper.getHeading());
          holder.amount.setText("Paid : "+bookingHelper.getPaid_amount());

    }

    @Override
    public int getItemCount() {
        return Booklist.size();
    }

    public static class BookingViewHolder extends RecyclerView.ViewHolder{
        TextView from,to,heading,amount;
        public BookingViewHolder(@NonNull View itemView) {
            super(itemView);
            from = itemView.findViewById(R.id.from);
            to = itemView.findViewById(R.id.to);
            heading = itemView.findViewById(R.id.heading);
            amount = itemView.findViewById(R.id.amount);
        }
    }
}
