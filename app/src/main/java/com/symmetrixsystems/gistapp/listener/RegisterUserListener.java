/**
 * Created by Md Azharuddin on 03/03/2020.
 * Email mailsahil07@gmail.com
 * Developed by Symmetrix Systems
 */
package com.symmetrixsystems.gistapp.listener;


import com.symmetrixsystems.gistapp.model.GetStory;
import com.symmetrixsystems.gistapp.model.RegisterUser;

import java.util.ArrayList;


public interface RegisterUserListener {
    void registerUserCallBack(ArrayList<RegisterUser> registerUsers);
}
