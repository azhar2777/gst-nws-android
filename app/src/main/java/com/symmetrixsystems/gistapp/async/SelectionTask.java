package com.symmetrixsystems.gistapp.async;

import android.content.Context;
import android.util.Log;

import com.symmetrixsystems.gistapp.consts.Consts;
import com.symmetrixsystems.gistapp.listener.SelectionsListener;
import com.symmetrixsystems.gistapp.listener.VolleyCallback;
import com.symmetrixsystems.gistapp.model.Selections;
import com.symmetrixsystems.gistapp.util.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.symmetrixsystems.gistapp.consts.Consts.BASE_URL;
import static com.symmetrixsystems.gistapp.consts.Consts.GET_ICON_CAT;

/**
 * Copyright - All Rights Reserved
 * Created by Mohammad Azharuddin on 04/03/2020.
 * Email mailsahil07@gmail.com
 */
public class SelectionTask {
    Context mContext;
    private String requestString;
    private String TAG = "SelectionTaskTAG";
    public SelectionsListener mListener;
    private ArrayList<Selections> selections = new ArrayList<Selections>();
    public SelectionTask(Context mContext, String requestString){
        this.mContext = mContext;
        this.requestString = requestString;
        getIconCategories();
    }

    public void getIconCategories(){

        Util.postWithVolley(Consts.BASE_URL + Consts.SELECTIONS, requestString, mContext, new VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                parseResponse(result);
            }
        });

    }

    private void parseResponse(String strResponse) {
        Log.v(TAG, ""+strResponse);
        try {

            if(strResponse !="") {
                Log.v("" + TAG, "" + strResponse);

                JSONObject jObj = new JSONObject(strResponse);


                Selections selection = new Selections();
                selection.setErrorCode(jObj.optInt("error_code"));
                selection.setIconCategoryData(jObj.optString("icon_cat_data"));
                selection.setMainCategoryData(jObj.optString("main_cat_data"));
                selections.add(selection);
                mListener.selectionsCallBack(selections);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
