package edu.wm.werewolfclient;

import java.io.UnsupportedEncodingException;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * Activity which displays a login screen to the user, offering registration as
 * well.
 */
public class LoginActivity extends Activity {
	
	protected static final String TAG = "LoginActivity";
	/**
	 * Keep track of the login task to ensure we can cancel it if requested.
	 */
	private UserLoginTask mAuthTask = null;

	// Values for email and password at the time of the login attempt.
	private String usernameString;
	private String passwordString;

	// UI references.
	private EditText username;
	private EditText password;
	private View mLoginFormView;
	private View mLoginStatusView;
	private TextView mLoginStatusMessageView;
	private TextView mWerewolf;
	private CheckBox adminBox;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_login);
		
		

		// UI specifications
		mWerewolf = (TextView) findViewById(R.id.werewolf);
		Typeface type = Typeface.createFromAsset(getAssets(), "NekroKids.ttf");
		mWerewolf.setTypeface(type);
		
		Spannable span = new SpannableString(mWerewolf.getText());
		span.setSpan(new RelativeSizeSpan(10f), 0, span.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		mWerewolf.setText(span);
		
		// Set up the login form.
		username = (EditText) findViewById(R.id.username);

		password = (EditText) findViewById(R.id.password);
		password
				.setOnEditorActionListener(new TextView.OnEditorActionListener() {
					@Override
					public boolean onEditorAction(TextView textView, int id,
							KeyEvent keyEvent) {
						if (id == R.id.login || id == EditorInfo.IME_NULL) {
							attemptLogin();
							return true;
						}
						return false;
					}
				});

		mLoginFormView = findViewById(R.id.login_form);
		mLoginStatusView = findViewById(R.id.login_status);
		mLoginStatusMessageView = (TextView) findViewById(R.id.login_status_message);
		adminBox = (CheckBox) findViewById(R.id.admin_checkBox);

		findViewById(R.id.sign_in_button).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						if (adminBox.isChecked()) {
							attemptAdminLogin();
						} else {
							attemptLogin();
						}
					}
				});
		
		findViewById(R.id.register_button).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
		           	 	Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
		           	 	startActivity(intent);
					}
				});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	/**
	 * Attempts to sign in or register the account specified by the login form.
	 * If there are form errors (invalid email, missing fields, etc.), the
	 * errors are presented and no actual login attempt is made.
	 */
	public void attemptLogin() {
		if (mAuthTask != null) {
			return;
		}

		// Reset errors.
		username.setError(null);
		password.setError(null);

		// Store values at the time of the login attempt.
		usernameString = username.getText().toString();
		passwordString = password.getText().toString();

		boolean cancel = false;
		View focusView = null;

		// Check for a valid password.
		if (TextUtils.isEmpty(passwordString)) {
			password.setError(getString(R.string.error_field_required));
			focusView = password;
			cancel = true;
		} else if (passwordString.length() < 3) {
			password.setError(getString(R.string.error_invalid_password));
			focusView = password;
			cancel = true;
		}

		// Check for a valid username
		if (TextUtils.isEmpty(usernameString)) {
			username.setError(getString(R.string.error_field_required));
			focusView = username;
			cancel = true;
		} else if (usernameString.contains("@")) {
			username.setError(getString(R.string.error_invalid_username));
			focusView = username;
			cancel = true;
		}

		if (cancel) {
			// There was an error; don't attempt login and focus the first
			// form field with an error.
			focusView.requestFocus();
		} else {
			// Show a progress spinner, and kick off a background task to
			// perform the user login attempt.
			mLoginStatusMessageView.setText(R.string.login_progress_signing_in);
			showProgress(true);
			//mAuthTask = new UserLoginTask();
			//mAuthTask.execute((Void) null);
			
			
			// Http Request to confirm Login
			RequestParams params = new RequestParams();
			params.put("username", usernameString);
			params.put("password", passwordString);

			Log.i(TAG, "HTTP Request to Login...");
			
			WerewolfRestClient.post("/login", params, new JsonHttpResponseHandler() {
				@Override
				public void onSuccess(int statusCode,
						org.apache.http.Header[] headers, byte[] response) {
					Log.i(TAG, "...SUCCESS");

					try {
						String decoded = new String(response, "UTF-8");
						
						if (decoded.contains("success")) {
							Toast toast = Toast.makeText(getApplicationContext(), "Login successful", Toast.LENGTH_SHORT);
							toast.setGravity(Gravity.CENTER, 0, 0);
							toast.show();
							
							Log.i(TAG, "Login successful");
							
							Authenication.setUsername(usernameString);
							Authenication.setPassword(passwordString);
							
							Intent intent = new Intent(getApplicationContext(),TabsFragmentActivity.class);
							startActivity(intent);

						} else if (decoded.contains("failure")) {
							Toast toast = Toast.makeText(getApplicationContext(), "Login failed", Toast.LENGTH_SHORT);
							toast.setGravity(Gravity.CENTER, 0, 0);
							toast.show();
							
							Log.i(TAG, "Login failure");
							
							View focusView = null;
							password.setError("Wrong username or password");
							focusView = password;
							focusView.requestFocus();
						} else {
							Toast toast = Toast.makeText(getApplicationContext(), "Login error", Toast.LENGTH_SHORT);
							toast.setGravity(Gravity.CENTER, 0, 0);
							toast.show();
							
							Log.i(TAG, "Login Response contains neither success or failure.");
						}
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
				}

				@Override
				public void onFailure(int statusCode, org.apache.http.Header[] headers, 
						byte[] responseBody, Throwable error) {
					
					Log.i(TAG, "...FAILED");
					View focusView = null;
					password.setError("Wrong username or password");
					focusView = password;
					focusView.requestFocus();
					
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
					showProgress(false);
				}
			});
		}
	}
	
	public void attemptAdminLogin() {
		if (mAuthTask != null) {
			return;
		}

		// Reset errors.
		username.setError(null);
		password.setError(null);

		// Store values at the time of the login attempt.
		usernameString = username.getText().toString();
		passwordString = password.getText().toString();

		boolean cancel = false;
		View focusView = null;

		// Check for a valid password.
		if (TextUtils.isEmpty(passwordString)) {
			password.setError(getString(R.string.error_field_required));
			focusView = password;
			cancel = true;
		} else if (passwordString.length() < 3) {
			password.setError(getString(R.string.error_invalid_password));
			focusView = password;
			cancel = true;
		}

		// Check for a valid username
		if (TextUtils.isEmpty(usernameString)) {
			username.setError(getString(R.string.error_field_required));
			focusView = username;
			cancel = true;
		} else if (usernameString.contains("@")) {
			username.setError(getString(R.string.error_invalid_username));
			focusView = username;
			cancel = true;
		}

		if (cancel) {
			// There was an error; don't attempt login and focus the first
			// form field with an error.
			focusView.requestFocus();
		} else {
			// Show a progress spinner, and kick off a background task to
			// perform the user login attempt.
			mLoginStatusMessageView.setText(R.string.login_progress_signing_in);
			showProgress(true);
			//mAuthTask = new UserLoginTask();
			//mAuthTask.execute((Void) null);
			
			
			// Http Request to confirm Login
			RequestParams params = new RequestParams();
			params.put("username", usernameString);
			params.put("password", passwordString);

			Log.i(TAG, "HTTP Request to Admin Login...");
			
			WerewolfRestClient.post("/adminLogin", params, new JsonHttpResponseHandler() {
				@Override
				public void onSuccess(int statusCode,
						org.apache.http.Header[] headers, byte[] response) {
					Log.i(TAG, "...SUCCESS");
					try {
						String decoded = new String(response, "UTF-8");
						
						if (decoded.contains("success")) {
							Toast toast = Toast.makeText(getApplicationContext(), "Admin Login successful", Toast.LENGTH_SHORT);
							toast.setGravity(Gravity.CENTER, 0, 0);
							toast.show();
							
							Log.i(TAG, "Admin Login successful");
							
							Authenication.setUsername(usernameString);
							Authenication.setPassword(passwordString);
							
							Intent intent = new Intent(getApplicationContext(), AdminActivity.class);
							startActivity(intent);

						} else if (decoded.contains("failure")) {
							Toast toast = Toast.makeText(getApplicationContext(), "Admin Login failed", Toast.LENGTH_SHORT);
							toast.setGravity(Gravity.CENTER, 0, 0);
							toast.show();
							
							Log.i(TAG, "Admin Login failure");
							
							View focusView = null;
							password.setError("Wrong username or password");
							focusView = password;
							focusView.requestFocus();
						} else {
							Toast toast = Toast.makeText(getApplicationContext(), "Admin Login error", Toast.LENGTH_SHORT);
							toast.setGravity(Gravity.CENTER, 0, 0);
							toast.show();
							
							Log.i(TAG, "Admin Login Response contains neither success or failure.");
						}
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
				}

				@Override
				public void onFailure(int statusCode, org.apache.http.Header[] headers, 
						byte[] responseBody, Throwable error) {
					Log.i(TAG, "...FAILED");
					View focusView = null;
					password.setError("Wrong username or password");
					focusView = password;
					focusView.requestFocus();
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
					showProgress(false);
				}
			});
		}
	}

	/**
	 * Shows the progress UI and hides the login form.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	private void showProgress(final boolean show) {
		// On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
		// for very easy animations. If available, use these APIs to fade-in
		// the progress spinner.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			int shortAnimTime = getResources().getInteger(
					android.R.integer.config_shortAnimTime);

			mLoginStatusView.setVisibility(View.VISIBLE);
			mLoginStatusView.animate().setDuration(shortAnimTime)
					.alpha(show ? 1 : 0)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mLoginStatusView.setVisibility(show ? View.VISIBLE
									: View.GONE);
						}
					});

			mLoginFormView.setVisibility(View.VISIBLE);
			mLoginFormView.animate().setDuration(shortAnimTime)
					.alpha(show ? 0 : 1)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mLoginFormView.setVisibility(show ? View.GONE
									: View.VISIBLE);
						}
					});
		} else {
			// The ViewPropertyAnimator APIs are not available, so simply show
			// and hide the relevant UI components.
			mLoginStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
			mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
		}
	}

	/**
	 * Represents an asynchronous login task used to authenticate
	 * the user.
	 */
	public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {
		@Override
		protected Boolean doInBackground(Void... params) {
			// TODO: attempt authentication against a network service.

			try {
				// Simulate network access.
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				return false;
			}
  
			return true;
		}

		@Override
		protected void onPostExecute(final Boolean success) {
			mAuthTask = null;
			showProgress(false);

			if (success) {
				finish();
			} else {
				password
						.setError(getString(R.string.error_incorrect_password));
				password.requestFocus();
			}
		}

		@Override
		protected void onCancelled() {
			mAuthTask = null;
			showProgress(false);
		}
	}
}
