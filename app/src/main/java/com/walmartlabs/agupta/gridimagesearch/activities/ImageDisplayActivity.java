package com.walmartlabs.agupta.gridimagesearch.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.walmartlabs.agupta.gridimagesearch.R;
import com.walmartlabs.agupta.gridimagesearch.models.ImageResult;

public class ImageDisplayActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_display);

        //String url = getIntent().getStringExtra("url");
        ImageResult imageResult = (ImageResult) getIntent().getSerializableExtra("result");
        String url = imageResult.fullURL;
        ImageView ivImage = (ImageView) findViewById(R.id.ivResult);
        Picasso.with(this).load(url).into(ivImage);
        //getSupportActionBar().hide();
    }

}
