package com.symmetrixsystems.gistapp.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import androidx.core.content.ContextCompat;

import com.symmetrixsystems.gistapp.R;
import com.symmetrixsystems.gistapp.async.UpdateUserTask;
import com.symmetrixsystems.gistapp.async.UserInfoTask;
import com.symmetrixsystems.gistapp.listener.UpdateUserListener;
import com.symmetrixsystems.gistapp.listener.UserInfoListener;
import com.symmetrixsystems.gistapp.model.UpdateUser;
import com.symmetrixsystems.gistapp.model.UserClass;
import com.symmetrixsystems.gistapp.model.UserInfo;
import com.symmetrixsystems.gistapp.util.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Copyright - All Rights Reserved
 * Created by Mohammad Azharuddin on 17/03/2020.
 * Email mailsahil07@gmail.com
 */
public class ControlActivity extends AppCompatActivity implements View.OnClickListener, UserInfoListener, UpdateUserListener {

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
    private int fontSize =0, notify=0, hdImage=0, hasSelection=0;
    private String userName="", userEmail="";


    SharedPreferences settings;
    SharedPreferences.Editor editor;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control);
        mContext = ControlActivity.this;
        initView();
    }

    private void initView(){

        settings = getApplicationContext().getSharedPreferences(mContext.getResources().getString(R.string.gist_control_prefs), 0);
        editor = settings.edit();



        ivEvacado = findViewById(R.id.iv_home);
        ivEvacado.setOnClickListener(this);


        etName = findViewById(R.id.et_name);
        etEmoji = findViewById(R.id.et_emoji);

        userName = etName.getText().toString();

        //Font size

        tvSmall = findViewById(R.id.tv_btnSmal);
        tvMedium = findViewById(R.id.tv_btnMedium);
        tvLarge = findViewById(R.id.tv_btnLarge);

        tvSmall.setOnClickListener(this);
        tvMedium.setOnClickListener(this);
        tvLarge.setOnClickListener(this);


        tvNotificationOn = findViewById(R.id.tv_notfiication_on);
        tvNotificationOff = findViewById(R.id.tv_notfiication_off);
        tvNotificationOn.setOnClickListener(this);
        tvNotificationOff.setOnClickListener(this);

        thHdOn = findViewById(R.id.tv_hd_images_on);
        tvHdOff = findViewById(R.id.tv_hd_images_off);
        thHdOn.setOnClickListener(this);
        tvHdOff.setOnClickListener(this);

        clFooter = findViewById(R.id.ll_footer);
        clFooter.setOnClickListener(this);



    }

    @Override
    protected void onResume() {
        super.onResume();
        getUserDetails();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_home:
                Intent intentControl = new Intent(mContext, StoryListActivity.class);
                startActivity(intentControl);
                break;
            case R.id.tv_btnSmal:
                fontSize = 1;
                changeFontSize(fontSize);updateProfile();break;
            case R.id.tv_btnMedium:
                fontSize = 2;
                changeFontSize(fontSize);updateProfile();break;
            case R.id.tv_btnLarge:
                fontSize = 3;
                changeFontSize(fontSize);updateProfile();break;
            case R.id.tv_notfiication_on:
                notify = 1;
                setNotification(notify);updateProfile();break;
            case R.id.tv_notfiication_off:
                notify = 0;
                setNotification(notify);updateProfile();break;
            case R.id.tv_hd_images_on:
                hdImage = 1;
                setHdImage(hdImage);updateProfile();break;
            case R.id.tv_hd_images_off:
                hdImage = 0;
                setHdImage(hdImage);updateProfile();break;

            case R.id.ll_footer:
                updateProfile();
                break;
        }
    }

    private void changeFontSize(int fontSize){
        if(fontSize ==1){
            tvSmall.setBackground(mContext.getResources().getDrawable(R.drawable.btn_control_selected_drawable));
            tvMedium.setBackground(mContext.getResources().getDrawable(R.drawable.btn_control_normal_drawable));
            tvLarge.setBackground(mContext.getResources().getDrawable(R.drawable.btn_control_normal_drawable));

            tvSmall.setTextColor(mContext.getResources().getColorStateList(R.color.btn_control_text_selected_drawable));
            tvMedium.setTextColor(mContext.getResources().getColorStateList(R.color.btn_control_text_drawable));
            tvLarge.setTextColor(mContext.getResources().getColorStateList(R.color.btn_control_text_drawable));

        }
        else if(fontSize ==2){
            tvSmall.setBackground(mContext.getResources().getDrawable(R.drawable.btn_control_normal_drawable));
            tvMedium.setBackground(mContext.getResources().getDrawable(R.drawable.btn_control_selected_drawable));
            tvLarge.setBackground(mContext.getResources().getDrawable(R.drawable.btn_control_normal_drawable));

            tvSmall.setTextColor(mContext.getResources().getColorStateList(R.color.btn_control_text_drawable));
            tvMedium.setTextColor(mContext.getResources().getColorStateList(R.color.btn_control_text_selected_drawable));
            tvLarge.setTextColor(mContext.getResources().getColorStateList(R.color.btn_control_text_drawable));
        }
        else if(fontSize ==3){
            tvSmall.setBackground(mContext.getResources().getDrawable(R.drawable.btn_control_normal_drawable));
            tvMedium.setBackground(mContext.getResources().getDrawable(R.drawable.btn_control_normal_drawable));
            tvLarge.setBackground(mContext.getResources().getDrawable(R.drawable.btn_control_selected_drawable));

            tvSmall.setTextColor(mContext.getResources().getColorStateList(R.color.btn_control_text_drawable));
            tvMedium.setTextColor(mContext.getResources().getColorStateList(R.color.btn_control_text_drawable));
            tvLarge.setTextColor(mContext.getResources().getColorStateList(R.color.btn_control_text_selected_drawable));
        }
    }


    private void setNotification(int notify){
        if(notify ==1) {
            tvNotificationOn.setBackground(mContext.getResources().getDrawable(R.drawable.btn_control_selected_drawable));
            tvNotificationOff.setBackground(mContext.getResources().getDrawable(R.drawable.btn_control_normal_drawable));

            tvNotificationOn.setTextColor(mContext.getResources().getColorStateList(R.color.btn_control_text_selected_drawable));
            tvNotificationOff.setTextColor(mContext.getResources().getColorStateList(R.color.btn_control_text_drawable));
        }
        else if(notify ==0) {
            tvNotificationOn.setBackground(mContext.getResources().getDrawable(R.drawable.btn_control_normal_drawable));
            tvNotificationOff.setBackground(mContext.getResources().getDrawable(R.drawable.btn_control_selected_drawable));

            tvNotificationOn.setTextColor(mContext.getResources().getColorStateList(R.color.btn_control_text_drawable));
            tvNotificationOff.setTextColor(mContext.getResources().getColorStateList(R.color.btn_control_text_selected_drawable));
        }
    }

    private void setHdImage(int hdImage){
        if(hdImage ==1) {
            thHdOn.setBackground(mContext.getResources().getDrawable(R.drawable.btn_control_selected_drawable));
            tvHdOff.setBackground(mContext.getResources().getDrawable(R.drawable.btn_control_normal_drawable));

            thHdOn.setTextColor(mContext.getResources().getColorStateList(R.color.btn_control_text_selected_drawable));
            tvHdOff.setTextColor(mContext.getResources().getColorStateList(R.color.btn_control_text_drawable));
        }
        else if(hdImage ==0) {
            thHdOn.setBackground(mContext.getResources().getDrawable(R.drawable.btn_control_normal_drawable));
            tvHdOff.setBackground(mContext.getResources().getDrawable(R.drawable.btn_control_selected_drawable));

            thHdOn.setTextColor(mContext.getResources().getColorStateList(R.color.btn_control_text_drawable));
            tvHdOff.setTextColor(mContext.getResources().getColorStateList(R.color.btn_control_text_selected_drawable));
        }
    }


    private void getUserDetails(){
        userId  =   Util.fetchUserClass(mContext).getUserId();
        JSONObject objUserDetails =   new JSONObject();
        try {
            objUserDetails.put("action", "get_user_info");

            objUserDetails.put("user_id", ""+userId);


            String requestUserDetails  =  objUserDetails.toString();

            Log.v(TAG, requestUserDetails);

            UserInfoTask userInfoTask = new UserInfoTask(mContext, requestUserDetails);
            userInfoTask.mListener = this;



        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    private void updateProfile(){
        userId  =   Util.fetchUserClass(mContext).getUserId();
        userName = etName.getText().toString();
        JSONObject objUpdateUser =   new JSONObject();
        try {
            objUpdateUser.put("action", "update_user_info");
            objUpdateUser.put("user_id", userId);
            objUpdateUser.put("user_name", userName);
            objUpdateUser.put("font_size", fontSize);
            objUpdateUser.put("notification", notify);
            objUpdateUser.put("hd_image", hdImage);

            String requestUserUpdate = objUpdateUser.toString();

            Log.v(TAG, "update profile, "+requestUserUpdate);

            UpdateUserTask updateUserTask = new UpdateUserTask(mContext, requestUserUpdate);
            updateUserTask.mListener = this;

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void userInfoCallback(ArrayList<UserInfo> userInfos) {
        if (userInfos.size() > 0){
            int erroCode = userInfos.get(0).getErrorCode();
            String userDetail = userInfos.get(0).getUserDetails();
            Log.v(TAG, ""+userDetail);

            if(erroCode ==0){
                try {
                    JSONObject objUser = new JSONObject(userDetail);
                    userName = objUser.optString("fullname");
                    userEmail = objUser.optString("user_email");
                    hasSelection = objUser.optInt("has_selection");

                    etName.setText(userName);

                    //etm.setText(userEmail);

                    fontSize = objUser.optInt("textsize");
                    changeFontSize(fontSize);

                    notify = objUser.optInt("send_notification");

                    setNotification(notify);
                    hdImage = objUser.optInt("hd_image");
                    setHdImage(hdImage);


                    UserClass userClass   =   new UserClass();
                    userClass.setUserId(""+userId);
                    userClass.setUserFullName(userName);
                    userClass.setLoggedIn(true);
                    if(hasSelection==1) {
                        userClass.setHasSelection(true);
                    }
                    else{
                        userClass.setHasSelection(false);
                    }

                    userClass.setFontSize(""+objUser.optString("fontsoze"));
                    if(objUser.optInt("send_notification") == 1){
                        userClass.setNotification(true);
                    }
                    else{
                        userClass.setNotification(false);
                    }

                    if(objUser.optInt("hd_images") == 1){
                        userClass.setHDImages(true);
                    }
                    else{
                        userClass.setHDImages(false);
                    }

                    Util.saveUserClass(mContext, userClass);





                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }
        else{
            Util.showMessageWithOk(mContext, ""+getString(R.string.something_went_wrong)+"\n"+getString(R.string.please_try_again));
            return;

        }
    }

    @Override
    public void updateUserCallBack(ArrayList<UpdateUser> updateUsers) {
        if(updateUsers.size() > 0){
            int errorCode = updateUsers.get(0).getErrorCode();
            if(errorCode ==0){
                startActivity(getIntent());
                finish();
            }
        }
        else{
            Util.showMessageWithOk(mContext, ""+getString(R.string.something_went_wrong)+"\n"+getString(R.string.please_try_again));
            return;

        }
    }
}
