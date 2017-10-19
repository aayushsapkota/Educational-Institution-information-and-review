package aayush.randompatrolling;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class Login extends AppCompatActivity {
    private static String user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        final EditText getUsername = (EditText) findViewById(R.id.user_username);
        final EditText getPassword = (EditText) findViewById(R.id.user_password);
        final Button sign_in = (Button) findViewById(R.id.sign_in_button);
        final Button login_Register = (Button) findViewById(R.id.register_button);

        login_Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent loginIntent = new Intent(Login.this, register.class);
                Login.this.startActivity(loginIntent);
            }
        });


        sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (hasNetworkConnection()) {
                    final String username = getUsername.getText().toString();
                    final String password = getPassword.getText().toString();

                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonResponse = new JSONObject(response);
                                Log.d("rsp",response);
                                boolean sucess = jsonResponse.getBoolean("sucess");
                                if (sucess) {
                                    String name = jsonResponse.getString("name");
                                    user_id = jsonResponse.getString("user_id");
//                                    Log.d("user ID", userID);
//                                    String organization = jsonResponse.getString("organization");
//                                    String user_type = jsonResponse.getString("user_type");
                                    Intent intent = new Intent(Login.this, MapsActivity.class);

                                    Login.this.startActivity(intent);


                                    Toast.makeText(getApplicationContext(), "Login Sucessful", Toast.LENGTH_SHORT).show();
                                } else {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
                                    builder.setMessage("Login Failed").setNegativeButton("Retry", null).create().show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    };

                    LoginRequest loginRequest = new LoginRequest(username, password, responseListener);
                    RequestQueue req_queue = Volley.newRequestQueue(Login.this);
                    req_queue.add(loginRequest);

                } else {
                    Toast.makeText(getApplicationContext(), "No internet Connection", Toast.LENGTH_SHORT).show();
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



    public String getUserID(){
        return user_id;
    }

}
