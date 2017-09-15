package com.magcomm.floatview;

/**
 * 
 * @author Yar
 * @website 
 * @date May 9, 2017
 * @version 1.0.0
 *
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.sax.StartElementListener;
import android.util.Log;

public class FxReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		String action = intent.getAction();
		Log.i("Yar", "---------floatview--------");
		boolean isChecked = (Settings.System.getInt(context.getContentResolver(), "virsual_key_state", -1) == 1);
		Log.i("Yar", "---------floatview--------" + isChecked + ", action = " + action);
		if (isChecked && Intent.ACTION_BOOT_COMPLETED.equals(action)) {
			Intent i = new Intent(context, FxService.class);
			context.startService(i);
		}
	}

}
