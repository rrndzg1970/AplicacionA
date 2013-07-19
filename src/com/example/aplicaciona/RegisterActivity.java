package com.example.aplicaciona;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterActivity extends Activity {

	// Values for email and password at the time of the login attempt.
	private String mFullName;
	private String mEmail;
	private String mPassword;

	// UI references.
	private EditText mFullNameView;
	private EditText mEmailView;
	private EditText mPasswordView;
	//private View mLoginFormView;
	//private View mLoginStatusView;
	//private TextView mLoginStatusMessageView;	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		
		mFullNameView = (EditText) findViewById(R.id.reg_fullname);
		mEmailView = (EditText) findViewById(R.id.reg_email);
		mPasswordView = (EditText) findViewById(R.id.reg_password);
		
		findViewById(R.id.btnRegister).setOnClickListener(
			new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					//setContentView(R.layout.activity_register);
					//Toast.makeText(getApplicationContext(), "antes de Registrando !!!", Toast.LENGTH_LONG).show();
					Log.d("Rafa","Antes del try .... ");
					try {
						Log.d("Rafa","Antes del register  .... ");
						attemptRegister();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						Log.d("Rafa","IOException " + e.getLocalizedMessage() + "-" + e.getMessage() );
						e.printStackTrace();
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						Log.d("Rafa","ClassNotFoundException " + e.getLocalizedMessage() + "-" + e.getMessage() );
						e.printStackTrace();
					}
				}
			});		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.register, menu);
		return true;
	}

	/**
	 * Attempts to sign in or register the account specified by the login form.
	 * If there are form errors (invalid email, missing fields, etc.), the
	 * errors are presented and no actual login attempt is made.
	 * @throws ClassNotFoundException 
	 */
	public void attemptRegister() throws IOException, ClassNotFoundException {
		// Reset errors.
		//mEmailView.setError(null);
		//mPasswordView.setError(null);

		// Reset errors.
		mFullNameView.setError(null);		
		mEmailView.setError(null);
		mPasswordView.setError(null);
		Log.d("Rafa","En el register  .... ");
		
		// Store values at the time of the login attempt.
		mFullName = mFullNameView.getText().toString();
		mEmail = mEmailView.getText().toString();
		mPassword = mPasswordView.getText().toString();

		boolean cancel = false;
		View focusView = null;

		Log.d("Rafa","En el register  ... FullName ");		
		// Check for a valid password.
		if (TextUtils.isEmpty(mFullName)) {
			mFullNameView.setError(getString(R.string.error_field_required));
			focusView = mFullNameView;
			cancel = true;
		} else if (mFullName.length() < 4) {
			mFullNameView.setError(getString(R.string.error_invalid_password));
			focusView = mFullNameView;
			cancel = true;
		}

		Log.d("Rafa","En el register  ... Email ");		
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
		
		Log.d("Rafa","En el register  ... Password ");
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

		
		Log.d("Rafa","pasa x aquí ... " + mFullName + "-" + mEmail + "-" + mPassword);
		
		Class.forName("org.postgresql.Driver");
		String url;
		url = "jdbc:postgresql://192.168.4.20:5432/" +
				"curso" +
				"?sslfactory=org.postgresql.ssl.NonValidatingFactory"; // +
				//"&ssl=true";
		
		Connection conn;
		try {
			conn = DriverManager.getConnection(url,"postgres","");
			PreparedStatement is = conn.prepareStatement(
					"INSERT INTO registro (nombre_completo, ecorreo, password) VALUES (?, ?, ?);");
			
			is.setString(1,mFullName);
			is.setString(2,mEmail);
			is.setString(3,mPassword);
			
			int rows = is.executeUpdate();
			is.close();
			
			Log.d("Rafa","rows <" + rows + ">");			
		} catch (SQLException e) {
			Log.d("Rafa","SQLException " + e.getErrorCode() + "-" + e.getCause() + "-" + e.getLocalizedMessage() + "-" + e.getMessage() );	
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		
		if (cancel) {
			// There was an error; don't attempt login and focus the first
			// form field with an error.
			focusView.requestFocus();
		} else {
			// Show a progress spinner, and kick off a background task to
			// perform the user login attempt.
			//mLoginStatusMessageView.setText(R.string.login_progress_signing_in);
			Toast.makeText(getApplicationContext(), "Registrando:" + mFullName + "-" + mEmail + "-" + mPassword , Toast.LENGTH_LONG).show();			
		}
	}
	
	
}
