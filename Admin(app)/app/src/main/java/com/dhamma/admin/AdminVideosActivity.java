package com.dhamma.admin;

import android.app.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import java.util.*;
import org.json.*;

public class AdminVideosActivity extends Activity 
implements JsonGetter.Listener
{ 
    ListView listview; 

    @Override 
    public void OnJsonGot(String result) { 
        JSONArray jarr; 
       	JSONObject jo;
        try {
			List<Video> videos = new ArrayList<>();
			jarr = new JSONArray(result);
            for(int i = jarr.length()-1; i >= 0; i--) {
           		jo = (JSONObject)jarr.get(i); 
            	Video video = new Video(); 
            	video.title = jo.getString("title"); 
            	video.type = jo.getString("type"); 
            	video.video = jo.getString("vlink"); 
            	video.image = jo.getString("mlink"); 
            	video.date = jo.getString("date"); 
            	videos.add(video);
			}
			MyListAdapter adapter = new MyListAdapter(this, videos); 
			listview.setAdapter(adapter); 
			
		} catch (JSONException joe) { 
			joe.printStackTrace();
		}
    } 

	@Override 
    protected void onCreate(Bundle bundle) { 
        super.onCreate(bundle); 
        setContentView(R.layout.videos); 
        listview = from(R.id.lv); 
        new JsonGetter(this).execute(); 
    } 

    public <T extends View> T from(int n) { 
        return (T)this.findViewById(n); 
    } 
} 
 
