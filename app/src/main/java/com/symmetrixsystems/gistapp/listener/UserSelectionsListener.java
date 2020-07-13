/**
 * Created by Md Azharuddin on 03/03/2020.
 * Email mailsahil07@gmail.com
 * Developed by Symmetrix Systems
 */
package com.symmetrixsystems.gistapp.listener;


import com.symmetrixsystems.gistapp.model.Selections;
import com.symmetrixsystems.gistapp.model.UserSelections;

import java.util.ArrayList;


public interface UserSelectionsListener {
    void userSelectionsCallBack(ArrayList<UserSelections> userSelections);
}
