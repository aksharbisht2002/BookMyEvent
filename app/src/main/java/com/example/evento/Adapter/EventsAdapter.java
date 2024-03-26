package com.example.evento.Adapter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.evento.EventDesc;
import com.example.evento.Models.EventsModel;
import com.example.evento.R;

import java.util.ArrayList;


public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.EventCardViewHolder> {

    ArrayList<EventsModel> items;
    private int pos ;

    public EventsAdapter(ArrayList<EventsModel> items, int position) {
        this.items = items;
         pos = position;
    }
    @NonNull
    @Override
    public EventCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.events,parent,false);
        EventCardViewHolder viewHolder = new EventCardViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull EventCardViewHolder holder, @SuppressLint("RecyclerView") int position) {
        EventsModel currentItem = items.get(position);
        holder.backgroundImg.setImageResource(currentItem.getBackgroungImg());
        holder.heading.setText(currentItem.getHeading());
        holder.location.setText(currentItem.getLocation());
        holder.type.setText(currentItem.getType());
        holder.premiumImg.setImageResource(currentItem.getPremiumImg());

        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), EventDesc.class);
                i.putExtra("Bkimg",currentItem.getBackgroungImg());
                i.putExtra("heading",currentItem.getHeading());
                i.putExtra("type",currentItem.getType());
                i.putExtra("location",currentItem.getLocation());
                i.putExtra("categoryPos",pos);
                i.putExtra("eventPos",position);
                view.getContext().startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class EventCardViewHolder extends RecyclerView.ViewHolder{
           ImageView backgroundImg,premiumImg;
           TextView  heading,location,type;
           CardView card;
           ConstraintLayout desc;
        public EventCardViewHolder(@NonNull View itemView) {
            super(itemView);
            backgroundImg = itemView.findViewById(R.id.overLayout);
            premiumImg = itemView.findViewById(R.id.premiumimg);
            heading = itemView.findViewById(R.id.event_heading);
            location = itemView.findViewById(R.id.event_location);
            type = itemView.findViewById(R.id.event_type);
            card = itemView.findViewById(R.id.events_card_layout);
            desc = itemView.findViewById(R.id.desc);
        }
    }
}
