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

public class SelectedChild implements Serializable {
    private int errorCode;
    private String childCategoryData;

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getChildCategoryData() {
        return childCategoryData;
    }

    public void setChildCategoryData(String childCategoryData) {
        this.childCategoryData = childCategoryData;
    }
}
