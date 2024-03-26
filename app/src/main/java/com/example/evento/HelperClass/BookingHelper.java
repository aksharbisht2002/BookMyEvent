package com.example.evento.HelperClass;

public class BookingHelper {
    String from,to,heading, paid_amount,remaining_amount;
    public BookingHelper() {
    }

    public BookingHelper(String from, String to, String heading, String amount){
        this.from = from;
        this.to = to;
        this.heading = heading;
        this.paid_amount = amount;
    }
    public BookingHelper(String from, String to, String heading, String amount,String remaining_amount) {
        this.from = from;
        this.to = to;
        this.heading = heading;
        this.paid_amount = amount;
        this.remaining_amount = remaining_amount;
    }

    public String getRemaining_amount() {
        return remaining_amount;
    }

    public void setRemaining_amount(String remaining_amount) {
        this.remaining_amount = remaining_amount;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public String getPaid_amount() {
        return paid_amount;
    }

    public void setPaid_amount(String paid_amount) {
        this.paid_amount = paid_amount;
    }
}
