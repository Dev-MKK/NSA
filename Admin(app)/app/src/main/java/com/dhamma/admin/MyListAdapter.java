package com.dhamma.admin;

import android.app.*;
import android.content.*;
import android.view.*;
import android.widget.*;
import com.bumptech.glide.*;
import java.util.*;
import android.view.View.*;

public class MyListAdapter extends BaseAdapter 
implements ServerConnector.Listener { 
    Activity activity; 
    ServerConnector.Listener listener; 
    List<Video> videos; 
	Video deletedVideo;
	
    public MyListAdapter(Activity activity, List<Video> list) { 
        this.activity = activity; 
        this.listener = this; 
        videos = list; 
    } 

    @Override 
    public void OnServerConnected(String result) { 
        if (result.contains("deleted")) { 
			// The video on server side has been successfully deleted,
			// so now delete it from our list
			videos.remove(deletedVideo);
			// then refresh our list
			notifyDataSetChanged();
            Toast.makeText(this.activity,"Video deleted.", Toast.LENGTH_LONG).show(); 
            return; 
        } 
        Toast.makeText(activity, result, Toast.LENGTH_LONG).show(); 
    } 

    @Override 
    public int getCount() { 
        return videos.size(); 
    } 

    @Override 
    public Object getItem(int position) { 
        return videos.get(position); 
    } 

    @Override 
    public long getItemId(int position) { 
        return position; 
    } 

    public View getView(int position, View recycled, ViewGroup parent) { 
        ViewHolder holder = null;
         
        if (recycled == null) { 
			LayoutInflater layoutInflater = (LayoutInflater)this.activity.getSystemService("layout_inflater");
            recycled = layoutInflater.inflate(R.layout.list_item, null); 
            holder = new ViewHolder(); 
            holder.imageView = (ImageView)recycled.findViewById(R.id.listitemimage); 
            holder.titleTv = (TextView)recycled.findViewById(R.id.listitemtitle); 
            holder.typeTv = (TextView)recycled.findViewById(R.id.listitemtype); 
            holder.deleteBtn = (ImageView)recycled.findViewById(R.id.deleteBtn); 
            recycled.setTag(holder); 
        } else { 
            holder = (ViewHolder)recycled.getTag(); 
        } 
		
		final Video video = videos.get(position);
		
		holder.titleTv.setText(video.title); 
		holder.typeTv.setText(video.type); 
		holder.deleteBtn.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v)
				{
					Toast.makeText(activity,"Press & Hold to delete this video",Toast.LENGTH_LONG).show();
				}
			});
		holder.deleteBtn.setOnLongClickListener(new OnLongClickListener(){

				@Override
				public boolean onLongClick(View v)
				{
					// We will try to delete this video
					deletedVideo = video;
					// So we delete on the server side first
					String api = Setting.BASE_URL + Setting.ADMIN_DELETE_MOVIE_PHP; 
					HashMap<String,String> postData = new HashMap<>(); 
					postData.put("date", video.date); 
					new ServerConnector(listener, api,postData).execute(); 
					
					return true;
				}
			});
		
		String url = video.image;
        if (!url.isEmpty() && url.startsWith("http")) { 
            Glide.with(activity)
				.load(url)
				.centerCrop()
				.placeholder(R.drawable.loading)
				.crossFade()
				.into(holder.imageView); 
        } 
        return recycled; 
    } 

    private class ViewHolder { 
        ImageView deleteBtn; 
        ImageView imageView; 
        TextView titleTv; 
        TextView typeTv;
    } 
	
} 
