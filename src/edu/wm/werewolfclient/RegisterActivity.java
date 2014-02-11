package edu.wm.werewolfclient;

import java.io.UnsupportedEncodingException;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class RegisterActivity extends Activity {

	protected static final String TAG = "RegisterActivity (Main)";
	public static final String PREFERENCES_NAME = "WWClient";
	public static final String USERNAME_KEY = "Username";
	public static final String PASSWORD_KEY = "Password";
	
	private TextView title;
	private TextView username;
	private TextView password;
	private TextView verifyPw;
	private TextView firstName;
	private TextView lastName;
	
	private EditText usernameText;
	private EditText passwordText;
	private EditText verifyPasswordText;
	private EditText firstNameText;
	private EditText lastNameText;
	private Button registerButton;
	private Button loginButton;
	
	private String usernameStr;
	private String passwordStr;
	private String verifyPasswordStr;
	private String firstNameStr;
	private String lastNameStr;
	
	//private SharedPreferences mSharedPreferences;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_register);
    
        title = (TextView) findViewById(R.id.textViewRegister);
        username = (TextView) findViewById(R.id.usernameTextView);
        password = (TextView) findViewById(R.id.passwordTV);
        verifyPw = (TextView) findViewById(R.id.verifyPasswordTV);
        firstName = (TextView) findViewById(R.id.firstName);
        lastName = (TextView) findViewById(R.id.lastName);
         
        
        usernameText = (EditText) findViewById(R.id.username);
        passwordText = (EditText) findViewById(R.id.password);
        verifyPasswordText = (EditText) findViewById(R.id.verify_password);
        firstNameText = (EditText) findViewById(R.id.firstname);
        lastNameText = (EditText) findViewById(R.id.lastname);
        
        registerButton = (Button) findViewById(R.id.registerButton);
        loginButton = (Button) findViewById(R.id.loginButton);
        
        
		setText(title);
		setText(username);
		setText(password);
		setText(verifyPw);
		setText(firstName);
		setText(lastName);
		setText(registerButton);
		setText(loginButton);

        
        // If Bundle savedState empty (not saved) - empty strings
        if (savedInstanceState == null) {
        	usernameStr="";
        	firstNameStr="";
        	lastNameStr="";
        }
        else {
        // If savedState, save input strings
	        usernameStr = savedInstanceState.getString("username");
	        passwordStr = savedInstanceState.getString("password");
	        firstNameStr = savedInstanceState.getString("firstName");
	        lastNameStr = savedInstanceState.getString("lastName");
        }
        
//        mSharedPreferences = getSharedPreferences(PREFERENCES_NAME, MODE_PRIVATE);
//        setFieldsFromSavedData();
        
        // Send request to WebApp to create User
        registerButton.setOnClickListener(mRegisterOnClick);
        loginButton.setOnClickListener(new OnClickListener() {
        	@Override
        	public void onClick(View view) {
           	 	Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
           	 	startActivity(intent);
        	}
        });
    }
    
