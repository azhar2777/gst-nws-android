package com.symmetrixsystems.gistapp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.symmetrixsystems.gistapp.R;
import com.symmetrixsystems.gistapp.util.Util;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Copyright - All Rights Reserved
 * Created by Mohammad Azharuddin on 17/03/2020.
 * Email mailsahil07@gmail.com
 */
public class ControlActivity extends AppCompatActivity implements View.OnClickListener {

    private Context mContext;
    private String TAG = "ControlActivityTAG";
    ImageView ivEvacado;
    private EditText etName, etEmoji;
    //TextSize
    private TextView tvSmall, tvMedium, tvLarge;
    //Notification & HD
    private TextView tvNotificationOn, tvNotificationOff, thHdOn, tvHdOff;

    private CardView cvContact, cvShare, cvRate;

    private ConstraintLayout clFooter;


    String userId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control);
        mContext = ControlActivity.this;
        initView();
    }

    private void initView(){
        ivEvacado = findViewById(R.id.iv_home);
        ivEvacado.setOnClickListener(this);

        clFooter = findViewById(R.id.ll_footer);
        clFooter.setOnClickListener(this);


        getUserDetails();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_home:
                Intent intentControl = new Intent(mContext, StoryListActivity.class);
                startActivity(intentControl);
                break;
            case R.id.ll_footer:
                updateProfile();
                break;
        }
    }


    private void getUserDetails(){
        userId  =   Util.fetchUserClass(mContext).getUserId();
        JSONObject objUserDetails =   new JSONObject();
        try {
            objUserDetails.put("action", "get_user_details");

            objUserDetails.put("user_id", ""+userId);


            String requestUserDetails  =  objUserDetails.toString();

            Log.v(TAG, requestUserDetails);



        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    private void updateProfile(){
        Log.v(TAG, "update profile");
    }
}
