package edu.carleton.COMP2601;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import edu.carleton.COMP2601.communication.JSONEventSource;
import edu.carleton.COMP2601.communication.ThreadWithReactor;

public class LoginActivity extends AppCompatActivity {

	Button loginBtn;
	EditText userName, password, host, port_field;
	private String empId, pwd, ipaddr, port;
	static LoginActivity instance;
	JSONEventSource e;
	ThreadWithReactor twr;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		instance = this;

		loginBtn = (Button)findViewById(R.id.login_btn);
		userName = (EditText) findViewById(R.id.login_text);
		password = (EditText) findViewById(R.id.password_text);
		host = (EditText) findViewById(R.id.ip_text);
		port_field = (EditText) findViewById(R.id.port_text);
	}


	public void login(View view) {
		ipaddr = host.getText().toString();


		//TODO: message with map JSON event
		// Fields.id with login -- source
		Intent intent = new Intent(LoginActivity.this, AdminActivity.class);
		startActivity(intent);
	}
}
