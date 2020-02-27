package com.dhamma.user;

import android.app.*;
import android.content.*;
import android.net.*;
import android.os.*;
import android.widget.*;
import android.content.pm.*;
import android.view.*;

public class VideoPlayerActivity extends Activity
 { 
    TextView m; 
    MediaController mc; 
    String monk,title, path; 
    TextView t; 
    VideoView video; 

    @Override 
    protected void onCreate(Bundle bundle) { 
        
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE); 
        requestWindowFeature(Window.FEATURE_NO_TITLE); 
        super.onCreate(bundle); 
        setContentView(R.layout.player); 
        
		title = getIntent().getStringExtra("title"); 
        monk = getIntent().getStringExtra("monk"); 
        path = getIntent().getStringExtra("url"); 
		
        video = (VideoView)findViewById(R.id.videoview); 
        t = (TextView)findViewById(R.id.title); 
        m = (TextView)findViewById(R.id.monk); 
        t.setText(title); 
        m.setText(monk); 
        
		mc = new MediaController(this); 
        video.setVideoURI(Uri.parse(getFilesDir() + "/" + path)); 
        video.setMediaController(mc); 
        video.start(); 
    } 
} 
