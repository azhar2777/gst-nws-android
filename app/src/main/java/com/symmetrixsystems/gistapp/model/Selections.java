/*
 *
 * Login.java of CleanerApp for Properhands.
 *
 * Copyright (C) ProperHands Pte. Ltd - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Developed by vGrow Tech (http://vgrow.tech)
 * vijendra created/modified on 4/7/16 11:47 PM
 *
 */

package com.symmetrixsystems.gistapp.model;

import java.io.Serializable;

public class Selections implements Serializable {
    private int errorCode;
    private String iconCategoryData;

    public String getMainCategoryData() {
        return mainCategoryData;
    }

    public void setMainCategoryData(String mainCategoryData) {
        this.mainCategoryData = mainCategoryData;
    }

    private String mainCategoryData;

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getIconCategoryData() {
        return iconCategoryData;
    }

    public void setIconCategoryData(String iconCategoryData) {
        this.iconCategoryData = iconCategoryData;
    }
}
