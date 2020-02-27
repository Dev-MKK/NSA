package com.dhamma.user;

import android.os.*;
import java.io.*;
import java.net.*;

public class JsonGetter extends AsyncTask<Void, Void, String>
{ 
    Listener L; 

    public JsonGetter(Listener listener) { 
        L = listener; 
    }

	@Override
	protected String doInBackground(Void...nothing)
	{

		StringBuilder sb = new StringBuilder();

		try  {
			URL Url = new URL(Setting.BASE_URL + Setting.JSON_FILE);
			HttpURLConnection con = (HttpURLConnection) Url.openConnection();
			InputStream is = con.getInputStream();
			sb.append(changeToString(is));
		} catch(IOException ioe) {

		}

		return sb.toString();
	}

	private String changeToString(InputStream is) {
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		String line;
		StringBuilder sb = new StringBuilder();
		try {
			while( (line = br.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException ioe) {
			sb.append(ioe.getMessage());
		}
		return sb.toString();
	}



	@Override 
    protected void onPostExecute(String result) { 
        L.OnJsonGot(result); 
    } 

    interface Listener { 
        public void OnJsonGot(String result); 
    } 
}
