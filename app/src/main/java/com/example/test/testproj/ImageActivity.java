package com.example.test.testproj;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class ImageActivity extends AppCompatActivity {
    private ImageView offersFullImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        offersFullImage = (ImageView) findViewById(R.id.offersFullImage);
        Glide.with(this).load(getIntent().getStringExtra("url")).into(offersFullImage);
    }

}
