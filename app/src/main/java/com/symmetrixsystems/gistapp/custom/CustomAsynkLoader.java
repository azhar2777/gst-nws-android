/*
 *
 *
 *
 */

package com.symmetrixsystems.gistapp.custom;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.BounceInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import com.symmetrixsystems.gistapp.R;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;


public class CustomAsynkLoader extends Dialog{

	Dialog mDialog;
	public CustomAsynkLoader(Context context){
		super(context);

		mDialog=new Dialog(context);
		mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		mDialog.getWindow ().setBackgroundDrawableResource(android.R.color.transparent);
		LayoutInflater layoutInflater = (LayoutInflater)
				context.getSystemService(LAYOUT_INFLATER_SERVICE);
		View v = layoutInflater.inflate(R.layout.custom_progress_image_dialog, null);
//		final ImageView bounceBallImage = v.findViewById(R.id.bounceBallImage);




//		bounceBallImage.clearAnimation();
//		TranslateAnimation transAnim = new TranslateAnimation(0, 0, 0,
//				getDisplayHeight()/2);
//		transAnim.setStartOffset(500);
//		transAnim.setDuration(3000);
//		transAnim.setFillAfter(true);
//		transAnim.setInterpolator(new BounceInterpolator());
//		transAnim.setAnimationListener(new Animation.AnimationListener() {
//
//			@Override
//			public void onAnimationStart(Animation animation) {
//				Log.i("ImageAnimation", "Starting button dropdown animation");
//
//			}
//
//			@Override
//			public void onAnimationRepeat(Animation animation) {
//				// TODO Auto-generated method stub
//
//			}
//
//			@Override
//			public void onAnimationEnd(Animation animation) {
//				Log.i("ImageAnimation",
//						"Ending button dropdown animation. Clearing animation and setting layout");
//				bounceBallImage.clearAnimation();
//				final int left = bounceBallImage.getLeft();
//				final int top = bounceBallImage.getTop();
//				final int right = bounceBallImage.getRight();
//				final int bottom = bounceBallImage.getBottom();
//				bounceBallImage.layout(left, top, right, bottom);
//
//			}
//		});
//		bounceBallImage.startAnimation(transAnim);

		mDialog.setContentView(R.layout.custom_progress_image_dialog);

//		startAnimation(bounceBallImage);

		mDialog.setCancelable(false);

//		mDialog.show();
	}

	public void ShowDialog(){
			mDialog.show();
	}

	public void DismissDialog(){
		mDialog.dismiss();
	}
	
	public boolean isDialogShowing(){
		return mDialog != null && mDialog.isShowing();
	}

	private int getDisplayHeight() {
		return getContext().getResources().getDisplayMetrics().heightPixels;
	}

	public void startAnimation(ImageView imageView) {
		Animation animation1 = AnimationUtils.loadAnimation(getContext(), R.anim.blink);
		imageView.startAnimation(animation1);
	}
}
