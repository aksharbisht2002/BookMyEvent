package com.example.evento.Models;

public class EventsModel {
    private int backgroungImg,PremiumImg;
    private String heading,location,type;


    public EventsModel(int backgroungImg, int premiumImg, String heading, String location, String type) {
        this.backgroungImg = backgroungImg;
        PremiumImg = premiumImg;
        this.heading = heading;
        this.location = location;
        this.type = type;
    }

    public int getBackgroungImg() {
        return backgroungImg;
    }

    public int getPremiumImg() {
        return PremiumImg;
    }

    public String getHeading() {
        return heading;
    }

    public String getLocation() {
        return location;
    }

    public String getType() {
        return type;
    }

}
