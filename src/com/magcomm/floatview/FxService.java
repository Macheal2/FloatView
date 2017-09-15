package com.magcomm.floatview;

import android.app.Instrumentation;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.LinearLayout;

import com.magcomm.inter.MenuCallback;

import android.os.SystemProperties;

public class FxService extends Service {

	// 定义浮动窗口布局
	LinearLayout mFloatLayout;
	WindowManager.LayoutParams wmParams;
	// 创建浮动窗口设置布局参数的对象
	WindowManager mWindowManager;

	private FloatView floatView = null;
	private MenuItemView menuView = null;
	//private PopupWindow pw = null;
	Instrumentation instrumentation;

	private static final String TAG = "FxService";
	private SharedPreferences sp;
	
	private int iconWidth = 150;
	private int iconHeight = 150;
	Object obj = new Object();
	
	private long lastTimeF = 0;
	private long curTimeF = 0;
	
	private long lastTimeM = 0;
	private long curTimeM = 0;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		Log.i(TAG, "oncreat");
		createView();
		instrumentation = new Instrumentation();
		// Toast.makeText(FxService.this, "create FxService",
		// Toast.LENGTH_LONG);
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	private void createView() {
		
		floatView = new FloatView(getApplicationContext());
		
		// floatView.setOnClickListener(this);
		// floatView.setImageResource(R.drawable.ic_launcher); //
		// 这里简单的用自带的icon来做演示
		// 获取WindowManager
		mWindowManager = (WindowManager) getApplicationContext()
				.getSystemService(Context.WINDOW_SERVICE);
		// 设置LayoutParams(全局变量）相关参数
		wmParams = ((FloatApplication) getApplication()).getWindowParams();

		wmParams.type = LayoutParams.TYPE_PHONE; // 设置window type
		wmParams.format = PixelFormat.RGBA_8888; // 设置图片格式，效果为背景透明
		// 设置Window flag
		wmParams.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL | LayoutParams.FLAG_NOT_FOCUSABLE | LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH;
		/*
		 * 注意，flag的值可以为： 下面的flags属性的效果形同“锁定”。 悬浮窗不可触摸，不接受任何事件,同时不影响后面的事件响应。
		 * LayoutParams.FLAG_NOT_TOUCH_MODAL 不影响后面的事件
		 * LayoutParams.FLAG_NOT_FOCUSABLE 不可聚焦 LayoutParams.FLAG_NOT_TOUCHABLE
		 * 不可触摸
		 */
		// 调整悬浮窗口至左上角，便于调整坐标
		wmParams.gravity = Gravity.LEFT | Gravity.TOP;
		// 以屏幕左上角为原点，设置x、y初始值
		sp = getSharedPreferences("float_view", Context.MODE_PRIVATE);
		
		int w = mWindowManager.getDefaultDisplay().getWidth();
		int h = mWindowManager.getDefaultDisplay().getHeight();
		
		wmParams.x = (int)sp.getFloat("float_x", w);
		wmParams.y = (int)sp.getFloat("float_y", h/3);
		// 设置悬浮窗口长宽数据
		wmParams.width = iconWidth;
		wmParams.height = iconHeight;
		
		FloatView.RELATIVE_WIDTH = w - iconWidth / 3 * 2 / 2 - floatView.getIconWidth();
		
		// 显示myFloatView图像
		mWindowManager.addView(floatView, wmParams);

		floatView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.i("Yar", "onClick");
				curTimeF = System.currentTimeMillis();
				if (curTimeF - lastTimeF < 400) {
					return;
				}
				lastTimeF = curTimeF;
				
				floatView.setClickable(false);
				floatView.setVisibility(View.INVISIBLE);
				
				menuView = new MenuItemView(getApplicationContext(), wmParams.x);
				//pw = new PopupWindow(menuView, menuView.getMenuItemViewWidth(), menuView.getMenuItemViewHeidth(), true);
				
				wmParams.width = menuView.getMenuItemViewWidth();
				wmParams.height = menuView.getMenuItemViewHeidth();
				
				wmParams.y = wmParams.y - (menuView.getMenuItemViewHeidth() - iconHeight) / 2;
				/*int location_x = wmParams.x;
				int location_y = wmParams.y - (menuView.getMenuItemViewHeidth() - iconHeight) / 2;
				int location_y = (menuView.getMenuItemViewHeidth() - iconHeight) / 2;
				
				pw.showAtLocation(floatView, Gravity.LEFT | Gravity.TOP, location_x, location_y);
				pw.setOutsideTouchable(true);*/
				
				Log.i("Yar", "11. wmParams.width = " + wmParams.width + ", wmParams.height = " + wmParams.height + ", wmParams.x = " + wmParams.x);
				
				menuView.setOnMenuClickListener(menuListener);
				
				mWindowManager.removeView(floatView);
				
				menuView.startAnimationIn();
				mWindowManager.addView(menuView, wmParams);
				menuView.setVisibility(View.VISIBLE);
				menuView.setClickable(true);
				menuView.setCallBack(mCallback);

			}
		});

		floatView.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View arg0) {
				// TODO Auto-generated method stub
				Log.i("Yar", "onLongClick");
				//sendBroadcast(new Intent("custom_virtual_powerkey_long_click"));
                Intent i = new Intent();
				i.setClassName("com.magcomm.floatview", "com.magcomm.floatview.MainActivity");
				i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(i);
				return true;
			}
		});
		
		registerBroadreceivcer();
	}
	
	private OnClickListener menuListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			curTimeM = System.currentTimeMillis();
			Log.i(TAG, "-------------1 id = " + v.getId());
			if (curTimeM - lastTimeM < 400) {
				return;
			}
			lastTimeM = curTimeM;
			
			int id = v.getId();
			switch (id) {
			case R.id.back:
				sendKeyCode(KeyEvent.KEYCODE_BACK);
				break;
			case R.id.home:
				sendKeyCode(KeyEvent.KEYCODE_HOME);
				menuView.setClickable(false);
				hideMenu();
				break;
			case R.id.menu:
				if(SystemProperties.getBoolean("sys.powersave.surper",false)){
					hideMenu();
	                return;
	            }
				sendKeyCode(KeyEvent.KEYCODE_APP_SWITCH);
				menuView.setClickable(false);
				hideMenu();
				break;
			case R.id.lock:
				sendKeyCode(KeyEvent.KEYCODE_POWER);
				menuView.setClickable(false);
				hideMenu();
				break;
			case R.id.cancel:
				menuView.setClickable(false);
				hideMenu();
				break;
			}
		}
	};
	
	private MenuCallback mCallback = new MenuCallback() {
		
		@Override
		public void hideMenuView() {
			// TODO Auto-generated method stub
			hideMenu();
		}
	};
	
	public synchronized void hideMenu() {
		if (menuView != null) {
			menuView.setClickable(false);
			menuView.startAnimationOut();
		}
		mHandler.sendEmptyMessageDelayed(HIDE_MENU, 400);
	}
	
	private static final int HIDE_MENU = 1;
	
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case HIDE_MENU:
				if (menuView != null && menuView.getVisibility() == View.VISIBLE) {
                    mWindowManager.removeView(menuView);
                    menuView.setVisibility(View.INVISIBLE);
                    wmParams.y = (menuView.getMenuItemViewHeidth() - iconHeight) / 2 + wmParams.y;
				}
				
				wmParams.width = iconWidth;
				wmParams.height = iconHeight;
				
				if (floatView != null && floatView.getVisibility() == View.INVISIBLE) {
					mWindowManager.addView(floatView, wmParams);
                    floatView.setClickable(true);
                    floatView.setVisibility(View.VISIBLE);
				}
				break;
			}
			
		}
		
	};
	
	public void sendKeyCode(final int keyCode) {
		
		Thread t = new Thread(new Runnable() {

			@Override
			public void run() {
				int iv =  Settings.System.getInt(getContentResolver(), "virsual_key_value", 0);
				Log.i("Yar", "onClick iv = " + iv );
				instrumentation.sendKeyDownUpSync(keyCode);
			}
		});
		t.start();	
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
		int width = mWindowManager.getDefaultDisplay().getWidth();
		Log.i("YYY", "1. onConfigurationChanged width = " + width + ", wmParams.x = " + wmParams.x);
		if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
			if (wmParams.x > 0) {
				wmParams.x = width;
				
				FloatView.RELATIVE_WIDTH = wmParams.x - iconWidth / 3 * 2 / 2 - floatView.getIconWidth();
			}
			Log.i("YYY", "2. floatView " + floatView + ", visible = " + floatView.getVisibility());
			if (floatView != null && floatView.getVisibility() == View.VISIBLE) {
				mWindowManager.updateViewLayout(floatView, wmParams);
			} else if (menuView != null && menuView.getVisibility() == View.VISIBLE) {
				mWindowManager.updateViewLayout(menuView, wmParams);
			}
			//mWindowManager.updateViewLayout(floatView, wmParams);
		}
		FloatView.RELATIVE_WIDTH = width - iconWidth / 3 * 2 / 2 - floatView.getIconWidth();
		Log.i("YYY", "3. wmParams.x = " + wmParams.x + ", FloatView.RELATIVE_WIDTH = " + FloatView.RELATIVE_WIDTH);
		
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.i("Yar", "onDestroy" + floatView);
		if (floatView != null && floatView.getVisibility() == View.VISIBLE) {
			mWindowManager.removeView(floatView);
		}
		if (menuView != null && menuView.getVisibility() == View.VISIBLE) {
			mWindowManager.removeView(menuView);
		}
		unRegisterBroadreceiver();
	}
	
	private BroadcastReceiver mReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			String action = intent.getAction();
			if (action.equals(Intent.ACTION_SCREEN_OFF)) {
				hideMenu();
			}
		}
		
	};
	
	private void registerBroadreceivcer() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_SCREEN_OFF);
		registerReceiver(mReceiver, filter);
	}
	
	private void unRegisterBroadreceiver() {
		unregisterReceiver(mReceiver);
	}

}
