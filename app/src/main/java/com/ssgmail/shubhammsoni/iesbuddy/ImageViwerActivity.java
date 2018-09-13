package com.ssgmail.shubhammsoni.iesbuddy;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class ImageViwerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_viwer);
        getSupportActionBar().hide();
        ImageView imageView = findViewById(R.id.ImageViewer);
        Glide.with(this).load(getIntent().getStringExtra("imageurl")).into(imageView);


    }
}
