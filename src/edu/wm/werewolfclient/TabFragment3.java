package edu.wm.werewolfclient;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
import android.widget.ListView;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;


public class TabFragment3 extends Fragment {

	protected static final String TAG = "TabFrag3";
	
	private View view;
	private ListView playerList;
	private CustomAdapter2 adapter;
//	private ArrayList<Player> playerArray = new ArrayList<Player>();
	private ArrayList<String> playerArray = new ArrayList<String>();
	
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }
        
        try {
        	getAllPlayers();
		} catch (JSONException e) {
			e.printStackTrace();
		}

        view = inflater.inflate(R.layout.tab_frag3_layout, container, false);

		
		
        	
//        adapter = new CustomAdapter2(getActivity(), R.layout.row_kill, playerArray);
//		playerList = (ListView) view.findViewById(R.id.playerListView);
//		playerList.setItemsCanFocus(false);
//		playerList.setAdapter(adapter);
		
        return view;
    }
    
    public void setList() {
    	adapter = new CustomAdapter2(getActivity(), R.layout.row_kill, playerArray);
		playerList = (ListView) view.findViewById(R.id.playerListView);
		playerList.setItemsCanFocus(false);
		playerList.setAdapter(adapter);
    }
    
 // Get All Players and put into playerArray List
 	public void getAllPlayers() throws JSONException {
 		Log.i(TAG, "HTTP Request to getAllPlayers...");
 		
 		// HTTP Request to get PlayersNearby
         WerewolfRestClient.get("/players/all", null, new JsonHttpResponseHandler() {   
             @Override
             public void onSuccess(int statusCode,
                     org.apache.http.Header[] headers, JSONArray players) {
            	 Log.i(TAG, "...SUCCESS");
              	for (int i=0; i< players.length(); i++){
             		//JSONObject player;
 					try {
 						//player = (JSONObject) players.get(i);
 						//String playerName = player.getString("id");
 						String playerName = players.getString(i);
 						//String userId = player.getString("userId");
 						Log.v(TAG, "playerId = " + playerName);
 						if (!playerArray.contains(playerName) 
 								&& !playerName.equals(Authenication.getUsername())) {
 							playerArray.add(playerName);
 						}
 					} catch (JSONException e) {
 						e.printStackTrace();
 					}
             	}
              	setList();
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
 	
    private void setTextTitle(TextView text) {
		Typeface type = Typeface.createFromAsset(text.getContext().getAssets(), "Bloodthirsty.ttf");
		text.setTypeface(type);
		Spannable span = new SpannableString(text.getText());
		span.setSpan(new RelativeSizeSpan(3f), 0, span.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		text.setText(span);
    }
}