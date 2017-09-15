package com.magcomm.floatview;

/**
 * 
 * @author Yar
 * @website 
 * @date May 9, 2017
 * @version 1.0.0
 *
 */

import android.database.ContentObserver;
import android.os.Handler;
import android.content.Intent;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.provider.Settings;
import android.util.Log;

public class MainActivity extends PreferenceActivity implements OnPreferenceChangeListener {
	PreferenceScreen mParent;
	private CheckBoxPreference mCbpAssistant;
	private ListPreference mLpDefault;
	final String KEY_PARENT = "parent";
    final String KEY_SCREEN_ASSISTANT = "key_screen_assistant";
    final String KEY_DEFAULT = "key_default";
	//public static boolean value = false;
	Intent intent;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.float_view);
        //setContentView(R.layout.activity_main);
        intent = new Intent(MainActivity.this, FxService.class);
        initView();
        initState();
        setChangeObserver();
        /*mEditText = (EditText) findViewById(R.id.et);
        mButton = (Button) findViewById(R.id.btn);
        mButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String str = mEditText.getText().toString();
				if (str != null || str == "") {
					int value = Integer.parseInt(str);
					Log.i("Yar", "value = " + value);
					Settings.System.putInt(getContentResolver(), "virsual_key", value);
				}
			}
		});*/
    }
	
	private void initView() {
		mParent = (PreferenceScreen) findPreference(KEY_PARENT);
        mCbpAssistant = (CheckBoxPreference) mParent.findPreference(KEY_SCREEN_ASSISTANT);
        mLpDefault = (ListPreference) mParent.findPreference(KEY_DEFAULT);
        
        mCbpAssistant.setOnPreferenceChangeListener(this);
        mLpDefault.setOnPreferenceChangeListener(this);
        
        if (mLpDefault != null) {
        	mParent.removePreference(mLpDefault);
        }
	}
	
	private void initState() {
		boolean isChecked = (Settings.System.getInt(getContentResolver(), "virsual_key_state", -1) == 1);
		mCbpAssistant.setChecked(isChecked);
		if (isChecked) {
			startService(intent);
		}
		int vv = Settings.System.getInt(getContentResolver(), "virsual_key_value", 0);
		mLpDefault.setValueIndex(vv);
		Log.i("Yar", "1. isChecked = " + isChecked + ", vv = " + vv);
		mLpDefault.setSummary(mLpDefault.getEntry());
	}

	@Override
	public boolean onPreferenceChange(Preference preference, Object newValue) {
		// TODO Auto-generated method stub
		Log.i("Yar", "------ onPreferenceChange ------");
		if (preference == mCbpAssistant) {
			/*if (newValue instanceof Boolean) {
				
			} */
			boolean isChecked = (Boolean)newValue;
			Settings.System.putInt(getContentResolver(), "virsual_key_state", isChecked ? 1 : 0);
			if (isChecked) {
				startService(intent);
				Log.i("Yar", "-- startService --");
			} else {
				boolean ret = stopService(intent);
				Log.i("Yar", "-- stopService -- ret = " + ret);
			}
			Log.i("Yar", "--newValue = " + newValue);
		} else if (preference == mLpDefault) {
			int val = Integer.valueOf(String.valueOf(newValue));
			Settings.System.putInt(getContentResolver(), "virsual_key_value", val);
			Log.i("Yar", "--mLpDefault-- val = " + val + ", mLpDefault.getEntry() = " + mLpDefault.getEntry());
			mLpDefault.setSummary(mLpDefault.getEntries()[val]);
		}
		return true;
	}
	
    //START: added by Yar
    private ContentObserver mFloatViewObserver = new ContentObserver(new Handler()) {  
        @Override  
        public void onChange(boolean selfChange) {  
        	setFloatViewUpdate();  
        }  
    };  
      
    public void setFloatViewUpdate() {
    	boolean isChecked = (Settings.System.getInt(getContentResolver(), "virsual_key_state", -1) == 1);
		mCbpAssistant.setChecked(isChecked);
    }
    
    public void setChangeObserver() {
    	getContentResolver().registerContentObserver(
    			Settings.System.getUriFor("virsual_key_state"), 
    			true, mFloatViewObserver);
    }
    //END: added by Yar

}
