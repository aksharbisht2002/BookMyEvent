package com.example.evento.Interface;

import com.example.evento.Models.EventsModel;

import java.util.ArrayList;

public interface UpdateEventView {
    public  void callBack(int position, ArrayList<EventsModel> items);
}
