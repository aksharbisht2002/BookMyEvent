package com.example.evento;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.View;
import android.widget.ImageView;

public class SplaseScreen extends AppCompatActivity {
    ImageView logo,bg;
    SharedPreferences preferences;
    boolean login;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splase_screen);

        preferences = getSharedPreferences("LoginPref", Context.MODE_PRIVATE);
        preferences.edit().putString("Uri",null).apply();

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        logo=findViewById(R.id.logo);
        bg=findViewById(R.id.zoomimg);

        logo.animate().rotation(360).setDuration(1000).setStartDelay(1000);
        logo.animate().scaleX(2).scaleY(2).setDuration(1000).setStartDelay(1000);
        bg.animate().scaleY(10).scaleX(10).setDuration(1000).setStartDelay(1000);


          new Handler().postDelayed(new Runnable() {
              @Override
              public void run() {
                  Intent i = new Intent(SplaseScreen.this,Register.class);
                  Pair[] pairs = new Pair[1];
                  pairs[0] = new Pair<View,String>(logo,"applogo");
                   login = preferences.getBoolean("Login",false);
                   if (!login){
                       ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SplaseScreen.this,pairs);
                       startActivity(i,options.toBundle());
                       finish();
                   }
                   else {
                       Intent intent = new Intent(SplaseScreen.this,Home.class);
                       startActivity(intent);
                       finish();
                   }
              }


          },4000);

    }
}