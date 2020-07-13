package com.symmetrixsystems.gistapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.symmetrixsystems.gistapp.R;
import com.symmetrixsystems.gistapp.util.Util;

public class SplashScreenActivity extends AppCompatActivity {
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        mContext    =   SplashScreenActivity.this;
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
                        }
                        else{
                            Intent splashIntent = new Intent(mContext, MainCategoryActivity.class);
                            startActivity(splashIntent);
                        }

                    }
                    else {
                        Intent splashIntent = new Intent(mContext, GetStartedActivity.class);
                        startActivity(splashIntent);
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
