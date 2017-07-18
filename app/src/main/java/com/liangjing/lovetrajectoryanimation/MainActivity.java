package com.liangjing.lovetrajectoryanimation;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.liangjing.lovetrajectoryanimation.view.FavorLayout;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_layout);
        init();
    }

    private void init() {
        final FavorLayout favorLayout = (FavorLayout) findViewById(R.id.favorAnimation);
        favorLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                favorLayout.addHeart();
            }
        });
    }
}
