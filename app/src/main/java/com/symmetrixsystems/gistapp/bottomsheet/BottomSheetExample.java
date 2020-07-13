package com.symmetrixsystems.gistapp.bottomsheet;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.symmetrixsystems.gistapp.R;

/**
 * Copyright - All Rights Reserved
 * Created by Mohammad Azharuddin on 13/03/2020.
 * Email mailsahil07@gmail.com
 */
public class BottomSheetExample extends BottomSheetDialogFragment {
    private BottomSheetBehavior bottomSheetBehavior;
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        View view = View.inflate(getContext(), R.layout.news_slide_next, null);


        dialog.setContentView(view);
        bottomSheetBehavior = BottomSheetBehavior.from((View) view.getParent());
        return dialog;
    }

    @Override
    public void onStart() {
        super.onStart();
        bottomSheetBehavior.setHideable(false);
        bottomSheetBehavior.setPeekHeight(500);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }
}
