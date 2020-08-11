package com.symmetrixsystems.gistapp.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

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
    private SwitchCompat switchNightMode;


    String userId;
    private int fontSize =0, notify=0, hdImage=0, hasSelection=0;
    private String userName="", userEmail="";


    SharedPreferences settings;
    SharedPreferences.Editor editor;

    boolean switchFirst = true;


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

        switchNightMode = findViewById(R.id.swicth_night_mode);
        //switchNightMode.setOnClickListener(this);







    }

    @Override
    protected void onResume() {
        super.onResume();

        switchNightMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean status) {
                //changeToNightMode();
                if(status){
                    Log.v(TAG, "Checked Button");
                    changeToNightMode(status);
//                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
//                    editor.putString("enable_night_mode", ""+getResources().getString(R.string.ctrl_scr_night_mode_yes));
//                    editor.apply();
                }
                else{
                    Log.v(TAG, "Unchecked Button");
                    changeToNightMode(status);
//                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
//                    editor.putString("enable_night_mode", ""+getResources().getString(R.string.ctrl_scr_night_mode_no));
//                    editor.apply();
                }

            }
        });

        String enableNightMode    = settings.getString("enable_night_mode", ""+getResources().getString(R.string.ctrl_scr_night_mode_no));
        Log.v("enableNightMode", ""+enableNightMode);
        if(enableNightMode.equals(""+getResources().getString(R.string.ctrl_scr_night_mode_yes))){
            switchNightMode.setChecked(true);
        }
        else{
            switchNightMode.setChecked(false);
        }
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
                editor.putString("font_size", ""+getResources().getString(R.string.ctrl_scr_txt_size_small));
                editor.apply();
                changeFontSize(fontSize);;
                 break;
            case R.id.tv_btnMedium:
                fontSize = 2;
                editor.putString("font_size", ""+getResources().getString(R.string.ctrl_scr_txt_size_medium));
                editor.apply();
                changeFontSize(fontSize);
                 break;
            case R.id.tv_btnLarge:
                fontSize = 3;
                editor.putString("font_size", ""+getResources().getString(R.string.ctrl_scr_txt_size_large));
                editor.apply();
                changeFontSize(fontSize);
                 break;
            case R.id.tv_notfiication_on:
                notify = 1;
                editor.putString("notification", ""+getResources().getString(R.string.ctrl_scr_notfication_on));
                editor.apply();
                setNotification(notify);break;
            case R.id.tv_notfiication_off:
                notify = 0;
                editor.putString("notification", ""+getResources().getString(R.string.ctrl_scr_notfication_off));
                editor.apply();
                setNotification(notify);break;
            case R.id.tv_hd_images_on:
                hdImage = 1;
                editor.putString("hd_image", ""+getResources().getString(R.string.ctrl_scr_label_set_hd_images_on));
                editor.apply();
                setHdImage(hdImage);
                break;
            case R.id.tv_hd_images_off:
                hdImage = 0;
                editor.putString("hd_image", ""+getResources().getString(R.string.ctrl_scr_label_set_hd_images_off));
                editor.apply();
                setHdImage(hdImage);
                break;

            case R.id.ll_footer:
                updateProfile();
                break;
