package com.symmetrixsystems.gistapp.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.symmetrixsystems.gistapp.R;
import com.symmetrixsystems.gistapp.async.AddToBookmarkTask;
import com.symmetrixsystems.gistapp.async.AnswerPollTask;
import com.symmetrixsystems.gistapp.async.GetStoryTask;
import com.symmetrixsystems.gistapp.async.ReactStoryTask;
import com.symmetrixsystems.gistapp.consts.Consts;
import com.symmetrixsystems.gistapp.custom.BlurBuilder;
import com.symmetrixsystems.gistapp.fragment.BottomSheet;
import com.symmetrixsystems.gistapp.listener.AddToBookmarkListener;
import com.symmetrixsystems.gistapp.listener.AnswerPollListener;
import com.symmetrixsystems.gistapp.listener.GetStoriesListener;
import com.symmetrixsystems.gistapp.listener.ReactStoryListener;
import com.symmetrixsystems.gistapp.model.AddToBookmark;
import com.symmetrixsystems.gistapp.model.AnswerPoll;
import com.symmetrixsystems.gistapp.model.GetStory;
import com.symmetrixsystems.gistapp.model.ReactToStory;
import com.symmetrixsystems.gistapp.util.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;




public class NewsNewActivity extends AppCompatActivity implements TextToSpeech.OnInitListener, View.OnClickListener, GetStoriesListener, ReactStoryListener, AddToBookmarkListener, AnswerPollListener, BottomSheet.ItemClickListener  {


    private int iconCategoryId;
    private Context mContext;
    private String TAG = "NewsNewActivityTAG";

    TextView tvSelectedItem;

    private TextToSpeech textToSpeech;
    private boolean isVolume = false;
    private int storyCounter    =   0, adId = 0;

    View roorView;
    private ConstraintLayout clStorySection, clAdImage, clStoryImage, clPollquestion;
    private TextView textView1, tvGistBy, tvPictureBy;
    private ImageView ivStory, ivStoryVolume, ivAvocado, ivControl, ivReactLove, ivReactSurprised, ivReactSad, ivReactAngry, ivBookmark;
    private VideoView vwStory;
    //===== Content provoder and share==
    private ImageView ivShareStory, ivAd, ivAdClose;

    private ImageView ivCP1, ivCP2, ivCP3;

    ProgressBar adProgressBar;

    private PopupWindow popUpShareContent;


    private BottomSheetBehavior mBottomSheetBehavior;
    private TextView mTextViewState;
    private TextView tvStoryTitle, tvStoryContent, tvNextTitle, tvPageTitle, tvPageTitleBelow;

    private ConstraintLayout clNextNews;
    private String userId = "";


    private int iconCat =0, nextStoryId =   0, currentStoryId=0;

    private  String storyText="";
    Bitmap imageBG;
    private String storyImageURL= "";
    private String currentStoryTitle= "", currentStoryURL;

    //===== POLL ==

    int questionId =0;
    private TextView tvPollQuestion;
    private RadioGroup rdgPollAnswer;
    private RadioButton rdbAnser1, rdbAnser2;
    String pollQuestion = "", pollOption1="", pollOption2="";

    String shareURL = "";
    ListView lv;
    AppAdapter adapter=null;
    List<ResolveInfo> launchables;

    Intent email = new Intent(Intent.ACTION_SEND);

    String dailyQuote="", userSelections="", iconCategoryName="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_new);

