package aayush.randompatrolling;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class alarms extends AppCompatActivity {

    Array title[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarms);
        final ListView display = (ListView) findViewById(R.id.listView);
        Button back = (Button) findViewById(R.id.back);
        Button addAlarm = (Button) findViewById(R.id.addAlarm);


        if (hasNetworkConnection()) {

            Response.Listener<String> responseListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        Boolean sucess = jsonResponse.getBoolean("sucess");
                        String title;
                        String message;
                        String id;
                        ArrayList<String> alarmsList = new ArrayList<>();
                        ArrayAdapter arrayAdapter = new ArrayAdapter(alarms.this,android.R.layout.simple_list_item_1,alarmsList);
                        if (sucess) {

                            for (int i = 0; i < jsonResponse.length() - 1; i++) {
                                String j = String.valueOf(i);
                                JSONObject object = (JSONObject) jsonResponse.get(j);
                                title = (String) object.get("title");
                                message = (String) object.get("message");
                                id = (String) object.get("id");
                                alarmsList.add("\n" +title +"\n\n"+message+"\n");
                            }
                            display.setAdapter(arrayAdapter);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            };

            alarmsRequest alarmsRequest = new alarmsRequest(responseListener);
            RequestQueue req_queue = Volley.newRequestQueue(alarms.this);
            req_queue.add(alarmsRequest);

        } else {
            Toast.makeText(getApplicationContext(), "No internet Connection", Toast.LENGTH_SHORT).show();
        }


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), MapsActivity.class);
                startActivity(i);
            }
        });
        addAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), aayush.randompatrolling.addAlarm.class);
                startActivity(i);
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