//    protected void setFieldsFromSavedData() {
//    	String username
//    }
    
    // Saves data
    @Override
    protected void onSaveInstanceState(Bundle outState){
    	super.onSaveInstanceState(outState);
    	
    	outState.putString("username", usernameStr);
    	outState.putString("firstName", passwordStr);
    	outState.putString("lastName", lastNameStr);
    }
    
    protected OnClickListener mRegisterOnClick = new OnClickListener() {
    	@Override
    	public void onClick(View view) {
    		
            usernameStr = usernameText.getText().toString();
    	    passwordStr = passwordText.getText().toString();
    	    verifyPasswordStr = verifyPasswordText.getText().toString();
    	    firstNameStr = firstNameText.getText().toString();
    	    lastNameStr = lastNameText.getText().toString();
    		
    		if (checkFields()){
    			Log.i(TAG, "HTTP Request to register...");
    			
    			RequestParams params = new RequestParams();
    			params.put("id", usernameText.getText().toString());
    			params.put("username", usernameText.getText().toString());
    			params.put("password", passwordText.getText().toString());
    			params.put("firstName", firstNameText.getText().toString());
    			params.put("lastName", lastNameText.getText().toString());

    			WerewolfRestClient.post("/createUser", params, new JsonHttpResponseHandler() {
    				@Override
    				public void onSuccess(int statusCode,
    						org.apache.http.Header[] headers, byte[] response) {
    					Log.i(TAG, "...SUCCESS");
    					//               	 	SharedPreferences.Editor editor = mSharedPreferences.edit();
    					//               	 	editor.putString(USERNAME_KEY, usernameStr);
    					//               	 	editor.putString(PASSWORD_KEY, passwordStr);
    					//               	 	
    					//               	 	if(editor.commit()) {
    					//               	 		Log.i(TAG, "Saved username & password");
    					//               	 	}

    					try {
    						
							String decoded = new String(response, "UTF-8");
							Log.v(TAG, "response = " + decoded);
							
							if (decoded.contains("success")) {
								Log.i(TAG, "Registration successful");
								Toast toast = Toast.makeText(getApplicationContext(), "Registration successful", Toast.LENGTH_SHORT);
								toast.setGravity(Gravity.CENTER, 0, 0);
								toast.show();
								
								Authenication.setUsername(usernameStr);
								Authenication.setPassword(passwordStr);
								
		    					Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
		    					startActivity(intent);

							} else if (decoded.contains("failure")) {
								
								if (decoded.contains("UserAlreadyExistsException")) {
									Log.i(TAG, "Registration fail: User Already Exists");
									Toast toast1 = Toast.makeText(getApplicationContext(), "Username already exists", Toast.LENGTH_SHORT);
									toast1.setGravity(Gravity.CENTER, 0, 0);
									toast1.show();
									
									View focusView = null;
									usernameText.setError("Please choose another Username");
									focusView = usernameText;
									focusView.requestFocus();
									
								} else {
									Log.i(TAG, "Registration fail: Unknown Error");
									Toast toast = Toast.makeText(getApplicationContext(), "Registration failed", Toast.LENGTH_SHORT);
									toast.setGravity(Gravity.CENTER, 0, 0);
									toast.show();
								}
								
							} else {
								Log.i(TAG, "Registration Response contains neither success or failure.");
								Toast toast = Toast.makeText(getApplicationContext(), "Registration error", Toast.LENGTH_SHORT);
								toast.setGravity(Gravity.CENTER, 0, 0);
								toast.show();
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
    	}
    };
    
    private boolean checkFields() {
    	
		// Reset errors.
		usernameText.setError(null);
		passwordText.setError(null);
		verifyPasswordText.setError(null);
		firstNameText.setError(null);
		lastNameText.setError(null);

		boolean cancel = false;
		View focusView = null;

		// Check for a valid password.
		if (TextUtils.isEmpty(passwordStr)) {
			passwordText.setError(getString(R.string.error_field_required));
			focusView = passwordText;
			cancel = true;
		} else if (passwordStr.length() < 3) {
			passwordText.setError(getString(R.string.error_invalid_password));
			focusView = passwordText;
			cancel = true;
		}
		
		// Check passwords match
		if (!passwordStr.equals(verifyPasswordStr)) {
			//Toast.makeText(getApplicationContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
			verifyPasswordText.setError(getString(R.string.error_match));
			focusView = verifyPasswordText;
			cancel = true;
		}

		// Check for a valid username
		if (TextUtils.isEmpty(usernameStr)) {
			usernameText.setError(getString(R.string.error_field_required));
			focusView = usernameText;
			cancel = true;
		} else if (usernameStr.contains("@")) {
			usernameText.setError(getString(R.string.error_invalid_username));
			focusView = usernameText;
			cancel = true;
		}
		
		// Check for valid first name
		if (TextUtils.isEmpty(firstNameStr)) {
			firstNameText.setError(getString(R.string.error_field_required));
			focusView = firstNameText;
			cancel = true;
		}
		
		// Check for valid last name
		if (TextUtils.isEmpty(lastNameStr)) {
			lastNameText.setError(getString(R.string.error_field_required));
			focusView = lastNameText;
			cancel = true;
		}
		
		if (cancel) {
			// There was an error; don't attempt login and focus the first
			// form field with an error.
			focusView.requestFocus();
		} else {
			return true;
		}
		
		return false;
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    private void setText(TextView text) {
		Typeface type = Typeface.createFromAsset(text.getContext().getAssets(), "BillionStars.ttf");
		text.setTypeface(type);
		Spannable span = new SpannableString(text.getText());
		span.setSpan(new RelativeSizeSpan(3f), 0, span.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		text.setText(span);
    }
}
