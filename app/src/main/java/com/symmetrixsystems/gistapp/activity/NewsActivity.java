package com.symmetrixsystems.gistapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.symmetrixsystems.gistapp.R;
import com.symmetrixsystems.gistapp.bottomsheet.BottomSheetExample;

public class NewsActivity extends AppCompatActivity {

    CoordinatorLayout constraintLayout;
    BottomSheetBehavior bottomSheetBehavior;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        BottomSheetExample b = new BottomSheetExample();
        b.show(getSupportFragmentManager(), "bse");

        View bottomSheet    =   findViewById(R.id.cl_next_news);
        bottomSheetBehavior =   BottomSheetBehavior.from(bottomSheet);

    }
}