//            case R.id.swicth_night_mode:
//                changeToNightMode();
//                break;
            default:break;
        }
    }

    private void changeToNightMode(boolean status) {

        if(status){
            Log.v("chkImmoblizer","chkImmoblizer TRUE");
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            editor.putString("enable_night_mode", ""+getResources().getString(R.string.ctrl_scr_night_mode_yes));
            editor.apply();
        }
        else{
            Log.v("chkImmoblizer","chkImmoblizer FALSE");
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            editor.putString("enable_night_mode", ""+getResources().getString(R.string.ctrl_scr_night_mode_no));
            editor.apply();
        }
//

//        if(switchFirst){
//            Log.v("chkImmoblizer", "chkImmoblizer first");
//            switchFirst = false;
//        }
//        else{
//            if(status){
//                Log.v("chkImmoblizer","chkImmoblizer TRUE");
//            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
//                editor.putString("enable_night_mode", ""+getResources().getString(R.string.ctrl_scr_night_mode_yes));
//                editor.apply();
//            }
//            else{
//                Log.v("chkImmoblizer","chkImmoblizer FALSE");
//            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
//                editor.putString("enable_night_mode", ""+getResources().getString(R.string.ctrl_scr_night_mode_no));
//                editor.apply();
//            }
//            switchFirst = false;
//        }

//        Toast.makeText(mContext, "Night mode "+setNightMode, Toast.LENGTH_SHORT).show();


    }

    private void changeFontSize(int fontSize){
        String userFontSize    = settings.getString("font_size", ""+getResources().getString(R.string.ctrl_scr_txt_size_small));
        if(userFontSize.equals(""+getResources().getString(R.string.ctrl_scr_txt_size_small))){
            tvSmall.setBackground(mContext.getResources().getDrawable(R.drawable.btn_control_selected_drawable));
            tvMedium.setBackground(mContext.getResources().getDrawable(R.drawable.btn_control_normal_drawable));
            tvLarge.setBackground(mContext.getResources().getDrawable(R.drawable.btn_control_normal_drawable));

            tvSmall.setTextColor(mContext.getResources().getColorStateList(R.color.btn_control_text_selected_drawable));
            tvMedium.setTextColor(mContext.getResources().getColorStateList(R.color.btn_control_text_drawable));
            tvLarge.setTextColor(mContext.getResources().getColorStateList(R.color.btn_control_text_drawable));

        }

        else if(userFontSize.equals(""+getResources().getString(R.string.ctrl_scr_txt_size_large))){
            tvSmall.setBackground(mContext.getResources().getDrawable(R.drawable.btn_control_normal_drawable));
            tvMedium.setBackground(mContext.getResources().getDrawable(R.drawable.btn_control_normal_drawable));
            tvLarge.setBackground(mContext.getResources().getDrawable(R.drawable.btn_control_selected_drawable));

            tvSmall.setTextColor(mContext.getResources().getColorStateList(R.color.btn_control_text_drawable));
            tvMedium.setTextColor(mContext.getResources().getColorStateList(R.color.btn_control_text_drawable));
            tvLarge.setTextColor(mContext.getResources().getColorStateList(R.color.btn_control_text_selected_drawable));
        }

        else {
            tvSmall.setBackground(mContext.getResources().getDrawable(R.drawable.btn_control_normal_drawable));
            tvMedium.setBackground(mContext.getResources().getDrawable(R.drawable.btn_control_selected_drawable));
            tvLarge.setBackground(mContext.getResources().getDrawable(R.drawable.btn_control_normal_drawable));

            tvSmall.setTextColor(mContext.getResources().getColorStateList(R.color.btn_control_text_drawable));
            tvMedium.setTextColor(mContext.getResources().getColorStateList(R.color.btn_control_text_selected_drawable));
            tvLarge.setTextColor(mContext.getResources().getColorStateList(R.color.btn_control_text_drawable));
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
        String emoji = etEmoji.getText().toString();
        JSONObject objUpdateUser =   new JSONObject();
        try {
            objUpdateUser.put("action", "update_user_info");
            objUpdateUser.put("user_id", userId);
            objUpdateUser.put("user_emoji", emoji);
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

    public static String unescapeJava(String escaped) {

        if (escaped.indexOf("\\u") == -1)
            return escaped;

        String processed = "";

        int position = escaped.indexOf("\\u");
        while (position != -1) {
            if (position != 0)
                processed += escaped.substring(0, position);
            String token = escaped.substring(position + 2, position + 6);
            escaped = escaped.substring(position + 6);
            processed += (char) Integer.parseInt(token, 16);
            position = escaped.indexOf("\\u");
        }
        processed += escaped;

        return processed;
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
                    if(!objUser.optString("user_emoji").equals("")) {
                        etEmoji.setText(Html.fromHtml(objUser.optString("user_emoji")));
                    }

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
                    if(!objUser.optString("user_emoji").equals("")) {
                        userClass.setUserEmoji(""+objUser.optString("user_emoji"));
                    }
                    else{
                        userClass.setUserEmoji("");
                    }
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
                //startActivity(getIntent());
                //finish();
                getUserDetails();
                Log.v(TAG, ""+mContext.getResources().getString(R.string.profile_upadted));
            }
            else{
                Util.showMessageWithOk(mContext, ""+mContext.getResources().getString(R.string.profile_not_upadted));
            }
        }
        else{
            Util.showMessageWithOk(mContext, ""+getString(R.string.something_went_wrong)+"\n"+getString(R.string.please_try_again));
            return;

        }
    }
}
