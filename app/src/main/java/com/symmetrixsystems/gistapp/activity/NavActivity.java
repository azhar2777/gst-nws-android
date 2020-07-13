package com.symmetrixsystems.gistapp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Menu;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.symmetrixsystems.gistapp.R;
import com.symmetrixsystems.gistapp.fragment.StoryFragment;
import com.symmetrixsystems.gistapp.util.Util;


import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import java.util.ArrayList;


public class NavActivity extends AppCompatActivity implements View.OnClickListener {

    private String TAG = "NavActivityTAG";
    private Context mContext;
    private ImageView ivControl, ivReactLove, ivReactSurprised, ivReactSad, ivReactAngry;
    DrawerLayout drawerLayout;
    private static final String TAG_DETAIL_FRAGMENT = "TAG_DETAIL_FRAGMENT";
    NavigationView navigationView;

    String userId = "";

    int iconCategoryId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav);
        mContext    =   NavActivity.this;

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        Bundle bundle = getIntent().getExtras();
        if(getIntent().hasExtra("icon_cat"))
            iconCategoryId =   bundle.getInt("icon_cat");
        

        if (Util.fetchUserClass(mContext) != null && Util.fetchUserClass(mContext).getUserId() != null) {
            userId  =   Util.fetchUserClass(mContext).getUserId();
        }
        else {
            Intent splashIntent = new Intent(mContext, MainCategoryActivity.class);
            startActivity(splashIntent);
            return;
        }

        // insert detail fragment into detail container
        StoryFragment detailFragment = StoryFragment.newInstance();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.detail_fragment_container, detailFragment, TAG_DETAIL_FRAGMENT)
                .commit();





        /*Intent intent=getIntent();
        if(getIntent().hasExtra("categories_selected")) {
            ArrayList<Integer> categoriesSelected = intent.getIntegerArrayListExtra("categories_selected");
            if (categoriesSelected.size() > 0) {
                for (int category : categoriesSelected) {
                    Log.v(TAG, "category: " + category);
                }
            }
        }*/


        initView();


    }

    private void initView() {
        Log.v(TAG, "Inint view"+userId);
        /*ivReactLove =   navigationView.findViewById(R.id.iv_react_love);
        ivReactSurprised =   findViewById(R.id.iv_react_surprised);
        ivReactSad =   findViewById(R.id.iv_react_sad);
        ivReactAngry =   findViewById(R.id.iv_react_angry);



        ivReactSurprised.setOnClickListener(this);
        ivReactSad.setOnClickListener(this);
        ivReactAngry.setOnClickListener(this);
        ivReactAngry.setOnClickListener(this);*/

    }


    @Override
    public void onClick(View view) {
        /*switch (view.getId()){
            case R.id.iv_react_love :
                Log.v(TAG, "React Love clicked");
                drawerLayout.closeDrawer(Gravity.RIGHT);
                break;
            default:
                break;
        }*/
    }
}
