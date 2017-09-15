package com.magcomm.floatview;

import android.app.Application;
import android.view.WindowManager;
/**
 * 
 * @author Yar
 * @website 
 * @date May 9, 2017
 * @version 1.0.0
 *
 */
public class FloatApplication extends Application {
	private WindowManager.LayoutParams windowParams = new WindowManager.LayoutParams();

	public WindowManager.LayoutParams getWindowParams() {
		return windowParams;
	}
}
