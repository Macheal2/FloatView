package com.magcomm.floatview;

/**
 * 
 * @author Yar
 * @website 
 * @date May 9, 2017
 * @version 1.0.0
 *
 */
 
import com.magcomm.inter.MenuCallback;
import com.magcomm.utils.CusAnimation;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class MenuItemView extends LinearLayout {
	private RelativeLayout relate_level1;
	private RelativeLayout relate_level2;
	private ImageButton mBackIB;
	private ImageButton mHomeIB;
	private ImageButton mMenuIB;
	private ImageButton mLockIB;
	private ImageButton mCancelIB;
	boolean isLeftSide = false;
	
	private OnClickListener listener;
	private MenuCallback callback;
	
	private int width;
	private int height;
	
	long mDownTime = 0;
	long mEventTime = 0;
	
	public MenuItemView(Context context, int isLeft) {
        super(context);
        setOrientation(LinearLayout.VERTICAL);// 水平排列

        //设置宽高
        this.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT));
        View root = null;
        if (isLeft == 0) {
        	root = LayoutInflater.from(context).inflate(  
                R.layout.left_menu, null);
        	isLeftSide = true;
        } else {
        	root = LayoutInflater.from(context).inflate(  
                    R.layout.right_menu, null);
        	isLeftSide = false;
        }
        this.addView(root);
		
        initView(root);
    }
	
	public void initView(View root) {
		relate_level1 = (RelativeLayout) root.findViewById(R.id.relate_level1);
		relate_level2 = (RelativeLayout) root.findViewById(R.id.relate_level2);
		
		mBackIB = (ImageButton) root.findViewById(R.id.back);
		mHomeIB = (ImageButton) root.findViewById(R.id.home);
		mMenuIB = (ImageButton) root.findViewById(R.id.menu);
		mLockIB = (ImageButton) root.findViewById(R.id.lock);
		mCancelIB = (ImageButton) root.findViewById(R.id.cancel);
		
		//startAnimationIn();
		
		int w = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);  
        int h = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);  
        relate_level2.measure(w, h);
        
        height = relate_level2.getMeasuredHeight();
        width = relate_level2.getMeasuredWidth();
	}
	
	public int getMenuItemViewWidth() {
		return width;
	}
	
	public int getMenuItemViewHeidth() {
		return height;
	}
	
	public void setOnMenuClickListener(OnClickListener listener) {
		mBackIB.setOnClickListener(listener);
		mHomeIB.setOnClickListener(listener);
		mMenuIB.setOnClickListener(listener);
		mLockIB.setOnClickListener(listener);
		mCancelIB.setOnClickListener(listener);
	}
	
	public void setClickable(boolean bool) {
		mBackIB.setClickable(bool);
		mHomeIB.setClickable(bool);
		mMenuIB.setClickable(bool);
		mLockIB.setClickable(bool);
		mCancelIB.setClickable(bool);
	}
	
	public void startAnimationIn() {
		CusAnimation.startAlphaAnimationsIn(relate_level1, 400);
		if (isLeftSide) {
			CusAnimation.startLeftAnimationsIn(relate_level2, 400);
		} else {
			CusAnimation.startRightAnimationsIn(relate_level2, 400);
		}
	}

	public void startAnimationOut() {
		CusAnimation.startAnimationsOut(relate_level1, 400, 0);
		CusAnimation.startAnimationsOut(relate_level2, 400, 0);
	}
	
	@Override
    public boolean onTouchEvent(MotionEvent event) {
        final int x = (int) event.getX();
        final int y = (int) event.getY();

        mDownTime = event.getDownTime();
		mEventTime = event.getEventTime();
		
        if ((event.getAction() == MotionEvent.ACTION_DOWN)
                && ((x < 0) || (x >= getWidth()) || (y < 0) || (y >= getHeight()))) {
        	Log.i("Yar", "1. onTouchEvent -> in");
            return true;
        } else if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
        	Log.i("Yar", "22. onTouchEvent -> out ,mEventTime = " + mEventTime + ", mDownTime = " + mDownTime);
        	/*if (mEventTime - mDownTime < 50) {
    			return false;
    		}*/
        	if (mCancelIB.isClickable()) {
        		callback.hideMenuView();
        	}
            return true;
        } else {
            return super.onTouchEvent(event);
        }
    }
	
	public void setCallBack(MenuCallback m) {
		callback = m;
	}
	
}
