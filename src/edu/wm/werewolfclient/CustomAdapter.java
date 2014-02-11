package edu.wm.werewolfclient;

import java.util.ArrayList;

import org.json.JSONArray;

import com.loopj.android.http.JsonHttpResponseHandler;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class CustomAdapter extends ArrayAdapter<Player>{
	
	private Activity context;
	private int layoutResourceId;
	private ArrayList<Player> data = new ArrayList<Player> ();

	public CustomAdapter(Activity context, int layoutResourceId, ArrayList<Player> data) {
		super(context, layoutResourceId, data);
		this.context = context;
		this.layoutResourceId = layoutResourceId;
		this.data = data;
	}
	
	static class PlayerHolder {
		TextView text;
		Button Votebtn;
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
			view.setTag(playerHolder);
		}
		
		else {
			((PlayerHolder) view.getTag()).Votebtn.setTag(data.get(position));
		}
		
		final PlayerHolder holder = (PlayerHolder) view.getTag();
		Player player = data.get(position);
		holder.text.setText(player.getName());
		holder.Votebtn.setOnClickListener(new OnClickListener(){
		
			// Vote or Kill player
			@Override
			public void onClick (View v) {
				// Logs which button pressed
				Log.i("CustomAdapter", "Vote button pressed");

				// Make HTTP Request to Vote player 
				WerewolfRestClient.post("/players/vote/{id}", null, new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(JSONArray players) {
						Log.i("CustomAdapter", "You voted for "+ holder.text.getText());
						Toast.makeText(context, "You voted for " + holder.text.getText(),
								Toast.LENGTH_LONG).show();
					}
				});	
			}
		});
		return view;
	}
	

}
