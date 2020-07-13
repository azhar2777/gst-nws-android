package com.symmetrixsystems.gistapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import com.symmetrixsystems.gistapp.R;
import com.symmetrixsystems.gistapp.async.SelectChildTask;
import com.symmetrixsystems.gistapp.async.SelectionTask;
import com.symmetrixsystems.gistapp.customlists.ChildCategoryAdapter;
import com.symmetrixsystems.gistapp.customlists.CustomItemDecorator;
import com.symmetrixsystems.gistapp.listener.SelectedChildListener;
import com.symmetrixsystems.gistapp.model.ChildCategoryData;
import com.symmetrixsystems.gistapp.model.SelectedChild;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class CategorySelectionActivity extends AppCompatActivity implements SelectedChildListener {
    private Context mContext;
    private TextView tvSelectedCat;
    private RecyclerView rcvChildCategory;
    private ArrayList<ChildCategoryData> childCategories  =   new ArrayList<ChildCategoryData>();

    private ChildCategoryAdapter childCategoryAdapter;
    private int parentId;
    private String catName="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_selection);

        mContext    =   CategorySelectionActivity.this;

        tvSelectedCat   =   findViewById(R.id.selected_category);
        rcvChildCategory   =   findViewById(R.id.rcv_child_category);

        Bundle bundle = getIntent().getExtras();
        if(getIntent().hasExtra("parent_category_id"))
            parentId  =   bundle.getInt("parent_category_id");
        if(getIntent().hasExtra("parent_category_name")) {
            catName = bundle.getString("parent_category_name");
            if(!catName.equals("")){
                tvSelectedCat.setText("#"+catName.toUpperCase());
            }
        }
        getChildCategory(parentId);
    }

    private void getChildCategory(int parentId) {
        try{
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("action", "get_child");
            jsonBody.put("parent_cat", parentId);
            final String requestBody = jsonBody.toString();

            SelectChildTask selectChildTask = new SelectChildTask(mContext, requestBody);
            selectChildTask.mListener   =   this;


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void selectedChildCallBack(ArrayList<SelectedChild> selectChild) {
        if(selectChild.size() > 0){
            int errorCode   =   selectChild.get(0).getErrorCode();
            if(errorCode ==0){
                String categoryData =   selectChild.get(0).getChildCategoryData();

                try {
                    JSONArray arrCategories    =   new JSONArray(categoryData);
                    if(arrCategories.length() > 0){
                        for (int i=0; i < arrCategories.length(); i++){
                            ChildCategoryData childCategoryData = new ChildCategoryData();
                            if(arrCategories.getJSONObject(i) != null){
                                JSONObject iconObject   =   arrCategories.getJSONObject(i);
                                childCategoryData.setId(iconObject.optInt("id"));
                                childCategoryData.setCatName(iconObject.optString("category_name"));
                                childCategoryData.setCatImage(iconObject.optString("category_image"));
                                childCategories.add(childCategoryData);
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }

        childCategoryAdapter =  new ChildCategoryAdapter(mContext, childCategories, 0);
        rcvChildCategory.setAdapter(childCategoryAdapter);
        rcvChildCategory.addItemDecoration(new CustomItemDecorator(16));
        rcvChildCategory.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
    }
}
