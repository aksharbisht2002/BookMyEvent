package com.example.evento.Fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.evento.Home;
import com.example.evento.R;
import com.example.evento.Register;

public class info extends Fragment {

 TextView call,sms;
 EditText text;
 Button send;
 LinearLayout layout;
 ImageView back;

 private  String phone = "7827129525";
    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_info, container, false);
        call = view.findViewById(R.id.call);
        sms = view.findViewById(R.id.sms);
        text = view.findViewById(R.id.edittext);
        send = view.findViewById(R.id.sendBtn);
        back = view.findViewById(R.id.backBtn);
        layout = view.findViewById(R.id.textField);
        layout.setVisibility(View.INVISIBLE);



        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE)!=
                        PackageManager.PERMISSION_GRANTED
                ){
                    ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.CALL_PHONE},1);
                }else {
                    Intent i = new Intent(Intent.ACTION_CALL);
                    i.setData(Uri.parse("tel:"+phone));
                    getActivity().startActivity(i);
                }

            }
        });

        sms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(getContext(),Manifest.permission.SEND_SMS)!=
                        PackageManager.PERMISSION_GRANTED
                ){
                    ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.SEND_SMS},2);
                }
                else {
                    layout.setVisibility(View.VISIBLE);
                    send.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String msg = text.getText().toString();
                            if (!msg.isEmpty()){
                                SmsManager smsManager = SmsManager.getDefault();
                                smsManager.sendTextMessage(phone,null,msg,null,null);
                                Toast.makeText(getContext(), "Send Successfully", Toast.LENGTH_SHORT).show();
                                text.setText("");

                            }else {
                                Toast.makeText(getActivity(), "Massage box can't empty", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), Home.class);
                startActivity(i);
            }
        });

        return view;
    }
}