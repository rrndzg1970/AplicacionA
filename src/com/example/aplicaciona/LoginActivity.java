package com.example.aplicaciona;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Activity which displays a login screen to the user, offering registration as
 * well.
 */
@SuppressLint("NewApi")
public class LoginActivity extends Activity {
	/**
	 * A dummy authentication store containing known user names and passwords.
	 * TODO: remove after connecting to a real authentication system.
	 */
	private static final String[] DUMMY_CREDENTIALS = new String[] {
			"foo@example.com:hello", "bar@example.com:world", "preventa" };

	/**
	 * The default email to populate the email field with.
	 */
	public static final String EXTRA_EMAIL = "com.example.android.authenticatordemo.extra.EMAIL";

	/**
	 * Keep track of the login task to ensure we can cancel it if requested.
	 */
	private UserLoginTask mAuthTask = null;

	// Values for email and password at the time of the login attempt.
	private String mEmail;
	private String mPassword;

	// UI references.
	private EditText mEmailView;
	private EditText mPasswordView;
	private View mLoginFormView;
	private View mLoginStatusView;
	private TextView mLoginStatusMessageView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);

		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);		
		
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_login);

		// Set up the login form.
		mEmail = getIntent().getStringExtra(EXTRA_EMAIL);
		mEmailView = (EditText) findViewById(R.id.email);
		mEmailView.setText(mEmail);

		mPasswordView = (EditText) findViewById(R.id.password);
		mPasswordView
				.setOnEditorActionListener(new TextView.OnEditorActionListener() {
					@Override
					public boolean onEditorAction(TextView textView, int id,
							KeyEvent keyEvent) {
						if (id == R.id.login_button || id == EditorInfo.IME_NULL) {
							try {
								attemptLogin();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							return true;
						}
						return false;
					}
				});

		mLoginFormView = findViewById(R.id.login_form);
		mLoginStatusView = findViewById(R.id.login_status);
		mLoginStatusMessageView = (TextView) findViewById(R.id.login_status_message);

		findViewById(R.id.sign_in_button).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						//setContentView(R.layout.activity_register);
						Intent i = new Intent(getApplicationContext(), RegisterActivity.class);
			            startActivity(i);						
					}
				});

		findViewById(R.id.login_button).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						try {
							attemptLogin();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
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
	public void attemptLogin() throws IOException {
		if (mAuthTask != null) {
			return;
		}

		// Reset errors.
		mEmailView.setError(null);
		mPasswordView.setError(null);

		// Store values at the time of the login attempt.
		mEmail = mEmailView.getText().toString();
		mPassword = mPasswordView.getText().toString();

		boolean cancel = false;
		View focusView = null;

		// Check for a valid password.
		if (TextUtils.isEmpty(mPassword)) {
			mPasswordView.setError(getString(R.string.error_field_required));
			focusView = mPasswordView;
			cancel = true;
		} else if (mPassword.length() < 4) {
			mPasswordView.setError(getString(R.string.error_invalid_password));
			focusView = mPasswordView;
			cancel = true;
		}

		// Check for a valid email address.
		if (TextUtils.isEmpty(mEmail)) {
			mEmailView.setError(getString(R.string.error_field_required));
			focusView = mEmailView;
			cancel = true;
/*		} else if (!mEmail.contains("preventa")) {
			mEmailView.setError(getString(R.string.error_invalid_email));
			focusView = mEmailView;
			cancel = true;
*/			
		}
		
		Log.d("Rafa","pasa x aquí ... " + mEmail + "-" + mPassword);

		if (cancel) {
			// There was an error; don't attempt login and focus the first
			// form field with an error.
			focusView.requestFocus();
		} else {
			// Show a progress spinner, and kick off a background task to
			// perform the user login attempt.
			mLoginStatusMessageView.setText(R.string.login_progress_signing_in);
			showProgress(true);
			postLoginData();
			
		}
	}

	private void postLoginData() {
        // Create a new HttpClient and Post Header
        HttpClient httpclient = new DefaultHttpClient();
         
        /* login.php returns true if username and password is equal to saranga */
        //HttpPost httppost = new HttpPost("http://www.sencide.com/blog/login.php");
        HttpPost httppost = new HttpPost("http://192.168.0.3:606");
        
        try {
            // Add user name and password
         EditText uname = (EditText)findViewById(R.id.email);
         String username = uname.getText().toString();
 
         EditText pword = (EditText)findViewById(R.id.password);
         String password = pword.getText().toString();
          
            //List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            //nameValuePairs.add(new BasicNameValuePair("usuario", username));
            //nameValuePairs.add(new BasicNameValuePair("password", password));
            //httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
 
            // Execute HTTP Post Request
/**            Log.d("SENCIDE", "Execute HTTP Post Request");
            HttpResponse response = httpclient.execute(httppost);
             
            String str = inputStreamToString(response.getEntity().getContent()).toString();
            Log.d("SENCIDE", str);
*/            
            
            
            //XML example to send via Web Service.
            StringBuilder sb = new StringBuilder();
            sb.append("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n<w_Login auth=\"" + username + ":" + password  + ":ciospala:000:00:\"></w_Login>"); //XML as a string

            //httppost.addHeader("Accept", "text/xml");


            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1); 
            nameValuePairs.add(new BasicNameValuePair("body", sb.toString()) );//WS Parameter and    Value           
            //httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));          

            httppost.setEntity(new StringEntity("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n<w_Login auth=\"" + username + ":" + password  + ":ciospala:000:00:\"></w_Login>", "ISO-8859-1"));            
            //httppost.setHeader("Content-Type","application/soap+xml;charset=UTF-8");
            httppost.addHeader("Accept", "*/*");
            httppost.addHeader("Content-Type", "text/xml");
            //httppost.addHeader("Content-Type", "application/x-www-form-urlencoded");
            httppost.addHeader("Authorization", "Basic " + Base64.encodeToString((username + ":" + password).getBytes(), Base64.NO_WRAP));
            //httppost.setHeader("Authorization", "Basic " + username + ":" + password);
            Log.d("Rafa", "Veamos que hace este hhtp ANTES se --> <" + sb.toString() + ">");


//            BasicHttpResponse httpResponse = (BasicHttpResponse) httpclient.execute(httppost);

            HttpResponse response = httpclient.execute(httppost);

            Log.d("Rafa", "Veamos que hace este http ANTES del status code");            
            int responseCode = response.getStatusLine().getStatusCode();            
            Log.d("SENCIDE", "2 --> <" + responseCode + ">" );
            String str = inputStreamToString(response.getEntity().getContent()).toString();
            Log.d("Rafa", "Respuesta [" +  str + "]");
            
			//tvData.setText(httpResponse.getStatusLine().toString()); //text view is expected to print the response            
            Log.d("Rafa", "Veamos que hace este http after");
           
            //InputStream in = response.getEntity().getContent();
            
            Log.d("Rafa", "in ?" );
            
            DocumentBuilder builder = DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder();
            Log.d("Rafa", "builder ?" );
            //Document doc = builder.parse( in );
            ByteArrayInputStream is = new ByteArrayInputStream( str.getBytes("ISO-8859-1"));
            Log.d("Rafa", "is ?" );
            Document doc = builder.parse( is );            
            Log.d("Rafa", "doc ?");
            String respuesta = "";
            if (doc != null) {
            	Log.d("Rafa", "doc no es null");
                NodeList nl = doc.getElementsByTagName("resultado");
                Log.d("Rafa", "doc no es null" + nl );
                if (nl.getLength() > 0) {
                    Node node = nl.item(0);
                    respuesta = node.getTextContent();
                }
            }            
            
            //Log.d("Rafa", "---> " + httpResponse.getStatusLine().toString() + "Veamos que hace este hhtp .... ");
            Log.d("Rafa", "Veamos que hace este http DESPUES respuesta <" + respuesta + ">" );
            
			if(respuesta.equalsIgnoreCase("ok"))
            {
             Log.w("SENCIDE", "TRUE");
             Log.d("Rafa", "Veamos que hace este hhtp ....TRUE");
             //mLoginStatusMessageView.setText("Login successful");   
             //Toast.makeText(getApplicationContext(), "Login Successful !!!", Toast.LENGTH_LONG).show();
             setContentView(R.layout.activity_menu);
            } else {
             Log.w("SENCIDE", "FALSE");
             Log.d("Rafa", "Veamos que hace este hhtp ....FALSE");
             //mLoginStatusMessageView.setText(str);
             setContentView(R.layout.activity_login);
             	Toast.makeText(getApplicationContext(), "Login Not Successful !!!" + str, Toast.LENGTH_LONG).show();   
            }
 
        } catch (ClientProtocolException e) {
        	Log.d("Rafa","ClientProtocolException:" );
        	Log.d("Rafa","ClientProtocolException:" + e.getMessage() );
         e.printStackTrace();
        } catch (IOException e) {
        	Log.d("Rafa","IOException:" );
        	Log.d("Rafa","IOException:" + e.getMessage() );
         e.printStackTrace();
	    } catch (NullPointerException e) {
	    	Log.d("Rafa","NullPointerException:");
	    	Log.d("Rafa","NullPointerException:" + e.getMessage() );
	     e.printStackTrace();
		} catch (ParserConfigurationException e) {
			Log.d("Rafa","ParserConfigurationException:" + e.getMessage());
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			Log.d("Rafa","SAXException:" + e.getMessage());
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    } 


    private StringBuilder inputStreamToString(InputStream is) {
	     String line = "";
	     StringBuilder total = new StringBuilder();
	     // Wrap a BufferedReader around the InputStream
	     BufferedReader rd = new BufferedReader(new InputStreamReader(is));
	     // Read response until the end
	     try {
	      while ((line = rd.readLine()) != null) { 
	        total.append(line); 
	      }
	     } catch (IOException e) {
	      e.printStackTrace();
	     }
	     // Return full string
	     return total;
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
	 * Represents an asynchronous login/registration task used to authenticate
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

			for (String credential : DUMMY_CREDENTIALS) {
				String[] pieces = credential.split(":");
				if (pieces[0].equals(mEmail)) {
					// Account exists, return true if the password matches.
					return pieces[1].equals(mPassword);
				}
			}

			// TODO: register the new account here.
			return true;
		}

		@Override
		protected void onPostExecute(final Boolean success) {
			mAuthTask = null;
			showProgress(false);

			if (success) {
				finish();
			} else {
				mPasswordView
						.setError(getString(R.string.error_incorrect_password));
				mPasswordView.requestFocus();
			}
		}

		@Override
		protected void onCancelled() {
			mAuthTask = null;
			showProgress(false);
		}
		
				
	}
}
