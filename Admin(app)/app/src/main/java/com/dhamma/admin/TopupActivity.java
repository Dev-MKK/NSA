package com.dhamma.admin;


import android.app.*;
import android.content.pm.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import java.io.*;
import java.net.*;
import java.util.*;
import javax.net.ssl.*;

public class TopupActivity extends Activity
{

	EditText phoneEdt, amountEdt;
	TextView tv;

    @Override 
    protected void onCreate(Bundle bundle) { 
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); 
        requestWindowFeature(Window.FEATURE_NO_TITLE); 
        super.onCreate(bundle); 

        setContentView(R.layout.topup); 

		phoneEdt = from(R.id.tpphone);
		amountEdt = from(R.id.tpamount);
		tv = from(R.id.tptv);
    } 

	public void topup(View view) { 

		String phone = phoneEdt.getText().toString();
		String amount = amountEdt.getText().toString();
		if(phone.isEmpty() || amount.isEmpty()) {
			Toast.makeText(this,"Fill all data.",Toast.LENGTH_LONG).show();
			return;
		}
		if(!NetworkChecker.isInternetOn(this)) {
			Toast.makeText(this,"Check internet connection",Toast.LENGTH_LONG).show();
			return;
		}
		tv.setText("Topping up...");
      	new Topupper().execute();
    } 
	

	public <T extends View> T from(int n) { 
        return (T)findViewById(n); 
    } 
	
	// Inner class

	private class Topupper extends AsyncTask <Void, Void, String> { 

		public Topupper() {
	
		}

		@Override
		protected String doInBackground(Void...v){

			String api = Setting.BASE_URL + Setting.ADMIN_TOPUP_PHP;
			HashMap<String,String> postData = new HashMap<>(); 
			postData.put("phone", phoneEdt.getText().toString());
			postData.put("amount", amountEdt.getText().toString());

			StringBuilder sb = new StringBuilder();
			try {
				URL url = new URL(api);

				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setReadTimeout(1500000);
				conn.setConnectTimeout(1500000);
				conn.setRequestMethod("POST");
				conn.setDoInput(true);
				conn.setDoOutput(true);

				OutputStream os = conn.getOutputStream();
				BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
				writer.write(formatPostData(postData));

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
			if(result.contains("topupok")) {
				tv.setText("Account topped up.");
				amountEdt.setText("");
			} else {
				tv.setText(result);
			}
		} 
	} // Inner class
} 
