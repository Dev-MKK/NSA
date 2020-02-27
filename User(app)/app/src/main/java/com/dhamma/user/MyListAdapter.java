package com.dhamma.user;

import android.app.*;
import android.content.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import com.bumptech.glide.*;
import java.io.*;
import java.util.*;

public class MyListAdapter extends BaseAdapter
{ 
    Listener L; 
    Activity activity; 
    VideoDownloader.Listener downloadListener; 
    List<Video> videos; 

    public MyListAdapter(Activity activity, Listener l, VideoDownloader.Listener listener, List<Video> list) { 
        this.activity = activity; 
        this.videos = list; 
        L = l; 
        downloadListener = listener; 
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
        final Video video = videos.get(position); 
        LayoutInflater layoutInflater = (LayoutInflater)this.activity.getSystemService("layout_inflater"); 
        if (recycled == null) { 
            recycled = layoutInflater.inflate(R.layout.list_item, null); 
            holder = new ViewHolder(); 
            holder.imageView = (ImageView)recycled.findViewById(R.id.listitemimage); 
            holder.titleTv = (TextView)recycled.findViewById(R.id.listitemtitle); 
            holder.typeTv = (TextView)recycled.findViewById(R.id.listitemtype); 
            holder.deleteBtn = (ImageView)recycled.findViewById(R.id.listdelete); 
            holder.downloadBtn = (ImageView)recycled.findViewById(R.id.listdownload); 
            holder.playBtn = (ImageView)recycled.findViewById(R.id.listplay); 
            holder.progressBar = (ProgressBar)recycled.findViewById(R.id.listprogress); 
			
            recycled.setTag(holder); 
			
            holder.titleTv.setText((CharSequence)video.title); 
            holder.typeTv.setText((CharSequence)video.type); 
            holder.playBtn.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View v)
					{
						Intent intent = new Intent(activity, VideoPlayerActivity.class); 
						intent.putExtra("title", video.title); 
						intent.putExtra("monk", video.type); 
						intent.putExtra("url", video.date); 
						activity.startActivity(intent); 
					}
				});
            holder.downloadBtn.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View v)
					{
						v.setVisibility(View.GONE); 
						new VideoDownloader(activity, downloadListener, video).execute(); 
					}
				});
            holder.deleteBtn.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View v)
					{
						Toast.makeText(activity,"Press & Hold to delete.", Toast.LENGTH_LONG).show(); 
					}
				});
            holder.deleteBtn.setOnLongClickListener(new OnLongClickListener(){

					@Override
					public boolean onLongClick(View v)
					{
						File file = new File(activity.getFilesDir() + "/" + video.date);
						if(file.delete()) {
							Toast.makeText(activity,"Video deleted from storage. ",Toast.LENGTH_LONG).show(); 
							L.OnVideoRemoved(videos); 
						}
						return true;
					}
				});
            String filename = video.date; 
            try { 
                activity.openFileInput(filename).close(); 
                holder.playBtn.setVisibility(View.VISIBLE); 
                holder.deleteBtn.setVisibility(View.VISIBLE); 
            } catch (IOException iOException) { 
                holder.downloadBtn.setVisibility(View.VISIBLE); 
            } 
      
        } else { 
            holder = (ViewHolder)recycled.getTag(); 
        } 
		String img = video.image;
        if (!img.isEmpty() && img.startsWith("http")) { 
            Glide.with(activity)
				.load(img)
				.centerCrop()
				.placeholder(R.drawable.loading)
				.crossFade()
				.into(holder.imageView); 
        } 
        return recycled; 
    } 
	
	static interface Listener { 
        public void OnVideoRemoved(List<Video> list); 
    } 

    private class ViewHolder { 
        ImageView deleteBtn; 
        ImageView downloadBtn; 
        ImageView imageView; 
        ImageView playBtn; 
        ProgressBar progressBar; 
        TextView titleTv; 
        TextView typeTv; 
    } 
}
	
	