        mContext    =   NewsNewActivity.this;


        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            //your codes here

        }


        Bundle bundle = getIntent().getExtras();
        if(getIntent().hasExtra("icon_cat")) {
            iconCategoryId = bundle.getInt("icon_cat");

            Log.v(TAG, "iconCategoryId: "+iconCategoryId);
        }
        initView();
    }

    private void initView() {

        popUpShareContent = new PopupWindow(mContext);


        textToSpeech    =   new TextToSpeech(mContext, this);

        tvPageTitle = findViewById(R.id.tv_page_title);
        tvPageTitleBelow = findViewById(R.id.tv_page_title2);

        // Parent Layout
        clStorySection      =   findViewById(R.id.cl_story_section);
        clAdImage           =   findViewById(R.id.cl_ad_image);
        clStoryImage        =   findViewById(R.id.cl_story_image);
        clPollquestion        =   findViewById(R.id.cl_pollquestion);

        clStorySection.setVisibility(View.GONE);
        clAdImage.setVisibility(View.GONE);


        ivControl   =   findViewById(R.id.iv_control);
        ivControl.setOnClickListener(this);
        ivAvocado   =   findViewById(R.id.iv_nav_avacodo);
        ivAvocado.setOnClickListener(this);

        //== Story Section ==
        ivBookmark  =   findViewById(R.id.iv_bookmark);
        ivStory = findViewById(R.id.iv_story);
        vwStory = findViewById(R.id.vw_story);
        ivStoryVolume = findViewById(R.id.st_volume);
        tvStoryTitle = findViewById(R.id.tv_story_title);
        tvStoryContent = findViewById(R.id.tv_story_content);


        ivStoryVolume.setOnClickListener(this);
        ivBookmark.setOnClickListener(this);
        //== Story Section Ends ==

        //===== Content provoder and share==
        ivCP1   =   findViewById(R.id.iv_cp1);
        ivCP2   =   findViewById(R.id.iv_cp2);
        ivCP3   =   findViewById(R.id.iv_cp3);

        ivShareStory    =   findViewById(R.id.iv_share_story);
        ivAd    =   findViewById(R.id.iv_ad);
        ivAdClose    =   findViewById(R.id.iv_ad_close);

        adProgressBar = (ProgressBar) findViewById(R.id.progress);

        //== Story Next ==
        tvNextTitle =   findViewById(R.id.tv_next_title);
        clNextNews  =   findViewById(R.id.cl_next_news);

        clNextNews.setOnClickListener(this);
        //== Story Next ==


        tvGistBy    =   findViewById(R.id.tv_gist_by);
        tvPictureBy    =   findViewById(R.id.tv_picture_by);

        ivReactLove =   findViewById(R.id.iv_react_love);
        ivReactSurprised =   findViewById(R.id.iv_react_surprised);
        ivReactSad =   findViewById(R.id.iv_react_sad);
        ivReactAngry =   findViewById(R.id.iv_react_angry);

        ivShareStory.setOnClickListener(this);

        ivReactLove.setOnClickListener(this);
        ivReactSurprised.setOnClickListener(this);
        ivReactSad.setOnClickListener(this);
        ivReactAngry.setOnClickListener(this);


        ivAdClose.setOnClickListener(this);

        //== POLL  ==

        tvPollQuestion = findViewById(R.id.tv_polquestion);
        rdgPollAnswer= findViewById(R.id.rdg_poll_options);
        rdbAnser1 = findViewById(R.id.rdoption1);
        rdbAnser2 = findViewById(R.id.rdoption2);

        rdgPollAnswer.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                Log.v(TAG, "checked id "+i);

                int id=radioGroup.getCheckedRadioButtonId();


                RadioButton rdChecked =findViewById(id);
                if(rdChecked != null && rdChecked.isChecked()) {
                    String userAnswer = rdChecked.getText().toString();
                    Log.v(TAG, "userAnswer "+userAnswer);
                    answerPoll(userAnswer);
                }

//                RadioButton rdChecked =(RadioButton) findViewById(i);
//                if(rdChecked.isChecked()) {
//
//                    Toast.makeText(mContext, rdChecked.getText().toString(), Toast.LENGTH_SHORT).show();
//                }



            }
        });


       /* View bottomSheet = findViewById(R.id.bottom_sheet);

        mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);

        mTextViewState = findViewById(R.id.tv_page_title2);


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
                //shareStory(currentStoryTitle, currentStoryURL);

                //openSharePopup(shareURL);
                /*if(launchables != null && launchables.size() > 0){
                    launchables.clear();
                }
                showCustomChooser(shareURL);*/

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


    private void getNextNews() {


        if(!iconCategoryName.equals("")){
            tvPageTitleBelow.setText(iconCategoryName);
        }
        else if(!userSelections.equals("")){
            tvPageTitleBelow.setText(userSelections);
        }

        rdgPollAnswer.clearCheck();
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
            objGetNews.put("icon_category", iconCategoryId);

            String requestNews = objGetNews.toString();
            Log.v(TAG, "requestNews"+requestNews);

            GetStoryTask getStoryTask = new GetStoryTask(mContext, requestNews);
            getStoryTask.mListener  =   this;


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void getNews(String userId) {
        rdgPollAnswer.clearCheck();
        JSONObject objGetNews =   new JSONObject();
        try {
            objGetNews.put("action", "get_news");
            objGetNews.put("user_id", userId);
            objGetNews.put("icon_category", iconCategoryId);
            String requestNews = objGetNews.toString();
            Log.v(TAG, "requestNews"+requestNews);

            GetStoryTask getStoryTask = new GetStoryTask(mContext, requestNews);
            getStoryTask.mListener  =   this;


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onItemClick(String item) {
        tvSelectedItem.setText("Selected action item is " + item);
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

    @Override
    public void getStoriesCallBack(ArrayList<GetStory> getStories) {
        rdgPollAnswer.clearCheck();
        if(getStories.size() > 0){
            int errorCode = getStories.get(0).getErrorCode();
            dailyQuote = getStories.get(0).getDailyQuote();
            userSelections = getStories.get(0).getUserSelections();
            if(dailyQuote !="" && nextStoryId ==0){
                tvPageTitleBelow.setText(""+dailyQuote);
            }
            if(iconCategoryId > 0){
                iconCategoryName = getStories.get(0).getIconCategory();

            }



            if(errorCode == 0){
                clStorySection.setVisibility(View.VISIBLE);
                clAdImage.setVisibility(View.GONE);

                //adProgressBar.setVisibility(View.VISIBLE);

                String storyData    =   getStories.get(0).getStories();
                Log.v(TAG, storyData);

                try {
                    JSONObject objStoryData =   new JSONObject(storyData);

                    String currentStoryData = objStoryData.optString("story_data");




                    JSONObject objCurrentStory   =   new JSONObject(currentStoryData);

                    if(objCurrentStory.optInt("is_ad") ==0) {


                        //======== Current Story ========


                        currentStoryId = objCurrentStory.optInt("id");

                        shareURL= ""+objCurrentStory.optString("story_title");

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
                                Bitmap blurredBitmap = BlurBuilder.blur(mContext, imageBG);

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

                            String videoURL = objCurrentStory.optString("video_link");

                            videoURL = "https://imasdk.googleapis.com/js/core/bridge3.391.0_en.html#goog_1686398574";
                            Log.v(TAG, ""+videoURL);


                            MediaController mediaController= new MediaController(mContext);
                            mediaController.setAnchorView(vwStory);

                            //specify the location of media file
                            Uri uri=Uri.parse(videoURL);

                            //Setting MediaController and URI, then starting the videoView
                            vwStory.setMediaController(mediaController);
                            vwStory.setVideoURI(uri);
                            vwStory.start();
                            vwStory.requestFocus();



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


                        //========== POLL DATA ==============
                        questionId = objCurrentStory.optInt("ques_id");
                        if(questionId > 0){
                            rdgPollAnswer.clearCheck();
                            pollQuestion = objCurrentStory.optString("poll_question");
                            pollOption1 = objCurrentStory.optString("poll_option1");
                            pollOption2 = objCurrentStory.optString("poll_option2");
                            Log.v(TAG, ""+pollQuestion+"\n"+pollOption1+"\n"+pollOption2);

                            tvPollQuestion.setText(pollQuestion);
                            rdbAnser1.setText(pollOption1);
                            rdbAnser2.setText(pollOption2);


                            clPollquestion.setVisibility(View.VISIBLE);

                        }
                        else{
                            clPollquestion.setVisibility(View.GONE);
                        }




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


    private void answerPoll(String pollAnswer){
        Log.v(TAG, "answer is "+pollAnswer+" question id is"+questionId);

        JSONObject objPollAnswer =   new JSONObject();
        try {
            objPollAnswer.put("action", "answer_to_poll");
            objPollAnswer.put("user_id", userId);
            objPollAnswer.put("question_id", questionId);
            objPollAnswer.put("user_answer", pollAnswer);

            String requestAnswer = objPollAnswer.toString();
            Log.v(TAG, "requestAnswer: "+requestAnswer);

            AnswerPollTask answerPollTask = new AnswerPollTask(mContext, requestAnswer);
            answerPollTask.mListener    =   this;


        } catch (JSONException e) {
            e.printStackTrace();
        }


    }


    @Override
    public void answerPollCallBack(ArrayList<AnswerPoll> answerPolls) {
        if(answerPolls.size() > 0){
            int errorCode = answerPolls.get(0).getErrorCode();
            if(errorCode == 0){
                //Util.showMessageWithOk(mContext, ""+getString(R.string.poll_answered_successfully));
                return;
            }
            else{
                Util.showMessageWithOk(mContext, ""+getString(R.string.poll_answered_failed));
                return;
            }
        }
        else{
            Util.showMessageWithOk(mContext, ""+getString(R.string.something_went_wrong)+"\n"+getString(R.string.please_try_again));
            return;
        }
    }

   /* private void openSharePopup(final String shareURL){

        Intent sendIntent = new Intent(android.content.Intent.ACTION_SEND);
        sendIntent.setType("text/plain");
        List activities = this.getPackageManager().queryIntentActivities(sendIntent, 0);

        Log.v(TAG, "popup: "+activities.size());

        if (popUpShareContent == null || !popUpShareContent.isShowing()) {



            LayoutInflater layoutInflater = (LayoutInflater)
                    mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
            View popupView = layoutInflater.inflate(R.layout.popup_share, null);
            popUpShareContent = new PopupWindow(popupView, WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.MATCH_PARENT);


            ConstraintLayout layoutMain   =   (ConstraintLayout) findViewById(R.id.cl_story_section);

            lv=popupView.findViewById(R.id.lv_share);
            final ShareAdapter adapter=new ShareAdapter(mContext,activities.toArray());
            lv.setAdapter(adapter);

            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    ResolveInfo launchable=(ResolveInfo) adapter.getItem(i);
                    ActivityInfo activity=launchable.activityInfo;
                    ComponentName name=new ComponentName(activity.applicationInfo.packageName,
                            activity.name);
                    email.addCategory(Intent.CATEGORY_LAUNCHER);
                    email.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                            Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                    email.setComponent(name);
                    startActivity(email);
                    *//*popUpShareContent.dismiss();
                    ResolveInfo info = (ResolveInfo) adapter.getItem(i);
                    if(info.activityInfo.packageName.contains("facebook")) {
                        //new PostToFacebookDialog(mContext, shareURL).show();
                        //here u can write your own code to handle the particular social media networking apps.
                        Toast.makeText(getApplicationContext(), "FaceBook", Toast.LENGTH_LONG).show();
                    } else {
                        Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                        intent.setClassName(info.activityInfo.packageName, info.activityInfo.name);
                        intent.setType("text/plain");
                        intent.putExtra(Intent.EXTRA_SUBJECT, "subject");
                        intent.putExtra(Intent.EXTRA_TEXT, ""+shareURL);
                        ((Activity)mContext).startActivity(intent);
                    }*//*
                }
            });




            popUpShareContent.setFocusable(true);


            popUpShareContent.setTouchable(true);
            popUpShareContent.setOutsideTouchable(false);
            //popupSearch.setOutsideTouchable(false);
//            popUpShareContent.setWidth(1000);
//            popUpShareContent.setHeight(1000);

            popUpShareContent.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
            popUpShareContent.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);


            popUpShareContent.showAtLocation(layoutMain, Gravity.CENTER_HORIZONTAL, 0, 0);
            popUpShareContent.update();
        }
        else {
            popUpShareContent.dismiss();
        }
    }*/


    public  void showCustomChooser(String shareURL) {
        // TODO Auto-generated method stub
        final Dialog dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams WMLP = dialog.getWindow().getAttributes();
        WMLP.gravity = Gravity.CENTER;
        dialog.getWindow().setAttributes(WMLP);
        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.popup_share);
        dialog.setCancelable(true);
        lv=(ListView)dialog.findViewById(R.id.lv_share);
        PackageManager pm=getPackageManager();
        email.putExtra(Intent.EXTRA_EMAIL, new String[]{"velmurugan@androidtoppers.com"});
        email.putExtra(Intent.EXTRA_SUBJECT, "The Gistapp");
        email.putExtra(Intent.EXTRA_TEXT, ""+shareURL);
        email.setType("text/plain");

        launchables=pm.queryIntentActivities(email, 0);

        Collections.sort(launchables,
                new ResolveInfo.DisplayNameComparator(pm));

        adapter=new AppAdapter(pm, launchables);
        lv.setAdapter(adapter);
        /*lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long arg3) {

                Log.v(TAG, "Clicked Item"+position);
                // TODO Auto-generated method stub
                ResolveInfo launchable=adapter.getItem(position);
                ActivityInfo activity=launchable.activityInfo;
                ComponentName name=new ComponentName(activity.applicationInfo.packageName,
                        activity.name);
                email.addCategory(Intent.CATEGORY_LAUNCHER);
                email.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                        Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                email.setComponent(name);
                startActivity(email);
            }
        });*/
        dialog.show();
    }


    class AppAdapter extends ArrayAdapter<ResolveInfo> {
        private PackageManager pm=null;

        AppAdapter(PackageManager pm, List<ResolveInfo> apps) {
            super(mContext, R.layout.item_share, apps);
            this.pm=pm;
        }

        @Override
        public View getView(int position, View convertView,
                            ViewGroup parent) {
            if (convertView==null) {
                convertView=newView(parent);
            }

            bindView(position, convertView);

            return(convertView);
        }

        private View newView(ViewGroup parent) {
            return(getLayoutInflater().inflate(R.layout.item_share, parent, false));
        }

        private void bindView(final int position, View row) {
            TextView label=(TextView)row.findViewById(R.id.tv_share_item);

            label.setText(getItem(position).loadLabel(pm));

            ImageView icon=(ImageView)row.findViewById(R.id.iv_share_item);

            icon.setImageDrawable(getItem(position).loadIcon(pm));

            label.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(mContext, "Clicked", Toast.LENGTH_SHORT).show();
                    Log.v(TAG, "Clicked Item"+position);
                    // TODO Auto-generated method stub
                    ResolveInfo launchable=adapter.getItem(position);
                    ActivityInfo activity=launchable.activityInfo;
                    ComponentName name=new ComponentName(activity.applicationInfo.packageName,
                            activity.name);
                    email.addCategory(Intent.CATEGORY_LAUNCHER);
                    email.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                            Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                    email.setComponent(name);
                    startActivity(email);
                }
            });

        }
    }

}

