/**
 * Created by Md Azharuddin on 31/05/2020.
 * Email mailsahil07@gmail.com
 * Developed by Symmetrix Systems
 */
package com.symmetrixsystems.gistapp.listener;


import com.symmetrixsystems.gistapp.model.AddToBookmark;

import java.util.ArrayList;


public interface AddToBookmarkListener {
    void addToBookmarkCallBack(ArrayList<AddToBookmark> addToBookmarks);
}
