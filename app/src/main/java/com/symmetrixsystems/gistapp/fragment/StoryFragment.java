package com.symmetrixsystems.gistapp.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.symmetrixsystems.gistapp.R;
import com.symmetrixsystems.gistapp.activity.ControlActivity;
import com.symmetrixsystems.gistapp.activity.MainCategoryActivity;
import com.symmetrixsystems.gistapp.async.AddToBookmarkTask;
import com.symmetrixsystems.gistapp.async.GetStoryTask;
import com.symmetrixsystems.gistapp.async.ReactStoryTask;
import com.symmetrixsystems.gistapp.consts.Consts;
import com.symmetrixsystems.gistapp.custom.BlurBuilder;
import com.symmetrixsystems.gistapp.listener.AddToBookmarkListener;
import com.symmetrixsystems.gistapp.listener.GetStoriesListener;
import com.symmetrixsystems.gistapp.listener.ReactStoryListener;
import com.symmetrixsystems.gistapp.model.AddToBookmark;
import com.symmetrixsystems.gistapp.model.GetStory;
import com.symmetrixsystems.gistapp.model.ReactToStory;
import com.symmetrixsystems.gistapp.util.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Copyright - All Rights Reserved
 * Created by Mohammad Azharuddin on 17/03/2020.
 * Email mailsahil07@gmail.com
 */
public class StoryFragment extends Fragment implements TextToSpeech.OnInitListener, View.OnClickListener, GetStoriesListener, ReactStoryListener, AddToBookmarkListener, BottomSheet.ItemClickListener {

    private int iconCategoryId;
    private Context mContext;
    private String TAG = "StoryFragmentTAG";

    TextView tvSelectedItem;

    private TextToSpeech textToSpeech;
    private boolean isVolume = false;
    private int storyCounter    =   0, adId = 0;

    View roorView;
    private ConstraintLayout clStorySection, clAdImage, clStoryImage;
    private TextView textView1, tvGistBy, tvPictureBy;
    private ImageView ivStory, ivStoryVolume, ivAvocado, ivControl, ivReactLove, ivReactSurprised, ivReactSad, ivReactAngry, ivBookmark;
    private VideoView vwStory;
    //===== Content provoder and share==
    private ImageView ivShareStory, ivAd, ivAdClose;

    private ImageView ivCP1, ivCP2, ivCP3;

    ProgressBar adProgressBar;


    private BottomSheetBehavior mBottomSheetBehavior;
    private TextView mTextViewState;
    private TextView tvStoryTitle, tvStoryContent, tvNextTitle;

    private ConstraintLayout clNextNews;
    private String userId = "";


    private int iconCat =0, nextStoryId =   0, currentStoryId=0;

    private  String storyText="";
    Bitmap imageBG;
    private String storyImageURL= "";
    private String currentStoryTitle= "", currentStoryURL;

    public static StoryFragment newInstance() {

        return new StoryFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*Bundle bundle = this.getArguments();
        if (bundle != null) {
            Log.v(TAG, "iconCategory ");
            iconCat = bundle.getInt("icon_cat", 0);

            Log.v(TAG, "iconCat"+iconCat);
        }*/
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            Log.v(TAG, "iconCategory ");
            iconCat = bundle.getInt("icon_cat", 0);



        }

        Log.v(TAG, "iconCat: "+iconCat);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        roorView = inflater.inflate(R.layout.fragment_story, container, false);

