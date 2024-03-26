package com.example.evento.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.evento.Interface.UpdateEventView;
import com.example.evento.Models.EventsModel;
import com.example.evento.Models.catagoryModel;
import com.example.evento.R;

import java.util.ArrayList;

public class catagoryAdapter extends RecyclerView.Adapter<catagoryAdapter.catagoryViewholdere>{
    private ArrayList<catagoryModel> items;
    int index=-1;
    UpdateEventView updateEventView;
    Activity activity;
    boolean check=true,select=true;
    public catagoryAdapter(ArrayList<catagoryModel> items,Activity activity,UpdateEventView updateEventView) {
        this.items = items;
        this.activity = activity;
        this.updateEventView = updateEventView;
    }
    @NonNull
    @Override
    public catagoryViewholdere onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_catagory_layout,parent,false);
        catagoryViewholdere catagoryViewholdere = new catagoryViewholdere(view);
        return catagoryViewholdere;
    }

    @Override
    public void onBindViewHolder(@NonNull catagoryViewholdere holder, @SuppressLint("RecyclerView") int position) {
        catagoryModel currentItem = items.get(position);
        holder.image.setImageResource(currentItem.getImage());
        holder.text.setText(currentItem.getText());
        ArrayList<EventsModel> item = new ArrayList<>();
        if (check){

            item.add(new EventsModel(R.drawable.event3, R.drawable.cardbox1, "Hudson Lane Bar", "Dawarka", "Bar/Restront"));
            item.add(new EventsModel(R.drawable.event2, R.drawable.crown, "Oak Yard", "Uttrakhand", "Outdoor Wedding"));
            item.add(new EventsModel(R.drawable.event4, R.drawable.cardbox1, "Bisht Hall", "Gurugram", "Hall"));
            item.add(new EventsModel(R.drawable.event1, R.drawable.cardbox1, "Roy's Banquet", "Delhi", "Banquest"));
                  updateEventView.callBack(position,item);
                 check=false;
        }

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                index= position;
                if (position==0 ){
                    item.add(new EventsModel(R.drawable.event3, R.drawable.cardbox1, "Bohemia Bar", "Karol Bagh", "Bar/Restront"));
                    item.add(new EventsModel(R.drawable.event2, R.drawable.crown, "Manali Trench", "Himachal Pradesh", "Outdoor Wedding"));
                    item.add(new EventsModel(R.drawable.event2, R.drawable.cardbox1, "Batra Hall", "Gurugram", "Hall"));
                    item.add(new EventsModel(R.drawable.event1, R.drawable.cardbox1, "Royal Pepper", "Peeragarhi", "Banquest"));
                    updateEventView.callBack(position,item);
                }
                else if (position==1){
                    item.add(new EventsModel(R.drawable.food3, R.drawable.cardbox1, "Thai Cuisine", "Karol Bagh", "Bar/Restront"));
                    item.add(new EventsModel(R.drawable.food2, R.drawable.crown, "French Cuisine", "Himachal Pradesh", "VIP"));
                    item.add(new EventsModel(R.drawable.food4, R.drawable.cardbox1, "Indian Cuisine", "Gurugram", "4's"));
                    item.add(new EventsModel(R.drawable.food1, R.drawable.crown, "Italian Cuisine", "Peeragarhi", "VIP"));
                    updateEventView.callBack(position,item);
                }
                else if (position==2){
                    item.add(new EventsModel(R.drawable.concert1, R.drawable.cardbox1, "KK Concert", "Karol Bagh", "2 Hr"));
                    item.add(new EventsModel(R.drawable.concert3, R.drawable.cardbox1, "Imagine-Dragon Concert", "Himachal Pradesh", "4 Hr"));
                    item.add(new EventsModel(R.drawable.concert2, R.drawable.cardbox1, "Divine Concert", "Gurugram", "1.5 Hr"));
                    item.add(new EventsModel(R.drawable.concert4, R.drawable.cardbox1, "Eminem Concert", "Peeragarhi", "3 Hr"));
                    updateEventView.callBack(position,item);
                }
                notifyDataSetChanged();
            }
        });
        if (select){
            if (holder.getAdapterPosition()==0){
                holder.layout.setBackgroundResource(R.drawable.catagory_on_select);
                select=false;
            }
        }else {
            if (index == holder.getAdapterPosition()) {
                holder.layout.setBackgroundResource(R.drawable.catagory_on_select);
            } else {
                holder.layout.setBackgroundResource(R.drawable.searchbox_bk);
            }
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class catagoryViewholdere extends RecyclerView.ViewHolder{

         TextView text;
         ImageView image;
         LinearLayout layout;
        public catagoryViewholdere(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            text = itemView.findViewById(R.id.text);
            layout = itemView.findViewById(R.id.home_catagory_layout);
        }
    }

}
