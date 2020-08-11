package com.symmetrixsystems.gistapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.symmetrixsystems.gistapp.R;
import com.symmetrixsystems.gistapp.util.Util;

public class SplashScreenActivity extends AppCompatActivity {
    private Context mContext;

    SharedPreferences settings;
    String CONTROL_PREFS = "";
    String enableNightMode = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        mContext    =   SplashScreenActivity.this;

        CONTROL_PREFS = ""+getResources().getString(R.string.gist_control_prefs);
        settings = getSharedPreferences(CONTROL_PREFS, MODE_PRIVATE);
        enableNightMode    = settings.getString("enable_night_mode", ""+getResources().getString(R.string.ctrl_scr_night_mode_no));
        if(enableNightMode.equals(""+getResources().getString(R.string.ctrl_scr_night_mode_no))) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }



        Thread timer = new Thread(){
            public void run(){
                try {
                    sleep(3000);
                }
                catch (InterruptedException e){
                    e.printStackTrace();
                }
                finally {
                    //Intent splashIntent = new Intent(mContext, MainCategoryActivity.class);
                    if (Util.fetchUserClass(mContext) != null && Util.fetchUserClass(mContext).getUserId() != null) {
                        if(Util.fetchUserClass(mContext).hasSelection()){
                            //Intent splashIntent = new Intent(mContext, NavActivity.class);
                            //Intent splashIntent = new Intent(mContext, NewsNewActivity.class);
                            Intent splashIntent = new Intent(mContext, StoryListActivity.class);
                            startActivity(splashIntent);
                            finish();
                        }
                        else{
                            Intent splashIntent = new Intent(mContext, MainCategoryActivity.class);
                            startActivity(splashIntent);
                            finish();
                        }

                    }
                    else {
                        Intent splashIntent = new Intent(mContext, WelcomeActivity.class);
                        startActivity(splashIntent);
                        finish();
                    }
                    /*Intent splashIntent = new Intent(mContext, GetStartedActivity.class);
                    startActivity(splashIntent);
                    finish();*/
                }
            }

        };

        timer.start();

    }


}
