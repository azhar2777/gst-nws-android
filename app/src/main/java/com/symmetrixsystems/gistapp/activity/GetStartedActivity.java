package com.symmetrixsystems.gistapp.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.symmetrixsystems.gistapp.R;
import com.symmetrixsystems.gistapp.fragment.SlideFiveFragment;
import com.symmetrixsystems.gistapp.fragment.SlideFourFragment;
import com.symmetrixsystems.gistapp.fragment.SlideOneFragment;
import com.symmetrixsystems.gistapp.fragment.SlideThreeFragment;
import com.symmetrixsystems.gistapp.fragment.SlideTwoFragment;

public class GetStartedActivity extends FragmentActivity {

    private Context mContext;
    private ViewPager viewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_started);
        viewPager = (ViewPager) findViewById(R.id.viewpager);

        viewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        mContext    =   GetStartedActivity.this;

    }

    private class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int pos) {
            switch(pos) {

                case 0: return SlideOneFragment.newInstance("FirstFragment, Instance 1");
                case 1: return SlideTwoFragment.newInstance("SecondFragment, Instance 2");
                case 2: return SlideThreeFragment.newInstance("ThirdFragment, Instance 2");
                case 3: return SlideFourFragment.newInstance("fourthFragment, Instance 2");
                case 4: return SlideFiveFragment.newInstance("FifthFragment, Instance 2");

                default: return SlideOneFragment.newInstance("FirstFragment, Default");
            }
        }

        @Override
        public int getCount() {
            return 5;
        }
    }
}
