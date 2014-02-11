package edu.wm.werewolfclient;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.JsonHttpResponseHandler;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


public class TabFragment1 extends Fragment {
	
	protected static final String TAG = "TabFrag1";

	private View view;
	private TextView username;
	private TextView role;
	private TextView status;
	
	private ArrayList<String> infoArray = new ArrayList<String>();

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }
        
        view = inflater.inflate(R.layout.tab_frag1_layout, container, false);
        
        try {
        	getInfo();
        } catch (JSONException e) {
        	e.printStackTrace();
        }
//      return (LinearLayout)inflater.inflate(R.layout.tab_frag1_layout, container, false);
        return view;
    }
    
    public void setFields() {
        username = (TextView) view.findViewById(R.id.usernameView);
        role = (TextView) view.findViewById(R.id.roleView);
        status = (TextView) view.findViewById(R.id.statusView);
        
        username.setText(Authenication.getUsername());
		role.setText(infoArray.get(0));
		status.setText(infoArray.get(1));
		
		
		Typeface type = Typeface.createFromAsset(username.getContext().getAssets(), "Bloodthirsty.ttf");
		username.setTypeface(type);
		Spannable span = new SpannableString(username.getText());
		span.setSpan(new RelativeSizeSpan(10f), 0, span.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		username.setText(span);
		
		Typeface type1 = Typeface.createFromAsset(role.getContext().getAssets(), "bloodcrow.ttf");
		role.setTypeface(type1);
		Spannable span1 = new SpannableString(role.getText());
		span1.setSpan(new RelativeSizeSpan(5f), 0, span1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		role.setText(span1);
		
		Typeface type2 = Typeface.createFromAsset(status.getContext().getAssets(), "bloodcrow.ttf");
		status.setTypeface(type2);
		Spannable span2 = new SpannableString(status.getText());
		span2.setSpan(new RelativeSizeSpan(5f), 0, span2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		status.setText(span2);
		

    }
    
    public void getInfo() throws JSONException {
    	Log.i(TAG, "HTTP Request to get User info...");
 		
 		// HTTP Request to get PlayersNearby
         WerewolfRestClient.get("/players/info", null, new JsonHttpResponseHandler() {
             @Override
             public void onSuccess(int statusCode,
                     org.apache.http.Header[] headers, JSONArray infoList) {
            	 Log.i(TAG, "...SUCCESS");
              	for (int i=0; i< infoList.length(); i++){
 					try {
 						String infoObj = (String) infoList.get(i);
 						infoArray.add(infoObj);
 					} catch (JSONException e) {
 						e.printStackTrace();
 					}
             	}
              	setFields();
             }
             
             @Override
             public void onFailure(int statusCode, org.apache.http.Header[] headers, 
            		 byte[] responseBody, Throwable error) {
            	 Log.i(TAG, "...FAILED");
            	 try {
					throw error;
				} catch (Throwable e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
             }
             
             @Override
             public void onRetry() {
                 Log.i(TAG, "...retrying...");
             }

             @Override
             public void onProgress(int bytesWritten, int totalSize) {
                Log.i(TAG, "...Progress (Bytes/TotalSize) = " + bytesWritten +"/"+ totalSize);
             }
             
             @Override
             public void onFinish() {
            	 Log.i(TAG, "...FINISHED");
             }
         });
    }
}