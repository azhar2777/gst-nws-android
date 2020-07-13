/**
 * Created by Md Azharuddin on 03/03/2020.
 * Email mailsahil07@gmail.com
 * Developed by Symmetrix Systems
 */
package com.symmetrixsystems.gistapp.listener;


import com.symmetrixsystems.gistapp.model.GetStory;
import com.symmetrixsystems.gistapp.model.Selections;

import java.util.ArrayList;


public interface GetStoriesListener {
    void getStoriesCallBack(ArrayList<GetStory> getStories);
}
