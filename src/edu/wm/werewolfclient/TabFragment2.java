package edu.wm.werewolfclient;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;

import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;


public class TabFragment2 extends Fragment  {
	
	protected static final String TAG = "TabFrag2";
	
	private View view;
	
	private TextView username;
	private TextView role;
	private TextView status;
	private TextView textView1;
	private TextView textView2;
	private TextView textView3;
	private TextView lastHungText;
	private TextView playerHung;
	private TextView lastKilledText;
	private TextView playerKilled;
	private TextView currentScoreText;
	private TextView currentScore;
	
	private ImageView imageView1;
	
	private Drawable day;
	private Drawable night;
	
	private ArrayList<String> infoArray = new ArrayList<String>();
	
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }
        
        view = inflater.inflate(R.layout.tab_frag2_layout, container, false);
        
        textView1 = (TextView) view.findViewById(R.id.textView1);
        textView2 = (TextView) view.findViewById(R.id.textView2);
        textView3 = (TextView) view.findViewById(R.id.textView3);
        lastHungText = (TextView) view.findViewById(R.id.lastHungText);
        playerHung = (TextView) view.findViewById(R.id.playerHung);
        lastKilledText = (TextView) view.findViewById(R.id.lastKilledText);
        playerKilled = (TextView) view.findViewById(R.id.playerKilled);
        currentScoreText = (TextView) view.findViewById(R.id.currentScoreText);
        currentScore = (TextView) view.findViewById(R.id.currentScore);
        
        day = getResources().getDrawable(R.drawable.day);
        night = getResources().getDrawable(R.drawable.nighttowncrop);
        imageView1 = (ImageView) view.findViewById(R.id.imageView_tab2);
        
        imageView1.setOnTouchListener(new ImageView.OnTouchListener() {
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				if (imageView1.getDrawable().getConstantState()
						.equals(night.getConstantState())) {
					imageView1.setImageDrawable(day);
				} else {
					imageView1.setImageDrawable(night);
				}
				return false;
			}
        });
        
        setFont(textView1);
        setFont(textView2);
        setFont(textView3);
        setFont(lastHungText);
        setFont(playerHung);
        setFont(lastKilledText);
        setFont(playerKilled);
        setFont(currentScoreText);
        setFont(currentScore);
        
        try {
        	getInfo();
        } catch (JSONException e) {
        	e.printStackTrace();
        }
        
        return view;
    }
    public void setFields() {
        username = (TextView) view.findViewById(R.id.usernameView);
        role = (TextView) view.findViewById(R.id.roleView);
        status = (TextView) view.findViewById(R.id.statusView);
        
        username.setText(Authenication.getUsername());
		role.setText(infoArray.get(0));
		status.setText(infoArray.get(1));
		
		
		setFont(username);
		setFont(role);
		setFont(status);
		
		
		if (status.getText().toString().equalsIgnoreCase("Alive")) {
			status.setTextColor(-16711936);
		} else {
			status.setTextColor(-65536);
		}

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
    
    private void setFont(TextView text) {
		Typeface type = Typeface.createFromAsset(text.getContext().getAssets(), "bloodcrow.ttf");
		text.setTypeface(type);
		Spannable span = new SpannableString(text.getText());
		span.setSpan(new RelativeSizeSpan(2f), 0, span.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		text.setText(span);
    }
}