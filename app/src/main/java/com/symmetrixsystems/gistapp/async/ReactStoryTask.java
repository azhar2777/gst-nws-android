package com.symmetrixsystems.gistapp.async;

import android.content.Context;
import android.util.Log;

import com.symmetrixsystems.gistapp.consts.Consts;
import com.symmetrixsystems.gistapp.listener.ReactStoryListener;
import com.symmetrixsystems.gistapp.listener.VolleyCallback;
import com.symmetrixsystems.gistapp.model.ReactToStory;
import com.symmetrixsystems.gistapp.util.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Copyright - All Rights Reserved
 * Created by Mohammad Azharuddin on 04/03/2020.
 * Email mailsahil07@gmail.com
 */
public class ReactStoryTask {
    Context mContext;
    private String requestString;
    private String TAG = "ReactStoryTask";
    public ReactStoryListener mListener;
    private ArrayList<ReactToStory> reactToStories = new ArrayList<ReactToStory>();
    public ReactStoryTask(Context mContext, String requestString){
        this.mContext = mContext;
        this.requestString = requestString;
        doNetworkTask();
    }

    public void doNetworkTask(){

        Util.postWithVolley(Consts.BASE_URL + Consts.REACT_TO_STORY, requestString, mContext, new VolleyCallback() {
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


                ReactToStory reactToStory = new ReactToStory();
                reactToStory.setErrorCode(jObj.optInt("error_code"));
                reactToStory.setReactionType(jObj.optInt("reaction_type"));
                reactToStory.setMessage(jObj.optString("meesage"));
                reactToStories.add(reactToStory);
                mListener.reactStoryCallBack(reactToStories);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
