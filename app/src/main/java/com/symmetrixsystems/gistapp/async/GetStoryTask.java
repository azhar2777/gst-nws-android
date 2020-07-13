package com.symmetrixsystems.gistapp.async;

import android.content.Context;
import android.util.Log;

import com.symmetrixsystems.gistapp.R;
import com.symmetrixsystems.gistapp.consts.Consts;
import com.symmetrixsystems.gistapp.custom.CustomAsynkLoader;
import com.symmetrixsystems.gistapp.listener.GetStoriesListener;
import com.symmetrixsystems.gistapp.listener.VolleyCallback;
import com.symmetrixsystems.gistapp.model.GetStory;
import com.symmetrixsystems.gistapp.model.Selections;
import com.symmetrixsystems.gistapp.util.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Copyright - All Rights Reserved
 * Created by Mohammad Azharuddin on 04/03/2020.
 * Email mailsahil07@gmail.com
 */
public class GetStoryTask {
    Context mContext;
    private String requestString;
    private String TAG = "GetStoryTaskTAG";
    public GetStoriesListener mListener;
    private ArrayList<GetStory> getStories = new ArrayList<GetStory>();



    public GetStoryTask(Context mContext, String requestString){
        this.mContext = mContext;
        this.requestString = requestString;



        doNetworkTask();
    }

    public void doNetworkTask(){

        Util.postWithVolley(Consts.BASE_URL + Consts.GET_STORIES, requestString, mContext, new VolleyCallback() {
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


                GetStory getStory = new GetStory();
                getStory.setErrorCode(jObj.optInt("error_code"));
                getStory.setMessage(jObj.optString("meesage"));
                getStory.setStories(jObj.optString("stories"));
                getStory.setIconCategory(jObj.optString("icon_category"));
                getStory.setUserSelections(jObj.optString("user_selections"));
                getStory.setDailyQuote(jObj.optString("daily_quotes"));
                getStories.add(getStory);
                mListener.getStoriesCallBack(getStories);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