        mContext    =   getContext();
        initView();
        return roorView;
    }

    private void initView() {




        textToSpeech    =   new TextToSpeech(mContext, this);

        // Parent Layout
        clStorySection      =   roorView.findViewById(R.id.cl_story_section);
        clAdImage           =   roorView.findViewById(R.id.cl_ad_image);
        clStoryImage        =   roorView.findViewById(R.id.cl_story_image);

        clStorySection.setVisibility(View.GONE);
        clAdImage.setVisibility(View.GONE);


        ivControl   =   roorView.findViewById(R.id.iv_control);
        ivControl.setOnClickListener(this);
        ivAvocado   =   roorView.findViewById(R.id.iv_nav_avacodo);
        ivAvocado.setOnClickListener(this);

        //== Story Section ==
        ivBookmark  =   roorView.findViewById(R.id.iv_bookmark);
        ivStory = roorView.findViewById(R.id.iv_story);
        vwStory = roorView.findViewById(R.id.vw_story);
        ivStoryVolume = roorView.findViewById(R.id.st_volume);
        tvStoryTitle = roorView.findViewById(R.id.tv_story_title);
        tvStoryContent = roorView.findViewById(R.id.tv_story_content);


        ivStoryVolume.setOnClickListener(this);
        ivBookmark.setOnClickListener(this);
        //== Story Section Ends ==

        //===== Content provoder and share==
        ivCP1   =   roorView.findViewById(R.id.iv_cp1);
        ivCP2   =   roorView.findViewById(R.id.iv_cp2);
        ivCP3   =   roorView.findViewById(R.id.iv_cp3);

        ivShareStory    =   roorView.findViewById(R.id.iv_share_story);
        ivAd    =   roorView.findViewById(R.id.iv_ad);
        ivAdClose    =   roorView.findViewById(R.id.iv_ad_close);

        adProgressBar = (ProgressBar) roorView.findViewById(R.id.progress);

        //== Story Next ==
        tvNextTitle =   roorView.findViewById(R.id.tv_next_title);
        clNextNews  =   roorView.findViewById(R.id.cl_next_news);

        clNextNews.setOnClickListener(this);
        //== Story Next ==


        tvGistBy    =   getActivity().findViewById(R.id.tv_gist_by);
        tvPictureBy    =   getActivity().findViewById(R.id.tv_picture_by);

        ivReactLove =   getActivity().findViewById(R.id.iv_react_love);
        ivReactSurprised =   getActivity().findViewById(R.id.iv_react_surprised);
        ivReactSad =   getActivity().findViewById(R.id.iv_react_sad);
        ivReactAngry =   getActivity().findViewById(R.id.iv_react_angry);

        ivShareStory.setOnClickListener(this);

        ivReactLove.setOnClickListener(this);
        ivReactSurprised.setOnClickListener(this);
        ivReactSad.setOnClickListener(this);
        ivReactAngry.setOnClickListener(this);


        ivAdClose.setOnClickListener(this);


       /* View bottomSheet = roorView.findViewById(R.id.bottom_sheet);

        mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);

        mTextViewState = roorView.findViewById(R.id.tv_page_title2);


        mBottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        mTextViewState.setText("Collapsed");
                        break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        mTextViewState.setText("Dragging...");
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        mTextViewState.setText("Expanded");
                        break;
                    case BottomSheetBehavior.STATE_HIDDEN:
                        mTextViewState.setText("Hidden");
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        mTextViewState.setText("Settling...");
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                Log.v(TAG, "slideOffset : "+slideOffset);
            }
        });
*/

        if (Util.fetchUserClass(mContext) != null && Util.fetchUserClass(mContext).getUserId() != null) {
            userId  =   Util.fetchUserClass(mContext).getUserId();
            getNews(userId);
        }

}


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_control :
                //Log.v(TAG, "Open Control screen");
                Intent intentControl    =   new Intent(mContext, ControlActivity.class);
                startActivity(intentControl);
                break;

            case R.id.iv_nav_avacodo:
                //Log.v(TAG, "Open Control screen");
                Intent intentSelection    =   new Intent(mContext, MainCategoryActivity.class);
                startActivity(intentSelection);
                break;
            case R.id.st_volume :

                if(!isVolume) {
                    ivStoryVolume.setImageResource(R.drawable.ic_volume_up);
                    speakOut();
                    isVolume    =   true;
                }
                else{
                    ivStoryVolume.setImageResource(R.drawable.ic_volume_off);
                    stopSpeak();
                }


                break;

            case R.id.iv_react_love :
                reactionOnStory(1);
                break;
            case R.id.iv_react_surprised :
                reactionOnStory(2);
                break;
            case R.id.iv_react_sad :
                reactionOnStory(3);
                break;
            case R.id.iv_react_angry :
                reactionOnStory(4);
                break;


            case R.id.cl_next_news :
                getNextNews();
                break;
            case R.id.iv_share_story :
                //Toast.makeText(mContext,"coming soon", Toast.LENGTH_SHORT).show();
                shareStory(currentStoryTitle, currentStoryURL);
                break;
            case R.id.iv_ad_close :
                clStorySection.setVisibility(View.VISIBLE);
                clAdImage.setVisibility(View.GONE);
                storyCounter =0;
                break;

            case R.id.iv_bookmark:
                bookMarkStory();
                break;
            default:
                break;
        }
    }

    private void showBottomSheet(){
        BottomSheet addPhotoBottomDialogFragment =
                BottomSheet.newInstance();
        addPhotoBottomDialogFragment.show(getFragmentManager(),
                BottomSheet.TAG);
    }

    private void getNextNews() {
        //Log.v(TAG, "getNext News");
        ivStoryVolume.setImageResource(R.drawable.ic_volume_off);
        stopSpeak();
        JSONObject objGetNews =   new JSONObject();
        try {
            objGetNews.put("action", "get_news");
            objGetNews.put("user_id", userId);
            objGetNews.put("news_id", nextStoryId);
            objGetNews.put("story_counter", storyCounter);
            objGetNews.put("ad_id", adId);
            objGetNews.put("icon_category", iconCat);

            String requestNews = objGetNews.toString();
            Log.v(TAG, "requestNews"+requestNews);

            GetStoryTask getStoryTask = new GetStoryTask(mContext, requestNews);
            getStoryTask.mListener  =   this;


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void getNews(String userId) {
        JSONObject objGetNews =   new JSONObject();
        try {
            objGetNews.put("action", "get_news");
            objGetNews.put("user_id", userId);
            objGetNews.put("icon_category", iconCat);
            String requestNews = objGetNews.toString();
            Log.v(TAG, "requestNews"+requestNews);

            GetStoryTask getStoryTask = new GetStoryTask(mContext, requestNews);
            getStoryTask.mListener  =   this;


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        setRetainInstance(true);
    }

    @Override
    public void getStoriesCallBack(ArrayList<GetStory> getStories) {
        if(getStories.size() > 0){
            int errorCode = getStories.get(0).getErrorCode();
            if(errorCode == 0){
                clStorySection.setVisibility(View.VISIBLE);
                clAdImage.setVisibility(View.GONE);

                //adProgressBar.setVisibility(View.VISIBLE);

                String storyData    =   getStories.get(0).getStories();
                //Log.v(TAG, storyData);

                try {
                    JSONObject objStoryData =   new JSONObject(storyData);

                    String currentStoryData = objStoryData.optString("story_data");




                    JSONObject objCurrentStory   =   new JSONObject(currentStoryData);

                    if(objCurrentStory.optInt("is_ad") ==0) {


                        //======== Current Story ========


                        currentStoryId = objCurrentStory.optInt("id");

                        tvStoryTitle.setText("" + objCurrentStory.optString("story_title"));
                        storyText = "" + objCurrentStory.optString("story_content");
                        tvStoryContent.setText("" + storyText);

                        currentStoryTitle   =   "" + objCurrentStory.optString("story_title");
                        currentStoryURL = "http://www.thegistapp.com/";

                        String imageUrl = "" + objCurrentStory.optString("img_video");
                        if(!objCurrentStory.optString("img_video").equals("")){
                            storyImageURL = Consts.STORY_IMAGES + imageUrl;
                        }

                        try {
                            URL url = new URL(Consts.STORY_IMAGES + imageUrl);


                            imageBG = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                            //BitmapDrawable ob = new BitmapDrawable(getResources(), imageBG);

                            //clStoryImage.setBackground(ob);

                            //ivStory.setBackground(ob);


                            if(imageBG != null) {
                                Bitmap blurredBitmap = BlurBuilder.blur(getActivity(), imageBG);

                                ivStory.setBackground(new BitmapDrawable(getResources(), blurredBitmap));
                            }


                        } catch(IOException e) {
                            System.out.println(e);
                        }

                        int isVideo =   objCurrentStory.optInt("is_video");

                        //Log.v(TAG, "Video "+isVideo);
                        if(isVideo ==1){
                            ivStory.setVisibility(View.GONE);
                            vwStory.setVisibility(View.VISIBLE);


                            MediaController mediaController= new MediaController(mContext);
                            mediaController.setAnchorView(vwStory);

                            //specify the location of media file
                            Uri uri=Uri.parse(Environment.getExternalStorageDirectory().getPath()+"/media/1.mp4");

                            //Setting MediaController and URI, then starting the videoView
                            vwStory.setMediaController(mediaController);
                            vwStory.setVideoURI(uri);
                            vwStory.requestFocus();
                            vwStory.start();



                        }
                        else {
                            ivStory.setVisibility(View.VISIBLE);
                            vwStory.setVisibility(View.GONE);
                            Glide.with(mContext.getApplicationContext())
                                    .load("" + Consts.STORY_IMAGES + imageUrl)
                                    .listener(new RequestListener<Drawable>() {
                                        @Override
                                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {

                                            return false;
                                        }

                                        @Override
                                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {

                                            //finalHolder.llProduct.setVisibility(View.VISIBLE);
                                            //finalHolder.llProgressBar.setVisibility(View.GONE);
                                            return false;
                                        }
                                    })
                                    .into(ivStory);
                        }

                        //====== Content provier =======

                        String cpLogo1 = "" + objCurrentStory.optString("provider_logo1");
                        String cpLogo2 = "" + objCurrentStory.optString("provider_logo2");
                        String cpLogo3 = "" + objCurrentStory.optString("provider_logo3");

                        if (cpLogo1.equals("")) {
                            ivCP1.setVisibility(View.GONE);
                        } else {

                            ivCP1.setVisibility(View.VISIBLE);
                            //Log.v(TAG, "CP1= " + Consts.STORY_CONTENT_POROVIDERS + cpLogo1);
                            Glide.with(mContext.getApplicationContext())
                                    .load("" + Consts.STORY_CONTENT_POROVIDERS + cpLogo1)
                                    .listener(new RequestListener<Drawable>() {
                                        @Override
                                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {

                                            return false;
                                        }

                                        @Override
                                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {

                                            //finalHolder.llProduct.setVisibility(View.VISIBLE);
                                            //finalHolder.llProgressBar.setVisibility(View.GONE);
                                            return false;
                                        }
                                    })
                                    .into(ivCP1);

                            final String cp1URL = "" + objCurrentStory.optString("content_provider_link1");
                            ivCP1.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(cp1URL));
                                    startActivity(browserIntent);
                                }
                            });
                        }

                        if (cpLogo2.equals("")) {
                            ivCP2.setVisibility(View.GONE);
                        } else {
                            ivCP2.setVisibility(View.VISIBLE);
                            //Log.v(TAG, "CP2= " + Consts.STORY_CONTENT_POROVIDERS + cpLogo2);
                            Glide.with(mContext.getApplicationContext())
                                    .load("" + Consts.STORY_CONTENT_POROVIDERS + cpLogo2)
                                    .listener(new RequestListener<Drawable>() {
                                        @Override
                                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {

                                            return false;
                                        }

                                        @Override
                                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {

                                            //finalHolder.llProduct.setVisibility(View.VISIBLE);
                                            //finalHolder.llProgressBar.setVisibility(View.GONE);
                                            return false;
                                        }
                                    })
                                    .into(ivCP2);

                            final String cp2URL = "" + objCurrentStory.optString("content_provider_link2");
                            ivCP2.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(cp2URL));
                                    startActivity(browserIntent);
                                }
                            });
                        }

                        if (cpLogo3.equals("")) {
                            ivCP3.setVisibility(View.GONE);
                        } else {
                            ivCP3.setVisibility(View.VISIBLE);
                            //Log.v(TAG, "CP3= " + Consts.STORY_CONTENT_POROVIDERS + cpLogo3);
                            Glide.with(mContext.getApplicationContext())
                                    .load("" + Consts.STORY_CONTENT_POROVIDERS + cpLogo3)
                                    .listener(new RequestListener<Drawable>() {
                                        @Override
                                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {

                                            return false;
                                        }

                                        @Override
                                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {

                                            //finalHolder.llProduct.setVisibility(View.VISIBLE);
                                            //finalHolder.llProgressBar.setVisibility(View.GONE);
                                            return false;
                                        }
                                    })
                                    .into(ivCP3);

                            final String cp3URL = "" + objCurrentStory.optString("content_provider_link3");
                            ivCP3.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(cp3URL));
                                    startActivity(browserIntent);
                                }
                            });
                        }


                        //Nav Panel
                        String storyWriter = "" + objCurrentStory.optString("writer");
                        tvGistBy.setText("" + storyWriter);

                        int isPhotographer = objCurrentStory.optInt("is_photographer");
                        if (isPhotographer == 1) {
                            tvPictureBy.setText("" + objCurrentStory.optString("photographer_name"));
                        } else {
                            tvPictureBy.setText("");
                        }
                        //====== Next Part =======

                        String nextStoryData = objStoryData.optString("next_story_data");

                        JSONObject objNextStory = new JSONObject(nextStoryData);

                        tvNextTitle.setText("" + objNextStory.optString("title"));

                        nextStoryId = objNextStory.optInt("id");

                        //Log.v(TAG, "Current story : " + objCurrentStory.optString("story_title") + "\nNext Story: " + objNextStory.optString("title"));


                        storyCounter++;

                        clAdImage.setVisibility(View.GONE);
                        clStorySection.setVisibility(View.VISIBLE);
                    }
                    else{
                        storyCounter    =   0;

                        String adData = objStoryData.optString("ad_data");
                        JSONObject objAdData = new JSONObject(adData);

                        final String adUrl    =   objAdData.optString("url");
                        String adImage    =   objAdData.optString("image");

                        adId    =   objAdData.optInt("id");


                        Glide.with(mContext.getApplicationContext())
                                .load("" + Consts.STORY_AD_IMAGES + adImage)
                                .listener(new RequestListener<Drawable>() {
                                    @Override
                                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                        adProgressBar.setVisibility(View.GONE);
                                        return false;
                                    }

                                    @Override
                                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {

                                        //finalHolder.llProduct.setVisibility(View.VISIBLE);
                                        //finalHolder.llProgressBar.setVisibility(View.GONE);
                                        adProgressBar.setVisibility(View.GONE);
                                        return false;
                                    }
                                })
                                .into(ivAd);

                        clAdImage.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(adUrl));
                                startActivity(browserIntent);
                            }
                        });




                        clAdImage.setVisibility(View.VISIBLE);
                        clStorySection.setVisibility(View.GONE);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
            else{

            }
        }
        else{
            Util.showMessageWithOk(mContext, ""+getString(R.string.something_went_wrong)+"\n"+getString(R.string.please_try_again));
            return;
        }
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {

            int result = textToSpeech.setLanguage(Locale.US);

            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "This Language is not supported");
            } else {

                speakOut();
            }

        } else {
            Log.e("TTS", "Initilization Failed!");
        }

    }

    private void speakOut() {

        textToSpeech.speak(storyText, TextToSpeech.QUEUE_FLUSH, null);
    }

    private void stopSpeak(){
        isVolume    =   false;
        if (textToSpeech != null) {
            textToSpeech.stop();
        }
    }


    //====== Story Reaction ========


    public void reactionOnStory(int reactionType){
        Log.v(TAG, "React "+reactionType);
        userId  =   Util.fetchUserClass(mContext).getUserId();

        JSONObject objReactOnNews =   new JSONObject();
        try {
            objReactOnNews.put("action", "react_to_story");
            objReactOnNews.put("user_id", userId);
            objReactOnNews.put("news_id", currentStoryId);
            objReactOnNews.put("reaction_type", reactionType);

            String requestReactNews = objReactOnNews.toString();
            Log.v(TAG, "requestReactNews"+requestReactNews);

            ReactStoryTask reactStoryTask = new ReactStoryTask(mContext, requestReactNews);
            reactStoryTask.mListener    =   this;


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void reactStoryCallBack(ArrayList<ReactToStory> reactToStories) {
        if(reactToStories.size() > 0){
            int errorCode   =   reactToStories.get(0).getErrorCode();
            if(errorCode ==0){
                int reactionType = reactToStories.get(0).getReactionType();
                if(reactionType ==1){
                    ivReactLove.setBackgroundColor(getResources().getColor(R.color.reaction_bg));

                    ivReactLove.setPadding(4,4,4,4);

                    ivReactSurprised.setBackgroundColor(0x00000000);
                    ivReactSad.setBackgroundColor(0x00000000);
                    ivReactAngry.setBackgroundColor(0x00000000);


                    ivReactSurprised.setPadding(0,0,0,0);
                    ivReactSad.setPadding(0,0,0,0);
                    ivReactAngry.setPadding(0,0,0,0);

                }
                else if(reactionType ==2){
                    ivReactSurprised.setBackgroundColor(getResources().getColor(R.color.reaction_bg));

                    ivReactSurprised.setPadding(4,4,4,4);


                    ivReactLove.setBackgroundColor(0x00000000);
                    ivReactSad.setBackgroundColor(0x00000000);
                    ivReactAngry.setBackgroundColor(0x00000000);

                    ivReactLove.setPadding(0,0,0,0);
                    ivReactSad.setPadding(0,0,0,0);
                    ivReactAngry.setPadding(0,0,0,0);

                }
                else if(reactionType ==3){
                    ivReactSad.setBackgroundColor(getResources().getColor(R.color.reaction_bg));
                    ivReactSad.setPadding(4,4,4,4);

                    ivReactLove.setBackgroundColor(0x00000000);
                    ivReactSurprised.setBackgroundColor(0x00000000);
                    ivReactAngry.setBackgroundColor(0x00000000);

                    ivReactLove.setPadding(0,0,0,0);
                    ivReactSurprised.setPadding(0,0,0,0);
                    ivReactAngry.setPadding(0,0,0,0);
                }
                else if(reactionType ==4){
                    ivReactAngry.setBackgroundColor(getResources().getColor(R.color.reaction_bg));
                    ivReactAngry.setPadding(4,4,4,4);

                    ivReactLove.setBackgroundColor(0x00000000);
                    ivReactSurprised.setBackgroundColor(0x00000000);
                    ivReactSad.setBackgroundColor(0x00000000);


                    ivReactLove.setPadding(0,0,0,0);
                    ivReactSurprised.setPadding(0,0,0,0);
                    ivReactSad.setPadding(0,0,0,0);

                }
            }
        }
        else{
            Util.showMessageWithOk(mContext, ""+getString(R.string.something_went_wrong)+"\n"+getString(R.string.please_try_again));
            return;
        }
    }

    @Override
    public void onItemClick(String item) {
        tvSelectedItem.setText("Selected action item is " + item);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            //your codes here

        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public void shareStory(String postTitle, String postURL){
        String shareUrl	=	postURL;

        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        share.putExtra(Intent.EXTRA_TEXT, shareUrl);

        mContext.startActivity(Intent.createChooser(share, ""+postTitle));
    }

    private void bookMarkStory(){
        userId  =   Util.fetchUserClass(mContext).getUserId();

        JSONObject objBookmark =   new JSONObject();
        try {
            objBookmark.put("action", "bookmark_story");
            objBookmark.put("user_id", userId);
            objBookmark.put("story_id", currentStoryId);

            String requestBookmark = objBookmark.toString();
            Log.v(TAG, "requestBookmark"+requestBookmark);

            AddToBookmarkTask addToBookmarkTask = new AddToBookmarkTask(mContext, requestBookmark);
            addToBookmarkTask.mListener    =   this;


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void addToBookmarkCallBack(ArrayList<AddToBookmark> addToBookmarks) {
        if(addToBookmarks.size() > 0) {
            int errorCode = addToBookmarks.get(0).getErrorCode();
            if (errorCode == 0) {
                Util.showMessageWithOk(mContext, getResources().getString(R.string.story_bookmarked));
            }
            else{
                Util.showMessageWithOk(mContext, getResources().getString(R.string.story_not_bookmarked));
            }
        }
        else{
            Util.showMessageWithOk(mContext, ""+getString(R.string.something_went_wrong)+"\n"+getString(R.string.please_try_again));
            return;
        }
    }
}
