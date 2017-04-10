package edu.carleton.COMP2601;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.net.Socket;
import java.util.HashMap;

import edu.carleton.COMP2601.communication.AcceptorReactor;
import edu.carleton.COMP2601.communication.Event;
import edu.carleton.COMP2601.communication.EventHandler;
import edu.carleton.COMP2601.communication.Fields;
import edu.carleton.COMP2601.communication.JSONEvent;
import edu.carleton.COMP2601.communication.JSONEventSource;
import edu.carleton.COMP2601.communication.Reactor;
import edu.carleton.COMP2601.communication.ThreadWithReactor;

public class LoginActivity extends AppCompatActivity {

	Button loginBtn;
	EditText userName, password, host, port_field;
	private String empId, pwd, ipaddr;
	private int port;
	ProgressBar spinner;
	static LoginActivity instance;
	JSONEventSource e;
	ThreadWithReactor twr;
	Socket s;
	Reactor r;

	AcceptorReactor ar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		instance = this;
		ar = AcceptorReactor.getInstance();

		spinner = (ProgressBar) findViewById(R.id.progressBar);
		spinner.setVisibility(View.GONE);
		loginBtn = (Button)findViewById(R.id.login_btn);
		userName = (EditText) findViewById(R.id.login_text);
		password = (EditText) findViewById(R.id.password_text);
		host = (EditText) findViewById(R.id.ip_text);
		port_field = (EditText) findViewById(R.id.port_text);
	}


	public void login(View view) throws JSONException {
		ipaddr = host.getText().toString();
		empId = userName.getText().toString();
		pwd = password.getText().toString();
		port = Integer.parseInt(port_field.getText().toString());

		ar.init(port, ipaddr);
		LoginActivity.ConnectResponseHandler connectResponseHandler = new ConnectResponseHandler();
		ar.register(Fields.CONNECTED_RESPONSE, connectResponseHandler);

		HashMap m = new HashMap();
		m.put(Fields.ID, empId);
		m.put(Fields.PASSWORD, pwd);

		spinner.setIndeterminate(true);
		JSONObject jo = new JSONObject();
		jo.put(Fields.TYPE, Fields.CONNECT_REQUEST);
		jo.put(Fields.SOURCE, empId);
		jo.put(Fields.DEST, "");

		ar.start(new JSONEvent(jo, null, m));



	}


	@Override
	protected void onDestroy() {
		super.onDestroy();

		try {
			JSONObject jo = new JSONObject();
			jo.put("Type", Fields.DISCONNECT_REQUEST);
			jo.put("Source", empId);
			jo.put("Dest", "");
			jo.put("Message", "");
			sendToServer(new JSONEvent(jo, null, new HashMap<String, Serializable>()));
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * Sends an Event to the server
	 * @param e the Event to send
	 */
	void sendToServer(Event e) {
		if (twr != null) {
			try {
				((JSONEventSource) twr.getEventSource()).putEvent(e);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	/**
	 * Initialize reactor and register connection handler
	 */
	void init() {
		r = new Reactor();
		LoginActivity.ConnectResponseHandler connectResponseHandler = new ConnectResponseHandler();
		r.register(Fields.CONNECTED_RESPONSE, connectResponseHandler);
	}


	private class ConnectResponseHandler implements EventHandler {
		@Override
		public void handleEvent(Event event) {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					Toast.makeText(LoginActivity.this, "Connected!", Toast.LENGTH_SHORT).show();
					spinner.setVisibility(View.GONE);
				}
			});

			Intent intent = new Intent(LoginActivity.this, UserListActivity.class);
//			intent.putExtra("name", name);
			startActivity(intent);
		}
	}


}


//	Intent intent = new Intent(LoginActivity.this, AdminActivity.class);
//	startActivity(intent);