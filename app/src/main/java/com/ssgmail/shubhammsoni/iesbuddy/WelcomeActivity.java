package com.ssgmail.shubhammsoni.iesbuddy;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

public class WelcomeActivity extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 2900;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        getSupportActionBar().hide();
//        getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
        ImageView img = findViewById(R.id.img);
        ImageView hiimg = findViewById(R.id.hi);
        TextView txt1 = findViewById(R.id.txt1);
        TextView txt2 = findViewById(R.id.txt2);
        hiimg.animate().alpha(1f).setDuration(1800);
        txt1.animate().alpha(1f).setDuration(3000);
        txt2.animate().alpha(1f).setDuration(4200);
        //start welcome screen

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        }, SPLASH_TIME_OUT);

    }
}
