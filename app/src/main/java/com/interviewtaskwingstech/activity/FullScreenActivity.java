package com.interviewtaskwingstech.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.interviewtaskwingstech.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FullScreenActivity extends AppCompatActivity {

    @BindView(R.id.ivFullImage) ImageView ivFullImage;
    @BindView(R.id.txtAppName) TextView txtAppName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen);

        ButterKnife.bind(this);

        Intent i = getIntent();
        if (i != null){
            Glide.with(FullScreenActivity.this)
                    .load(i.getStringExtra("image_url"))
                    .placeholder(R.drawable.ic_baseline_person_24)
                    .error(R.drawable.ic_baseline_person_24)
                    .into(ivFullImage);

        }
    }
}