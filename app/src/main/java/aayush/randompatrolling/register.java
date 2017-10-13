package aayush.randompatrolling;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class register extends AppCompatActivity {
    Login login = new Login();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        final EditText getUsername = (EditText) findViewById(R.id.register_username);
        final EditText getPassword = (EditText) findViewById(R.id.register_password);
        final EditText getName = (EditText) findViewById(R.id.register_Name);
        final EditText getOrganization = (EditText) findViewById(R.id.register_organization);
        final EditText getUserType = (EditText) findViewById(R.id.register_user_type);
        final Button Register = (Button) findViewById(R.id.register_button);

        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(hasNetworkConnection()){
                    final String name = getName.getText().toString();
                    final String organization = getOrganization.getText().toString();
                    final String username = getUsername.getText().toString();
                    final String password = getPassword.getText().toString();
                    final String user_type = getUserType.getText().toString();

                    final Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonResponse = new JSONObject(response);
                                boolean sucess = jsonResponse.getBoolean("sucess");
                                if (sucess) {
                                    Intent intent = new Intent(register.this, Login.class);
                                    register.this.startActivity(intent);
                                    Toast.makeText(getApplicationContext(), "successfully registered", Toast.LENGTH_SHORT).show();
                                } else {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(register.this);
                                    builder.setMessage("Registration Failed").setNegativeButton("Retry", null).create().show();
                                }

                            } catch (JSONException ex) {
                                ex.printStackTrace();
                            }
                        }


                    };
                    RegisterRequest registerRequest = new RegisterRequest(name, organization, username, password, user_type, responseListener);
                    RequestQueue req_queue = Volley.newRequestQueue(register.this);
                    req_queue.add(registerRequest);



                }
             else{
                Toast.makeText(register.this, "No internet Connection", Toast.LENGTH_SHORT).show();
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