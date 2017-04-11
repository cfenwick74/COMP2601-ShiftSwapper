package edu.carleton.COMP2601;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
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
	private String empId;
	private String pwd = "1234";
	private String ipaddr = "192.168.1.110";
	private int port = 1025;
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
		ConnectResponseHandler connectResponseHandler = new ConnectResponseHandler();
		ar.register(Fields.CONNECTED_RESPONSE, connectResponseHandler);

		spinner = (ProgressBar) findViewById(R.id.progressBar);
		spinner.setVisibility(View.GONE);
		loginBtn = (Button)findViewById(R.id.login_btn);
		userName = (EditText) findViewById(R.id.login_text);
		password = (EditText) findViewById(R.id.password_text);
		host = (EditText) findViewById(R.id.ip_text);
		port_field = (EditText) findViewById(R.id.port_text);
		password.setText(pwd);
		host.setText(ipaddr);
		port_field.setText(String.valueOf(port));
	}


	public void login(View view) {
		ipaddr = host.getText().toString();
		empId = userName.getText().toString();
		pwd = password.getText().toString();
		final boolean blankEmp = empId.equals("");
		final boolean blankPass = pwd.equals("");
		if (blankEmp || blankPass) {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					StringBuilder title = new StringBuilder("Please enter your ");

					AlertDialog dialog = new AlertDialog.Builder(LoginActivity.this).create();
					if (blankEmp && blankPass) {
						title.append("employee id and password");
					} else if (!blankPass) {
							title.append("employee id");
					} else {
						title.append("password");
					}
					dialog.setTitle(title);
					dialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog, int which) {
									dialog.dismiss();
								}
							});
					dialog.show();
				}
			});
			return;
		}


		port = Integer.parseInt(port_field.getText().toString());

		ar.init(port, ipaddr);


		HashMap m = new HashMap();
		m.put(Fields.ID, empId);
		m.put(Fields.PASSWORD, pwd);

		spinner.setIndeterminate(true);
		try {
			JSONObject jo = new JSONObject();
			jo.put(Fields.TYPE, Fields.CONNECT_REQUEST);
			jo.put(Fields.SOURCE, empId);
			jo.put(Fields.DEST, "");
			ar.start(new JSONEvent(jo, null, m));
		} catch (JSONException e1) {
			e1.printStackTrace();
		}

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


	private class ConnectResponseHandler implements EventHandler {
		@Override
		public void handleEvent(Event event) {
			System.out.println("In ConnectResponseHandler");
			Event e = event;
			boolean isAdmin = "true".equalsIgnoreCase(e.get(Fields.IS_ADMIN).toString());
			boolean status = "true".equalsIgnoreCase(e.get(Fields.STATUS).toString());
			System.out.println(isAdmin);

//			runOnUiThread(new Runnable() {
//				@Override
//				public void run() {
//					Toast.makeText(LoginActivity.this, "Connected!", Toast.LENGTH_SHORT).show();
//					spinner.setVisibility(View.GONE);
//				}
//			});

			Intent intent;

			if (status) {
				if (isAdmin) {
					intent = new Intent(LoginActivity.this, AdminActivity.class);
					intent.putExtra(Fields.SOURCE, empId);
					startActivity(intent);
				} else {
					intent = new Intent(LoginActivity.this, ShiftListActivity.class);
					startActivity(intent);
				}
			} else {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						Toast.makeText(LoginActivity.this, "Invalid employee id or password!", Toast.LENGTH_LONG).show();
						spinner.setVisibility(View.GONE);
					}
				});
			}

//			Intent intent = new Intent(LoginActivity.this, UserListActivity.class);
//			intent.putExtra("name", name);
//			startActivity(intent);
		}
	}


}
