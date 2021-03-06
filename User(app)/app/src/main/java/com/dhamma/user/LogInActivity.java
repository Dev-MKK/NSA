package com.dhamma.user;

import android.app.*;
import android.content.*;
import android.content.pm.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import java.io.*;
import java.net.*;
import java.util.*;
import javax.net.ssl.*;
import org.json.*;

public class LogInActivity extends Activity {
	
	EditText phoneEdt, passwordEdt;
	TextView tv;
   
    @Override 
    protected void onCreate(Bundle bundle) { 
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); 
        requestWindowFeature(Window.FEATURE_NO_TITLE); 
        super.onCreate(bundle); 
		
        setContentView(R.layout.login); 
		
		phoneEdt = from(R.id.phone);
		passwordEdt = from(R.id.password);
		tv = from(R.id.logintv);
    } 
	
	public void logIn(View view) { 
	
		String phone = phoneEdt.getText().toString();
		String password = passwordEdt.getText().toString();
		if(phone.isEmpty() || password.isEmpty()) {
			Toast.makeText(this,"Fill your login details",Toast.LENGTH_LONG).show();
			return;
		}
		if(!NetworkChecker.isInternetOn(this)) {
			Toast.makeText(this,"Check internet connection",Toast.LENGTH_LONG).show();
			return;
		}
		
		tv.setText("Logging in...");
        String api = Setting.BASE_URL + Setting.USER_LOGIN_PHP; 
		HashMap<String,String> postData = new HashMap<>(); 
		postData.put("phone", phone); 
		postData.put("password", password); 
		new LoginConnector(api,postData).execute(); 
		
    } 
	
	public <T extends View> T from(int n) { 
        return (T)findViewById(n); 
    } 
	
	/// Inner class

	public class LoginConnector extends AsyncTask<Void, Void, String> { 
		
		HashMap<String, String> postDataParams; 
		String server; 

		public LoginConnector( String url, HashMap<String, String> postData) { 
		
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
			try
			{
				JSONObject jo = new JSONObject(result);
				User user = new User();
				user.phone = jo.getString("phone");
				user.credits = jo.getInt("credits");
				Setting.USER = user;
				startActivity(new Intent(LogInActivity.this,MyVideosActivity.class)); 
				finish(); 
			} catch (JSONException je) {
				tv.setText("Login error. Try again!");
				Toast.makeText(LogInActivity.this,result,Toast.LENGTH_LONG).show();
			}
		} 

		
	} // Inner class

	
} 
