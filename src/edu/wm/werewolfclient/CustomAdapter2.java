package edu.wm.werewolfclient;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.JsonHttpResponseHandler;

import android.app.Activity;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class CustomAdapter2 extends ArrayAdapter<String>{
	
	protected static final String TAG = "CustomAdapter2";
	
	private Activity context;
	private int layoutResourceId;
//	private ArrayList<Player> data = new ArrayList<Player>();
	private ArrayList<String> data = new ArrayList<String>();
	

	public CustomAdapter2(Activity context, int layoutResourceId, ArrayList<String> data) {
		super(context, layoutResourceId, data);
		this.context = context;
		this.layoutResourceId = layoutResourceId;
		this.data = data;
	}
	
	static class PlayerHolder {
		TextView text;
		Button Votebtn;
		Button Killbtn;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		if(view == null) {
			LayoutInflater inflator = context.getLayoutInflater();
			view = inflator.inflate(layoutResourceId, parent, false);
			final PlayerHolder playerHolder = new PlayerHolder();
			playerHolder.text = (TextView) view.findViewById(R.id.textView);
			playerHolder.Votebtn = (Button) view.findViewById(R.id.register_button);
			playerHolder.Killbtn = (Button) view.findViewById(R.id.button2);
			view.setTag(playerHolder);
		}
		
		else {
			((PlayerHolder) view.getTag()).Votebtn.setTag(data.get(position));
			((PlayerHolder) view.getTag()).Killbtn.setTag(data.get(position));
		}
		
		final PlayerHolder holder = (PlayerHolder) view.getTag();
		//Player player = data.get(position);
		String player = data.get(position);
//		holder.text.setText(player.getName());
		holder.text.setText(player);
		
		setText(holder.text);
		
		holder.Votebtn.setOnClickListener(new OnClickListener(){
			// Vote player
			@Override
			public void onClick (View v) {
				Log.i(TAG, "HTTP Request to vote...");
					
				Log.v(TAG, "holder.text = " + holder.text.getText());
				Log.v(TAG, "/players/votes/" + holder.text.getText());

				WerewolfRestClient.post("/players/votes/" + holder.text.getText(), null, new JsonHttpResponseHandler() {
		             @Override
		             public void onSuccess(int statusCode, org.apache.http.Header[] headers,
		                     org.json.JSONObject response) {
		            	 Log.i(TAG, "...SUCCESS");
		            	 
		            	 try {
		            		 String status = response.getString("status");
		            		 
		            		 if (status.equals("success")) {
		            			 Log.i(TAG, "Vote successful");
		            			 Toast.makeText(context, "You voted for player: " + holder.text.getText(), Toast.LENGTH_SHORT).show();
		            		 } else if (status.equals("failure")) {
		            			 Log.i(TAG, "Vote failed");
		            		 } else {
		            			 Log.i(TAG, "Vote status unknown");
		            		 }
							 
						} catch (JSONException e) {
							e.printStackTrace();
						}
		             }
		             
		             @Override
		             public void onFailure(int statusCode, java.lang.Throwable error,
		                     java.lang.String content){
		            	 Log.i(TAG, "...FAILED");
		            	 try {
							throw error;
						} catch (Throwable e) {
							e.printStackTrace();
						} 	 
		             }
		             
		             @Override
		             public void onRetry() {
		                 Log.i(TAG, "...retrying ....");
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
		});
		
		
		holder.Killbtn.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				Log.i(TAG, "HTTP Request to kill...");
		
				WerewolfRestClient.post("/players/kill/" + holder.text.getText(), null, new JsonHttpResponseHandler() {
					@Override
		             public void onSuccess(int statusCode, org.apache.http.Header[] headers,
		                     org.json.JSONObject response) {
		            	 Log.i(TAG, "...SUCCESS");
		            	 
		            	 try {
		            		 String status = response.getString("status");
		            		 
		            		 if (status.equals("success")) {
		            			 Log.i(TAG, "Kill successful");
		            			 Toast.makeText(context, "You killed player: " + holder.text.getText(), Toast.LENGTH_SHORT).show();
		            		 } else if (status.equals("failure")) {
		            			 Log.i(TAG, "Kill failed");
		            		 } else {
		            			 Log.i(TAG, "Kill status unknown");
		            		 }
							 
						} catch (JSONException e) {
							e.printStackTrace();
						}
		             }
					
					@Override
					 public void onFailure(int statusCode, java.lang.Throwable error,
		                     java.lang.String content){
		            	 Log.i(TAG, "...FAILED");
		            	 try {
							throw error;
						} catch (Throwable e) {
							e.printStackTrace();
						} 	 
		             }
					 
		             @Override
		             public void onRetry() {
		                 Log.i(TAG, "...retrying ....");
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
		});
		
		return view;
	}
	
    private void setText(TextView text) {
		Typeface type = Typeface.createFromAsset(text.getContext().getAssets(), "Bloodthirsty.ttf");
		text.setTypeface(type);
		Spannable span = new SpannableString(text.getText());
		span.setSpan(new RelativeSizeSpan(3f), 0, span.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		text.setText(span);
    }
}
