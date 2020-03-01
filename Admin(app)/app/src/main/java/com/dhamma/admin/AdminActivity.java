package com.dhamma.admin;

import android.app.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import java.util.*;

public class AdminActivity extends Activity 
implements ServerConnector.Listener { 
    String imageLink; 
    EditText imageLinkEdt; 
    String title; 
    EditText titleEdt; 
    String type; 
    EditText typeEdt; 
    String videoLink; 
    EditText videoLinkEdt; 

    
    @Override 
    public void OnServerConnected(String result) { 
        if (result.contains("Success")) { 
            titleEdt.setText(""); 
            typeEdt.setText(""); 
            videoLinkEdt.setText(""); 
            imageLinkEdt.setText(""); 
            Toast.makeText(this,"Video added sucessfully.",Toast.LENGTH_LONG).show(); 
            return; 
        } 
        Toast.makeText(this, result, Toast.LENGTH_LONG).show(); 
    } 
	
	@Override 
    protected void onCreate(Bundle bundle) { 
	
		requestWindowFeature(Window.FEATURE_NO_TITLE); 
        super.onCreate(bundle); 
        setContentView(R.layout.main); 
        titleEdt = from(R.id.title); 
        typeEdt = from(R.id.type); 
        videoLinkEdt = from(R.id.video); 
        imageLinkEdt = from(R.id.image); 
    } 

    public void add(View view) { 
        title = titleEdt.getText().toString(); 
        type = typeEdt.getText().toString(); 
        videoLink = videoLinkEdt.getText().toString(); 
        imageLink = imageLinkEdt.getText().toString(); 
        if (title.isEmpty() || type.isEmpty() || videoLink.isEmpty() || imageLink.isEmpty()) { 
            Toast.makeText(this,"Please fill all data.", Toast.LENGTH_LONG).show(); 
            return; 
        } 
        if (!NetworkChecker.isInternetOn((Context)this)) { 
            Toast.makeText(this, "Check Internet Connection.", Toast.LENGTH_LONG).show(); 
            return; 
        } 
        if (videoLink.contains("dhammadownload.com")) { 
            videoLink = videoLink.replace("dhammadownload.com","dd.dhamma.foc.lotayamm.com"); 
        } 
        /*if (this.imageLink.contains((CharSequence)"dhammadownload.com")) { 
            this.imageLink = this.imageLink.replace((CharSequence)"dhammadownload.com", (CharSequence)"dd.dhamma.foc.lotayamm.com"); 
        } */
        String url = Setting.BASE_URL + Setting.ADMIN_ADD_MOVIE_PHP;
        HashMap<String,String> postData = new HashMap<>(); 
        postData.put("title", title); 
        postData.put("type", type); 
        postData.put("vlink", videoLink); 
        postData.put("mlink", imageLink); 
        new ServerConnector(this, url, postData).execute(); 
    } 

    public <T extends View> T from(int n) { 
        return (T)this.findViewById(n); 
    } 

    public void goToGallery(View view) { 
        if (!NetworkChecker.isInternetOn(this)) { 
            Toast.makeText(this,"Check Internet Connection.", Toast.LENGTH_LONG).show(); 
            return; 
        } 
        startActivity(new Intent(this,AdminVideosActivity.class)); 
    } 
	
	public void toTopup(View view) { 
        startActivity(new Intent(this,TopupActivity.class)); 
    } 
} 
 
