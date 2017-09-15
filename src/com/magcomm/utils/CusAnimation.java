package com.magcomm.utils;

/**
 * 
 * @author Yar
 * @website 
 * @date May 9, 2017
 * @version 1.0.0
 *
 */

import android.util.Log;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;

public class CusAnimation {
	
	//Right rotate animation in
	public static void startRightAnimationsIn(final ViewGroup viewgroup, int durationMillis) {

		viewgroup.setVisibility(0);
		for (int i = 0; i < viewgroup.getChildCount(); i++) {
			viewgroup.getChildAt(i).setVisibility(0);
			viewgroup.getChildAt(i).setClickable(false);
			viewgroup.getChildAt(i).setFocusable(false);
		}
		Animation animation;
		animation = new RotateAnimation(180, 0, Animation.RELATIVE_TO_SELF,
				1.0f, Animation.RELATIVE_TO_SELF, 0.5f);
		animation.setFillAfter(true);
		animation.setDuration(durationMillis);
		
		animation.setAnimationListener(new Animation.AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				for (int i = 0; i < viewgroup.getChildCount(); i++) {
					viewgroup.getChildAt(i).setClickable(false);
					viewgroup.getChildAt(i).setEnabled(false);
				}
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				for (int i = 0; i < viewgroup.getChildCount(); i++) {
					viewgroup.getChildAt(i).setClickable(true);
					viewgroup.getChildAt(i).setEnabled(true);
				}
			}
		});
		viewgroup.startAnimation(animation);

	}
	
	//Left rotate animation in
	public static void startLeftAnimationsIn(final ViewGroup viewgroup, int durationMillis) {

		viewgroup.setVisibility(0);
		for (int i = 0; i < viewgroup.getChildCount(); i++) {
			viewgroup.getChildAt(i).setVisibility(0);
			viewgroup.getChildAt(i).setClickable(false);
			viewgroup.getChildAt(i).setFocusable(false);
		}
		Animation animation;
		animation = new RotateAnimation(-180, 0, Animation.RELATIVE_TO_SELF,
				0f, Animation.RELATIVE_TO_SELF, 0.5f);
		animation.setFillAfter(true);
		animation.setDuration(durationMillis);
		
		animation.setAnimationListener(new Animation.AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				for (int i = 0; i < viewgroup.getChildCount(); i++) {
					viewgroup.getChildAt(i).setClickable(false);
					viewgroup.getChildAt(i).setEnabled(false);
				}
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				for (int i = 0; i < viewgroup.getChildCount(); i++) {
					viewgroup.getChildAt(i).setClickable(true);
					viewgroup.getChildAt(i).setEnabled(true);
				}
			}
		});
		
		viewgroup.startAnimation(animation);

	}
	
	//alpha animation in
	public static void startAlphaAnimationsIn(final ViewGroup viewgroup, int durationMillis) {

		viewgroup.setVisibility(0);
		for (int i = 0; i < viewgroup.getChildCount(); i++) {
			viewgroup.getChildAt(i).setVisibility(0);
			viewgroup.getChildAt(i).setClickable(false);
			viewgroup.getChildAt(i).setFocusable(false);
		}
		Animation animation;
		animation = new AlphaAnimation(0f, 1.0f);
		animation.setFillAfter(true);
		animation.setDuration(durationMillis);
		animation.setAnimationListener(new Animation.AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				for (int i = 0; i < viewgroup.getChildCount(); i++) {
					viewgroup.getChildAt(i).setClickable(false);
					viewgroup.getChildAt(i).setEnabled(false);
					Log.i("Yar", "1. ----start----id = " + viewgroup.getChildCount());
					Log.i("Yar", "1. ----start----id = " + viewgroup.getChildAt(i));
				}
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				for (int i = 0; i < viewgroup.getChildCount(); i++) {
					viewgroup.getChildAt(i).setClickable(true);
					viewgroup.getChildAt(i).setEnabled(true);
					Log.i("Yar", "2. ----start----id = " + viewgroup.getChildCount());
				}
			}
		});
		
		viewgroup.startAnimation(animation);

	}

	//alpha animation out
	public static void startAnimationsOut(final ViewGroup viewgroup,
			int durationMillis, int startOffset) {

		Animation animation;
		/*animation = new RotateAnimation(0, 180, Animation.RELATIVE_TO_SELF,
				1.0f, Animation.RELATIVE_TO_SELF, 0.5f);*/
		animation = new AlphaAnimation(1.0f, 0f);
		animation.setFillAfter(true);
		animation.setDuration(durationMillis);
		animation.setStartOffset(startOffset);
		animation.setAnimationListener(new Animation.AnimationListener() {
			@Override
			public void onAnimationStart(Animation arg0) {
				for (int i = 0; i < viewgroup.getChildCount(); i++) {
					viewgroup.getChildAt(i).setClickable(false);
					viewgroup.getChildAt(i).setFocusable(false);
				}
			}
			@Override
			public void onAnimationRepeat(Animation arg0) {}
			@Override
			public void onAnimationEnd(Animation arg0) {
				viewgroup.setVisibility(8);
				for (int i = 0; i < viewgroup.getChildCount(); i++) {
					viewgroup.getChildAt(i).setVisibility(8);
					viewgroup.getChildAt(i).setClickable(false);
					viewgroup.getChildAt(i).setFocusable(false);
				}
			}
		});
		viewgroup.startAnimation(animation);
	}

}
