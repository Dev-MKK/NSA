package com.dhamma.user;

import android.app.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import android.content.pm.*;

public class LogInActivity extends Activity
{ 
   
    @Override 
    protected void onCreate(Bundle bundle) { 
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); 
        requestWindowFeature(Window.FEATURE_NO_TITLE); 
        super.onCreate(bundle); 
        setContentView(R.layout.login); 
    } 
	
	public void logIn(View view) { 
		if(!NetworkChecker.isInternetOn(this)) {
			Toast.makeText(this,"Check internet connection",Toast.LENGTH_LONG).show();
			return;
		}
        startActivity(new Intent(this,MyVideosActivity.class)); 
        finish(); 
    } 
} 
