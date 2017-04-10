package edu.carleton.COMP2601;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {

	Button loginBtn;
	EditText userName, password, host, port;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		loginBtn = (Button)findViewById(R.id.login_btn);
		userName = (EditText) findViewById(R.id.login_text);
		password = (EditText) findViewById(R.id.password_text);
		host = (EditText) findViewById(R.id.ip_text);
		port = (EditText) findViewById(R.id.port_text);
	}


	public void login(View view) {

		//TODO: message with map JSON event
		// Fields.id with login -- source
		Intent intent = new Intent(LoginActivity.this, AdminActivity.class);
		startActivity(intent);
	}
}
