package com.dhamma.user;

import android.app.*;
import android.content.*;
import android.os.*;
import android.widget.*;
import java.io.*;
import java.net.*;

public class VideoDownloader extends AsyncTask<String, Integer, String> { 
    private Context context; 
    ProgressDialog mProgressDialog; 
    Video video; 
	MyListAdapter adapter;

    public VideoDownloader(Context context, MyListAdapter adapter, Video video) { 
        this.context = context; 
        this.video = video;
		this.adapter = adapter;
        init(); 
    } 

    private void init() { 
        mProgressDialog = new ProgressDialog(this.context); 
        mProgressDialog.setMessage("Downloading...\n" + video.title); 
        mProgressDialog.setIndeterminate(true); 
        mProgressDialog.setProgressStyle(1); 
        mProgressDialog.setCancelable(false); 
    }

	@Override
	protected String doInBackground(String ...s) {
		InputStream input = null;
		OutputStream output = null;
		HttpURLConnection connection = null;
		try {
			URL url = new URL(video.video);
			connection = (HttpURLConnection) url.openConnection();
			connection.connect();

			// expect HTTP 200 OK, so we don't mistakenly save error report
			// instead of the file
			if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
				return "Server returned HTTP " + connection.getResponseCode()
					+ " " + connection.getResponseMessage();
			}

			// this will be useful to display download percentage
			// might be -1: server did not report the length
			int fileLength = connection.getContentLength();

			// download the file
			input = connection.getInputStream();
			output = context.openFileOutput(video.date,context.MODE_PRIVATE);

			byte data[] = new byte[4096];
			long total = 0;
			int count;
			while ((count = input.read(data)) != -1) {
				// allow canceling with back button
				if (isCancelled()) {
					input.close();
					return null;
				}
				total += count;
				// publishing the progress....
				if (fileLength > 0) // only if total length is known
					publishProgress((int) (total * 100 / fileLength));
				output.write(data, 0, count);
			}
		} catch (Exception e) {
			return e.toString();
		} finally {
			try {
				if (output != null)
					output.close();
				if (input != null)
					input.close();
			} catch (IOException ignored) {
			}

			if (connection != null)
				connection.disconnect();
		}
		
		return null;
	}
 
	@Override 
    protected void onPostExecute(String result) { 
        mProgressDialog.dismiss(); 
        if (result != null) { 
            Toast.makeText(context,"Download error: " + result, Toast.LENGTH_LONG).show(); 
            return; 
        } 
        Toast.makeText(context,"Video downloaded",Toast.LENGTH_LONG).show();
		adapter.notifyDataSetChanged();
    } 

    @Override 
    protected void onPreExecute() { 
        super.onPreExecute(); 
        mProgressDialog.show(); 
    }

	@Override
	protected void onProgressUpdate(Integer...values) {
		super.onProgressUpdate(values); 
        mProgressDialog.setIndeterminate(false); 
        mProgressDialog.setMax(100); 
        mProgressDialog.setProgress(values[0]); 
    }
}
