package com.creativeminds.stealthpatrolling;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

public class mapActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

//        final EditText getUsername = (EditText) findViewById(R.id.register_username);
        final EditText getName = (EditText) findViewById(R.id.map_welcome_Name);
        final EditText getOrganization = (EditText) findViewById(R.id.map_welcome_organization);
        final EditText getUserType = (EditText) findViewById(R.id.map_welcome_user_type);
        final TextView welcome_msg = (TextView) findViewById(R.id.welcome_msg);

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String organization = intent.getStringExtra("organization");
        String user_type = intent.getStringExtra("user_type");

        String message = name + "Welcome! to the user area";
        welcome_msg.setText(message);
        getName.setText(name);
        getOrganization.setText(organization);
        getUserType.setText(user_type);

    }
}
