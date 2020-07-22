package com.symmetrixsystems.gistapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.symmetrixsystems.gistapp.R;
import com.symmetrixsystems.gistapp.async.RegisterUserTask;
import com.symmetrixsystems.gistapp.listener.RegisterUserListener;
import com.symmetrixsystems.gistapp.model.RegisterUser;
import com.symmetrixsystems.gistapp.model.UserClass;
import com.symmetrixsystems.gistapp.util.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener, RegisterUserListener {

    private Context mContext;
    private String TAG = "RegisterActivityTAG";
    private TextView tvStep1, tvStep1Text, tvNameTap, tvEmailTap;
    private EditText etName, etEmail;

    private ConstraintLayout clRegFooter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mContext    =   RegisterActivity.this;

        initView();
    }

    private void initView() {
        tvNameTap   =   (TextView) findViewById(R.id.tv_name_tap);
        tvEmailTap   =   (TextView) findViewById(R.id.tv_email_tap);

        etName      =   (EditText)findViewById(R.id.et_name);
        etEmail      =   (EditText)findViewById(R.id.et_email);

        clRegFooter =   (ConstraintLayout)findViewById(R.id.cl_reg_footer);

        tvNameTap.setOnClickListener(this);
        tvEmailTap.setOnClickListener(this);
        clRegFooter.setOnClickListener(this);






    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_name_tap :
               etName.setVisibility(View.VISIBLE);

                break;

            case R.id.tv_email_tap :
                etEmail.setVisibility(View.VISIBLE);

                break;

            case R.id.cl_reg_footer :
                registerUser();

                break;
            default:
                break;
        }
    }

    // ========== Register User =====
    private void  registerUser(){
        String name= etName.getText().toString();
        String email = etEmail.getText().toString();
        String mDeviceId = Settings.Secure.getString(mContext.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        if(name.equals("")){
            Util.showMessageWithOk(mContext, ""+getString(R.string.reg_name_error));
            return;
        }
        if(email.equals("")){
            Util.showMessageWithOk(mContext, ""+getString(R.string.reg_email_error));
            return;
        }

        JSONObject objRegister = new JSONObject();
        try {
            objRegister.put("action", "create_user");
            objRegister.put("name", ""+name);
            objRegister.put("email", ""+email);
            objRegister.put("device_id", ""+mDeviceId);

            String requestRegister =    objRegister.toString();
            Log.v(TAG, requestRegister);

            RegisterUserTask registerUserTask = new RegisterUserTask(mContext, requestRegister);
            registerUserTask.mListener  =   this;

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void registerUserCallBack(ArrayList<RegisterUser> registerUsers) {
        if(registerUsers.size() > 0){
            int errorCode   =   registerUsers.get(0).getErrorCode();
            if(errorCode == 0){
                String userData =   registerUsers.get(0).getUserData();
                try {
                    JSONObject objUser  =   new JSONObject(userData);
                    Log.v(TAG, "User name: "+objUser.optString("name"));

                    int userId = objUser.optInt("id");
                    if(userId > 0){
                        UserClass userClass   =   new UserClass();
                        userClass.setUserId(""+userId);
                        userClass.setUserFullName(""+objUser.optString("name"));
                        userClass.setLoggedIn(true);
                        userClass.setHasSelection(false);

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

                        Intent intent = new Intent(mContext, MainCategoryActivity.class);
                        startActivity(intent);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                /**/
            }
            else if(errorCode ==1){
                Util.showMessageWithOk(mContext, ""+getString(R.string.register_invalid_post));
                return;
            }
            else if(errorCode ==2){
                Util.showMessageWithOk(mContext, ""+getString(R.string.register_invalid_name));
                return;
            }
            else if(errorCode ==3){
                Util.showMessageWithOk(mContext, ""+getString(R.string.register_invalid_email));
                return;
            }
            else if(errorCode ==4){
                Util.showMessageWithOk(mContext, ""+getString(R.string.register_failed));
                return;
            }
            else{
                Util.showMessageWithOk(mContext, ""+getString(R.string.something_went_wrong)+"\n"+getString(R.string.please_try_again));
                return;
            }
        }
        else{
            Util.showMessageWithOk(mContext, ""+getString(R.string.something_went_wrong)+"\n"+getString(R.string.please_try_again));
            return;

        }
    }
}
