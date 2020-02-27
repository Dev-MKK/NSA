package com.dhamma.admin;

import android.os.*;
import java.io.*;
import java.net.*;
import java.util.*;
import javax.net.ssl.*;

public class ServerConnector extends AsyncTask<Void, Void, String> { 
    Listener L; 
    HashMap<String, String> postDataParams; 
    String server; 

    public ServerConnector(Listener listener, String url, HashMap<String, String> postData) { 
        L = listener; 
        server = url; 
        postDataParams = postData; 
    }

	@Override
	protected String doInBackground(Void...v){
		StringBuilder sb = new StringBuilder();
		try {
			URL url = new URL(server);

			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setReadTimeout(1500000);
			conn.setConnectTimeout(1500000);
			conn.setRequestMethod("POST");
			conn.setDoInput(true);
			conn.setDoOutput(true);
			
			OutputStream os = conn.getOutputStream();
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
			writer.write(formatPostData(postDataParams));

			writer.flush();
			writer.close();
			os.close();
			int responseCode = conn.getResponseCode();

			if (responseCode == HttpsURLConnection.HTTP_OK) {
				BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				sb = new StringBuilder();
				String response;
				while ((response = br.readLine()) != null){
					sb.append(response);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString();
		
	}
	
	private String formatPostData(HashMap<String, String> params) throws UnsupportedEncodingException {
		StringBuilder result = new StringBuilder();
		boolean first = true;
		for (Map.Entry<String, String> entry : params.entrySet()) {
			if (first)
				first = false;
			else
				result.append("&");

			result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
			result.append("=");
			result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
		}

		return result.toString();
	}

	
	@Override 
    protected void onPostExecute(String result) { 
        L.OnServerConnected(result); 
    } 

    interface Listener { 
        public void OnServerConnected(String result); 
    } 
}

 
