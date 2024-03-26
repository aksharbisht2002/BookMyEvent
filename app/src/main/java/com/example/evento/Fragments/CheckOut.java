package com.example.evento.Fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.evento.HelperClass.BookingHelper;
import com.example.evento.Home;
import com.example.evento.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class CheckOut extends Fragment implements PaymentResultListener {

     ImageView backBtn;
     Button pay;
     TextView heading,pricedisplay;
     EditText fromdateBox,todateBox,setprice;
     LinearLayout fromcalenderBtn,tocalenderBtn;
     SharedPreferences preferences;

     DatabaseReference bookingReference = FirebaseDatabase.getInstance().getReference("Bookings");
     String CustomerId;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_check_out, container, false);
        backBtn = view.findViewById(R.id.backBtn);
        pay = view.findViewById(R.id.bookBtn);
        heading = view.findViewById(R.id.heading);
        fromcalenderBtn = view.findViewById(R.id.linearLayout6);
        tocalenderBtn = view.findViewById(R.id.linearLayout);
        fromdateBox = view.findViewById(R.id.fromdate);
        todateBox = view.findViewById(R.id.todate);
        setprice = view.findViewById(R.id.priceset);
        pricedisplay = view.findViewById(R.id.price);

        preferences = this.getActivity().getSharedPreferences("LoginPref", Context.MODE_PRIVATE);
         CustomerId = preferences.getString("CustomerId",null);


        heading.setText(getArguments().getString("heading"));
        pricedisplay.setText(getArguments().getString("price"));

        int y,m,d,price,minprice;
        Calendar ca = Calendar.getInstance();
        y=ca.get(Calendar.YEAR);
        m=ca.get(Calendar.MONTH);
        d=ca.get(Calendar.DAY_OF_MONTH);
        price = (Integer.parseInt(pricedisplay.getText().toString()) * 3)/10;

        setprice.setHint("Minimum of "+price+" need to be paid in advance");

        Checkout.preload(getContext().getApplicationContext());



        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().remove(CheckOut.this).commit();
            }
        });

        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fromdateBox.getText().toString().isEmpty() | todateBox.getText().toString().isEmpty() |
                        setprice.getText().toString().isEmpty()
                ){
                    Toast.makeText(getActivity(), "Fields are empty", Toast.LENGTH_LONG).show();
                    return;
                } else if (Integer.parseInt(setprice.getText().toString()) < price) {
                    setprice.setError("Pay at least "+price+" or more");
                } else {
                    setBookingValueInDB(CustomerId);
                    PaymentNow(setprice.getText().toString());
                }


            }
        });

        fromcalenderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new DatePickerDialog(getActivity(),listener,y,m,d).show();

            }
        });

         tocalenderBtn.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {

                 new DatePickerDialog(getActivity(),listener2,y,m,d).show();
             }
         });


        return view;
    }

    private void PaymentNow(String amount) {
        Checkout checkout = new Checkout();
        checkout.setKeyID("rzp_test_m9TVXriVcTLDnP");
        checkout.setImage(R.drawable.bookmyeventlogo);

        double finalprice = Float.parseFloat(setprice.getText().toString()) * 100;
        try {
            JSONObject object = new JSONObject();
            object.put("name","Evento");
            object.put("description","#342dw24dsf");
            object.put("theme.color","#41170F");
            object.put("currency","INR");
            object.put("amount",finalprice+"");

            checkout.open(getActivity(),object);

        }catch (Exception e){
            Log.d("error : ",e+"");
        }

    }

    @SuppressLint("MissingInflatedId")
    private void callSuccessPopUpMenu(View view,int a,String s,boolean success) {
        CardView cardView = view.findViewById(R.id.sucess_card_layout);
        View view1 = LayoutInflater.from(getActivity()).inflate(R.layout.successfull_payment,cardView);
        Button btn = view1.findViewById(R.id.okbutton);
        CircleImageView img = view1.findViewById(R.id.cardImg);
        TextView txt = view1.findViewById(R.id.textView9);
        img.setImageResource(a);
        txt.setText(s);
        AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
        builder1.setView(view1);
        final  AlertDialog alertDialog1 = builder1.create();
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (success){
                    setBookingValueInDB(CustomerId);
                }
                alertDialog1.dismiss();
                Intent i =new Intent(getActivity(),Home.class);
                startActivity(i);
            }
        });

        if (alertDialog1.getWindow()!=null){
            alertDialog1.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog1.show();
    }

    private void setBookingValueInDB(String customerId) {
        String from,to,heading,amount;
        int remainingPrice;
        from = fromdateBox.getText().toString();
        to = todateBox.getText().toString();
        heading = getArguments().getString("heading");
        amount = setprice.getText().toString();
        remainingPrice = Integer.parseInt(pricedisplay.getText().toString()) - Integer.parseInt(amount);
        BookingHelper bookingHelper = new BookingHelper(from,to,heading,amount,String.valueOf(remainingPrice));
        bookingReference.child(customerId).child(heading).setValue(bookingHelper);
    }

    DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
            fromdateBox.setText(i2+"-"+(i1+1)+"-"+i);
        }
    };
    DatePickerDialog.OnDateSetListener listener2 = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
            todateBox.setText(i2+"-"+(i1+1)+"-"+i);
        }
    };


    @Override
    public void onPaymentSuccess(String s) {
        callSuccessPopUpMenu(getView(),R.drawable.baseline_checkwhite_24,s,true);
    }

    @Override
    public void onPaymentError(int i, String s) {
        callSuccessPopUpMenu(getView(),R.drawable.baseline_checkwhite_24,s,false);
    }
}