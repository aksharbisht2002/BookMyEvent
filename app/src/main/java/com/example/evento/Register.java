package com.example.evento;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.example.evento.Fragments.SignIn;
import com.example.evento.Fragments.SignUp;

public class Register extends AppCompatActivity {
    Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        if (ActivityCompat.checkSelfPermission(Register.this,Manifest.permission.INTERNET)!=
              PackageManager.PERMISSION_GRANTED
         ){
            ActivityCompat.requestPermissions(Register.this,new String[]{Manifest.permission.INTERNET},0);
        }

        getSupportFragmentManager().beginTransaction().add(R.id.Register,new SignIn()).commit();
    }

    @Override
    public void onBackPressed() {
        int count = getSupportFragmentManager().getBackStackEntryCount();

        if (count == 0) {
            super.onBackPressed();
            finish();
        } else {
            getSupportFragmentManager().popBackStack();
        }
    }
}