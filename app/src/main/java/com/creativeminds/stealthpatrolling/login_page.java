package com.creativeminds.stealthpatrolling;

import android.content.Intent;
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

public class login_page extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        final EditText getUsername = (EditText) findViewById(R.id.user_username);
        final EditText getPassword = (EditText) findViewById(R.id.user_password);
        final Button sign_in = (Button) findViewById(R.id.sign_in_button);
        final Button login_Register = (Button) findViewById(R.id.register_button);

        login_Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent loginIntent = new Intent(login_page.this, registerActivity.class);
                login_page.this.startActivity(loginIntent);
            }
        });


        sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String username = getUsername.getText().toString();
                final String password = getPassword.getText().toString();

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean sucess = jsonResponse.getBoolean("sucess");
                            if (sucess) {
                                String name = jsonResponse.getString("name");
                                String organization = jsonResponse.getString("organization");
                                String user_type = jsonResponse.getString("user_type");
                                Intent intent = new Intent(login_page.this, LocationList.class);
//                                intent.putExtra("name", name);
//                                intent.putExtra("organization", organization);
//                                intent.putExtra("user_type", user_type);
                                login_page.this.startActivity(intent);


                                login_page.this.startActivity(intent);
                                Toast.makeText(getApplicationContext(), "Login Sucessful", Toast.LENGTH_LONG).show();
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(login_page.this);
                                builder.setMessage("Login Failed").setNegativeButton("Retry", null).create().show();
                            }
                        }
                        catch (JSONException e){
                            e.printStackTrace();
                        }



                    }

                };

                LoginRequest loginRequest = new LoginRequest(username, password, responseListener);
                RequestQueue req_queue = Volley.newRequestQueue(login_page.this);
                req_queue.add(loginRequest);

            }
        });
    }
}
