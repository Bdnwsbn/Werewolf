package edu.wm.werewolfclient;

import java.io.UnsupportedEncodingException;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;

public class AdminActivity extends Activity {
	
	protected static final String TAG = "AdminActivity";
	private Button createGameBtn;
	private Button restartGameBtn;
	private Button viewStatsBtn;
	private TextView mWerewolf;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_admin);
		
		createGameBtn = (Button) findViewById(R.id.create_game_btn);
		restartGameBtn = (Button) findViewById(R.id.restart_game_btn);
		viewStatsBtn = (Button) findViewById(R.id.view_game_stats_btn);
		
		mWerewolf = (TextView) findViewById(R.id.werewolf2);
		Typeface type = Typeface.createFromAsset(getAssets(), "NekroKids.ttf");
		mWerewolf.setTypeface(type);
		
		Spannable span = new SpannableString(mWerewolf.getText());
		span.setSpan(new RelativeSizeSpan(10f), 0, span.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		mWerewolf.setText(span);
		
		createGameBtn.setOnClickListener(mCreateGame);
		restartGameBtn.setOnClickListener(mRestartGame);
		viewStatsBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), ViewGameStatsActivity.class);
				startActivity(intent);
			}
		});
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.admin, menu);
		return true;
	}
	
	protected OnClickListener mCreateGame = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Log.i(TAG, "HTTP Request to Create Game...");
			
			WerewolfRestClient.post("/admin/createGame", null, new JsonHttpResponseHandler() {
				@Override
				public void onSuccess(int statusCode,
						org.apache.http.Header[] headers, byte[] response) {
					Log.i(TAG, "...SUCCESS");

					try {
						String decoded = new String(response, "UTF-8");
						
						if (decoded.contains("success")) {
							Toast toast = Toast.makeText(getApplicationContext(), "Game created", Toast.LENGTH_SHORT);
							toast.setGravity(Gravity.CENTER, 0, 0);
							toast.show();
							
							Log.i(TAG, "Game create success");
						} else if (decoded.contains("failure")) {
							Toast toast = Toast.makeText(getApplicationContext(), "Failed to create Game", Toast.LENGTH_SHORT);
							toast.setGravity(Gravity.CENTER, 0, 0);
							toast.show();
							
							Log.i(TAG, "Game create fail");
						} else {
							Toast toast = Toast.makeText(getApplicationContext(), "Game create error", Toast.LENGTH_SHORT);
							toast.setGravity(Gravity.CENTER, 0, 0);
							toast.show();
							
							Log.i(TAG, "Game creation status unknown.");
						}
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
				}

				@Override
				public void onFailure(int statusCode, org.apache.http.Header[] headers, 
						byte[] responseBody, Throwable error) {
					
					Log.i(TAG, "...FAILED");
					try {
						throw error;
					} catch (Throwable e) {
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
	};
	
	protected OnClickListener mRestartGame = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Log.i(TAG, "HTTP Request to Restart Game...");

			WerewolfRestClient.post("/admin/restartGame", null, new JsonHttpResponseHandler() {
				@Override
				public void onSuccess(int statusCode,
						org.apache.http.Header[] headers, byte[] response) {
					Log.i(TAG, "...SUCCESS");

					try {
						String decoded = new String(response, "UTF-8");
						
						if (decoded.contains("success")) {
							Toast toast = Toast.makeText(getApplicationContext(), "Game restarted", Toast.LENGTH_SHORT);
							toast.setGravity(Gravity.CENTER, 0, 0);
							toast.show();
							
							Log.i(TAG, "Game restart success");
						} else if (decoded.contains("failure")) {
							Toast toast = Toast.makeText(getApplicationContext(), "Failed to restart Game", Toast.LENGTH_SHORT);
							toast.setGravity(Gravity.CENTER, 0, 0);
							toast.show();
							
							Log.i(TAG, "Game restart fail");
						} else {
							Toast toast = Toast.makeText(getApplicationContext(), "Game restart error", Toast.LENGTH_SHORT);
							toast.setGravity(Gravity.CENTER, 0, 0);
							toast.show();
							
							Log.i(TAG, "Game restart status unknown.");
						}
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
				}

				@Override
				public void onFailure(int statusCode, org.apache.http.Header[] headers, 
						byte[] responseBody, Throwable error) {
					
					Log.i(TAG, "...FAILED");
					try {
						throw error;
					} catch (Throwable e) {
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
	};

}
