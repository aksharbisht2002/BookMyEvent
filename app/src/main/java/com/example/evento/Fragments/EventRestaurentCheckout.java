package com.example.evento.Fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.evento.HelperClass.BookingHelper;
import com.example.evento.Home;
import com.example.evento.R;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class EventRestaurentCheckout extends Fragment implements PaymentResultListener {
    ImageView backBtn;
    Button pay;
    TextView heading,pricedisplay;
    EditText setprice;
    Spinner fromdateBox;
    LinearLayout fromcalenderBtn;
    SharedPreferences preferences;
    MaterialDatePicker datePicker;
    Random random = new Random();

    DatabaseReference bookingReference = FirebaseDatabase.getInstance().getReference("Bookings");
    String CustomerId;
    ArrayList<String> dateList= new ArrayList<>();
    String[] dateArray ={"28-Jun,2023","29-Jun,2023","30-Jun,2023","1-Jul,2023","2-Jul,2023","3-Jul,2023","4-Jul,2023","5-Jul,2023","6-Jul,2023","7-Jul,2023","8-Jul,2023","9-Jul,2023","10-Jul,2023","11-Jul,2023","12-Jul,2023","13-Jul,2023","14-Jul,2023","15-Jul,2023","16-Jul,2023","17-Jul,2023","18-Jul,2023","19-Jul,2023","20-Jul,2023",
            "21-Jul,2023","22-Jul,2023","23-Jul,2023","24-Jul,2023","25-Jul,2023","26-Jul,2023","27-Jul,2023","28-Jul,2023","29-Jul,2023","30-Jul,2023","31-Jul,2023"};
    private int price;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view  = inflater.inflate(R.layout.fragment_event_restaurent_checkout, container, false);

        backBtn = view.findViewById(R.id.backBtn);
        pay = view.findViewById(R.id.bookBtn);
        heading = view.findViewById(R.id.heading);
        fromcalenderBtn = view.findViewById(R.id.linearLayout6);
        fromdateBox = view.findViewById(R.id.fromdate);
        setprice = view.findViewById(R.id.priceset);
        pricedisplay = view.findViewById(R.id.price);
        dateList.clear();
       dateList.add("Select a date");
       for (int i=1;i<=random.nextInt(12)+2;i++){
           String date = dateArray[random.nextInt(dateArray.length)];
           dateList.add(date);
       }
        ArrayAdapter ad = new ArrayAdapter(getContext(),android.R.layout.simple_list_item_1,dateList);
        //ad.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
         fromdateBox.setAdapter(ad);

        datePicker = MaterialDatePicker.Builder.datePicker().setTitleText("Select Date").setSelection(MaterialDatePicker.todayInUtcMilliseconds()).build();

        preferences = this.getActivity().getSharedPreferences("LoginPref", Context.MODE_PRIVATE);
        CustomerId = preferences.getString("CustomerId",null);


        heading.setText(getArguments().getString("heading"));
        pricedisplay.setText(getArguments().getString("price"));

        price = (Integer.parseInt(pricedisplay.getText().toString()) * 3)/10;

        setprice.setHint("Minimum of "+price+" need to be paid in advance");

        Checkout.preload(getContext().getApplicationContext());



        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().remove(EventRestaurentCheckout.this).commit();
            }
        });

        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fromdateBox.toString().isEmpty() |
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




        return view;
    }

    public class dateclass{
        String date;
        public dateclass() {
        }

        public dateclass(String date) {
            this.date = date;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }
    }

    private void PaymentNow(String amount) {
        Checkout checkout = new Checkout();
        checkout.setKeyID("rzp_test_m9TVXriVcTLDnP");
        checkout.setImage(R.drawable.bookmyeventlogo);

        double finalprice = Float.parseFloat(setprice.getText().toString()) * 100;
        try {
            JSONObject object = new JSONObject();
            object.put("name","bookmyevent");
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
                Intent i =new Intent(getActivity(), Home.class);
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
        from = fromdateBox.getSelectedItem().toString();
        heading = getArguments().getString("heading");
        amount = setprice.getText().toString();
        remainingPrice = Integer.parseInt(pricedisplay.getText().toString()) - Integer.parseInt(amount);
        BookingHelper bookingHelper = new BookingHelper(from,from,heading,amount,String.valueOf(remainingPrice));
        bookingReference.child(customerId).child(heading).setValue(bookingHelper);
    }


    @Override
    public void onPaymentSuccess(String s) {
        callSuccessPopUpMenu(getView(),R.drawable.baseline_checkwhite_24,s,true);
    }

    @Override
    public void onPaymentError(int i, String s) {
        callSuccessPopUpMenu(getView(),R.drawable.baseline_checkwhite_24,s,false);
    }


}