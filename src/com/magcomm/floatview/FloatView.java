package com.magcomm.floatview;

/**
 * 
 * @author Yar
 * @website 
 * @date May 9, 2017
 * @version 1.0.0
 *
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.database.ContentObserver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

public class FloatView extends Button {
	public static final int DELAY_TIME = 4000;// 4sec
	private float mTouchX;
	private float mTouchY;
	private float x;
	private float y;
	private float mStartX;
	private float mStartY;
	private long mDownTime;
	private long mEventTime;
	private OnClickListener mClickListener;
	private FloatViewObserver mObserver;
	public static boolean mBtnState = false;
	
	public static int RELATIVE_WIDTH = 0;
	private int iconW = 0;
	private int iconH = 0;
	
	private Object obj = new Object();

	private WindowManager windowManager = (WindowManager) getContext()
			.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
	// Global 'windowManagerParams'
	private WindowManager.LayoutParams windowManagerParams = ((FloatApplication) getContext()
			.getApplicationContext()).getWindowParams();
	private Context mContext;
	
	private SharedPreferences sp;
	SharedPreferences.Editor editor;
	
	public static final int SPEED = 50;			//control speed @Yar add
	public static final int SPEED_DELAY = 10;	//the time millisecond @Yar add
	public static final int ITEM_BG = 0;
	public static final int ITEM_LONG_C = 1;
	public static final int ITEM_SINGLE_C = 2;
	public static final int ITEM_MOVE_S = 3;

	public FloatView(Context context) {
		super(context);
		mContext = context;
		setDefaultBg();
		mHandler.removeMessages(0);
		mHandler.sendEmptyMessageDelayed(0, DELAY_TIME);
		mObserver = new FloatViewObserver(getContext());
		Uri halUri = Settings.System.getUriFor("virsual_key_value");
		getContext().getContentResolver().registerContentObserver(halUri, false, mObserver);
		sp = mContext.getSharedPreferences("float_view", Context.MODE_PRIVATE);
		editor = sp.edit();
		
		Bitmap bm = BitmapFactory.decodeResource(this.getResources(), R.drawable.home_pressed);
		iconW = bm.getWidth();
		iconH = bm.getHeight();
		
	}
	
	private boolean mHasLongClickPressed = false;
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);

			synchronized (obj) {
			switch (msg.what) {
			case ITEM_BG:
				//setBackgroundResource(R.drawable.home_unfocused);
				setUnfocusBg();
				mBtnState = true;
				break;
			case ITEM_LONG_C:
				performLongClick();
				mHasLongClickPressed = true;
				break;
			case ITEM_SINGLE_C:
			Log.i("Yar", "1. windowManagerParams.x = " + windowManagerParams.x);
				if (getVisibility() == View.VISIBLE && (windowManagerParams.x == 0 || (windowManagerParams.x >= RELATIVE_WIDTH && RELATIVE_WIDTH > 0))) 
				performClick();
				break;
			case ITEM_MOVE_S:			////set the 'move' speed @Yar add
				
				Log.i("Yar", "1. visibale = " + getVisibility());
				if (getVisibility() == View.VISIBLE) {
					windowManager.updateViewLayout(FloatView.this, windowManagerParams);
				}
				if (mWidth == 0) {
					windowManagerParams.x -= SPEED;
				} else {
					windowManagerParams.x += SPEED;
				}
				
				if ((windowManagerParams.x < mWidth && windowManagerParams.x > 0)
						|| (mWidth == 0 && windowManagerParams.x > -SPEED)) {
					//mHandler.removeMessages(ITEM_MOVE_S);
					mHandler.sendEmptyMessageDelayed(ITEM_MOVE_S, SPEED_DELAY);
				}
				break;
			}
			}
		}
		
	};

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// height of statusbar
		Rect frame = new Rect();
		getWindowVisibleDisplayFrame(frame);
		int statusBarHeight = frame.top;
		System.out.println("statusBarHeight:" + statusBarHeight);
		// relative position of screen
		x = event.getRawX();
		y = event.getRawY() - statusBarHeight;
		Log.i("tag", "1. currX = " + x + ", currY = " + y);
		int mWth = windowManager.getDefaultDisplay().getWidth();
		int mHgt = windowManager.getDefaultDisplay().getHeight();
		Log.i("tag", "2. mWth = " + mWth + ", mHgt = " + mHgt);
		Bitmap bm = BitmapFactory.decodeResource(this.getResources(), R.drawable.home_pressed);
		int width = bm.getWidth();
		int height = bm.getHeight();
		Log.i("tag", "3. width = " + width + ", height = " + height);
		
		//setBackgroundResource(R.drawable.home_pressed);
		if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
			;
		} else {
			setPressBg();
		}
		mBtnState = false;
		mDownTime = event.getDownTime();
		mEventTime = event.getEventTime();
		synchronized (obj) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN: // get the finger press action
			// the relative view coordinates
			mTouchX = event.getX();
			mTouchY = event.getY();
			mStartX = x;
			mStartY = y;
			
			Log.i("tag", "startX" + mTouchX + "====startY" + mTouchY);
			Log.i("Yar", "MotionEvent.ACTION_DOWN");
			
			//performLongClick();
			mHasLongClickPressed = false;

			break;

		case MotionEvent.ACTION_MOVE: // get the finger move action
			updateViewPosition();
			Log.i("Yar", "MotionEvent.ACTION_MOVE, mDownTime = " + mDownTime + ", mEventTime = " + mEventTime);
			if (!mHasLongClickPressed && (x - mStartX) < 5 && (y - mStartY) < 5 && (mEventTime - mDownTime > 1000)) {
				//if (!mHasLongClickPressed) performClick();
				performLongClick();
				mHasLongClickPressed = true;
			}
			break;

		case MotionEvent.ACTION_UP: //
			Log.i("Yar", "11. mEventTime = " + mEventTime + ", mDownTime = " + mDownTime);
			/*if (mEventTime - mDownTime < 35) {
				return false;
			}*/
			
			updateViewPosition(mWth-width, mHgt-height);
			
			mTouchX = mTouchY = 0;
			if (!mHasLongClickPressed && (x - mStartX) < 5 && (y - mStartY) < 5 && (mEventTime - mDownTime < 500)) {
				/*if (mClickListener != null) {
					mClickListener.onClick(this);
				}*/
				Log.i("Yar", "2. windowManagerParams.x = " + windowManagerParams.x);
				if (getVisibility() == View.VISIBLE && (windowManagerParams.x == 0 || (windowManagerParams.x >= RELATIVE_WIDTH && RELATIVE_WIDTH > 0)))
				performClick();
			}
			//setBackgroundResource(R.drawable.home_default);
			setDefaultBg();
			Log.i("Yar", "MotionEvent.ACTION_UP RELATIVE_WIDTH = " + RELATIVE_WIDTH);
			
			break;
		}
		}
		return true;//super.onTouchEvent(event);
	}

	/*@Override
	public void setOnClickListener(OnClickListener l) {
		this.mClickListener = l;
	}*/
	
	private void updateViewPosition() {
		// update parameters of the float window 
		windowManagerParams.x = (int) (x - mTouchX);
		windowManagerParams.y = (int) (y - mTouchY);
		windowManager.updateViewLayout(this, windowManagerParams); // refresh
	}
	
	int mWidth = 0;
	
	private void updateViewPosition(int width, int height) {
		if (x - mTouchX > width / 2) {
			windowManagerParams.x = width;// (int) (x - mTouchX);
		} else {
			windowManagerParams.x = 0;
		}
		
		mWidth = windowManagerParams.x;

		windowManagerParams.x = (int) (x - mTouchX);
		windowManagerParams.y = (int) (y - mTouchY);

		Log.i("Yar", "start -> " + (x - mTouchX) + ",  end -> " + windowManagerParams.x + ", mWidth = " + mWidth + ", windowManagerParams.y = " + windowManagerParams.y);
		//windowManager.updateViewLayout(this, windowManagerParams);
		mHandler.sendEmptyMessage(ITEM_MOVE_S);
		
		mHandler.removeMessages(ITEM_BG);
		mHandler.sendEmptyMessageDelayed(ITEM_BG, DELAY_TIME);
		editor.putFloat("float_x", mWidth);
		editor.putFloat("float_y", windowManagerParams.y);
		editor.commit();
	}

	/*@Override
	public boolean performLongClick() {
		// TODO Auto-generated method stub
		boolean bool = super.performLongClick();
		Log.i("Yar", "performLongClick = " + bool);
		return bool;
	}*/

	/*@Override
	public boolean performClick() {
		// TODO Auto-generated method stub
		Log.i("Yar", "performClick");
		return super.performClick();
	}*/

	private class FloatViewObserver extends ContentObserver {
		private Context mContext;
		public FloatViewObserver(Context context) {
			super(new Handler());
			// TODO Auto-generated constructor stub
			mContext = context;
		}

		@Override
		public void onChange(boolean selfChange) {
			// TODO Auto-generated method stub
			if (mBtnState) {
				setUnfocusBg();
			} else {
				setDefaultBg();
			}
		}
		
	}
	
	private void setPressBg() {
		int iv =  Settings.System.getInt(mContext.getContentResolver(), "virsual_key_value", 0);
		switch (iv) {
		case 0:
			setBackgroundResource(R.drawable.back_pressed);
			break;
		case 1:
			setBackgroundResource(R.drawable.home_pressed);
			break;
		case 2:
			setBackgroundResource(R.drawable.menu_pressed);
			break;
		case 3:
			setBackgroundResource(R.drawable.lock_pressed);
			break;
		case 4:
			setBackgroundResource(R.drawable.home_pressed);
			break;
		default:
			break;
		}
	}
	
	private void setDefaultBg() {
		int iv =  Settings.System.getInt(mContext.getContentResolver(), "virsual_key_value", 0);
		switch (iv) {
		case 0:
			setBackgroundResource(R.drawable.back_default);
			break;
		case 1:
			setBackgroundResource(R.drawable.home_default);
			break;
		case 2:
			setBackgroundResource(R.drawable.menu_default);
			break;
		case 3:
			setBackgroundResource(R.drawable.lock_default);
			break;
		case 4:
			setBackgroundResource(R.drawable.home_default);
			break;
		default:
			break;
		}
	}
	
	private void setUnfocusBg() {
		int iv =  Settings.System.getInt(mContext.getContentResolver(), "virsual_key_value", 0);
		switch (iv) {
		case 0:
			setBackgroundResource(R.drawable.back_unfocused);
			break;
		case 1:
			setBackgroundResource(R.drawable.home_unfocused);
			break;
		case 2:
			setBackgroundResource(R.drawable.menu_unfocused);
			break;
		case 3:
			setBackgroundResource(R.drawable.lock_unfocused);
			break;
		case 4:
			setBackgroundResource(R.drawable.home_unfocused);
			break;
		default:
			break;
		}
	}
	
	public int getIconWidth() {
		return iconW;
	}
	
	public int getIconHeight() {
		return iconH;
	}
}
