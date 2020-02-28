package com.dhamma.user;

import android.app.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import java.util.*;
import org.json.*;

public class MyVideosActivity extends Activity 
implements JsonGetter.Listener
{
	@Override
	public void OnJsonGot(String result)
	{
		JSONArray jarr; 
       	JSONObject jo;
        try {
			videosList = new ArrayList<>();
			jarr = new JSONArray(result);
            for(int i = jarr.length()-1; i >= 0; i--) {
           		jo = (JSONObject)jarr.get(i); 
            	Video video = new Video(); 
            	video.title = jo.getString("title"); 
            	video.type = jo.getString("type"); 
            	video.video = jo.getString("vlink"); 
            	video.image = jo.getString("mlink"); 
            	video.date = jo.getString("date"); 
            	videosList.add(video);
			}
			MyListAdapter adapter = new MyListAdapter(this, videosList); 
			listview.setAdapter(adapter); 

		} catch (JSONException joe) { 
			joe.printStackTrace();
		}
	}
	

    ListView listview; 
    List<Video> videosList; 

	@Override 
    protected void onCreate(Bundle bundle) { 
        super.onCreate(bundle); 
        setContentView(R.layout.main); 
        
		listview = from(R.id.lv); 
		
        if (NetworkChecker.isInternetOn(this)) { 
            new JsonGetter(this).execute(); 
            return; 
        } 
        Toast.makeText(this,"Check internet connection!", Toast.LENGTH_LONG).show(); 
    } 
	
    public <T extends View> T from(int n) { 
        return (T)findViewById(n); 
    } 
	
}
