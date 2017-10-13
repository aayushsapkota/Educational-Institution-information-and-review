package aayush.randompatrolling;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;


public class addAlarm extends AppCompatActivity {
    Login login = new Login();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_alarm);

        final EditText titlebox = (EditText) findViewById(R.id.titlebox);
        final EditText messagebox = (EditText) findViewById(R.id.messageBox);
        final Button addAlarm = (Button) findViewById(R.id.addAlarmButton);
        final Button back = (Button) findViewById(R.id.alarmBack);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(addAlarm.this, alarms.class);
                startActivity(intent);
            }
        });

        addAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (hasNetworkConnection()) {
                    final String title = titlebox.getText().toString();
                    final String message = messagebox.getText().toString();


                    final Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonResponse = new JSONObject(response);
                                boolean sucess = jsonResponse.getBoolean("sucess");
                                if (sucess) {
                                    Intent intent = new Intent(aayush.randompatrolling.addAlarm.this, alarms.class);
                                    aayush.randompatrolling.addAlarm.this.startActivity(intent);
                                    Toast.makeText(getApplicationContext(), "successfully Sent!", Toast.LENGTH_SHORT).show();
                                } else {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(aayush.randompatrolling.addAlarm.this);
                                    builder.setMessage("Sending Failed").setNegativeButton("Retry", null).create().show();
                                }

                            } catch (JSONException ex) {
                                ex.printStackTrace();
                            }
                        }


                    };
                    alarmRequest AlarmRequest = new alarmRequest(title, message, responseListener);
                    RequestQueue req_queue = Volley.newRequestQueue(aayush.randompatrolling.addAlarm.this);
                    req_queue.add(AlarmRequest);


                } else {
                    Toast.makeText(aayush.randompatrolling.addAlarm.this, "No internet Connection", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    public boolean hasNetworkConnection() {
        boolean connected = false;
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netActive = connectivityManager.getActiveNetworkInfo();
            connected = netActive != null && netActive.isAvailable() && netActive.isConnected();
            return connected;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connected;
    }
}
