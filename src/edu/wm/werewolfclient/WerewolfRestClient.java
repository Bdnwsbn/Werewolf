package edu.wm.werewolfclient;

import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class WerewolfRestClient {

	protected static final String TAG = "WWRestClient";
	
	private static final String BASE_URL = "http://infinite-eyrie-7887.herokuapp.com";
	private static AsyncHttpClient client = new AsyncHttpClient();

	public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
		//	  client.setBasicAuth("ben", "1234");
		
		String username = Authenication.getUsername();
		String password = Authenication.getPassword();
		
		//Log.v(TAG + "get", "Username = " + username);
		//Log.v(TAG + "get", "Pw = " + password);
		
		if (username != null && !username.isEmpty()
				&& password != null && !password.isEmpty()) {
			
			client.setBasicAuth(username, password);
			
			Log.i(TAG + "get", "setBasicAuth success");
			
		}
		client.get(getAbsoluteUrl(url), params, responseHandler);
	}

	public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
		//client.setBasicAuth("ben", "1234");
		
		String username = Authenication.getUsername();
		String password = Authenication.getPassword();
		
		//Log.v(TAG + "post", "Username = " + username);
		//Log.v(TAG + "post", "Pw = " + password);
		
		if (username != null && !username.isEmpty()
				&& password != null && !password.isEmpty()) {
			client.setBasicAuth(username, password);
		
			Log.i(TAG + "post", "setBasicAuth success");
		
		}
		client.post(getAbsoluteUrl(url), params, responseHandler);
	}

	private static String getAbsoluteUrl(String relativeUrl) {
		return BASE_URL + relativeUrl;
	}

	public static void setBasicAuth(String username, String password) {
		client.setBasicAuth(username, password);
	}
}
