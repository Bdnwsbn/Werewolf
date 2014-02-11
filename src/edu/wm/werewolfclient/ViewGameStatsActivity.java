package edu.wm.werewolfclient;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class ViewGameStatsActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_game_stats);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.view_game_stats, menu);
		return true;
	}

}
