package aayush.randompatrolling;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;

import java.util.ArrayList;

public class places extends AppCompatActivity {

    tspCalculator tsp = new tspCalculator();


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_places);


        Button addPlace = (Button) findViewById(R.id.addPlaceButton);
        Button back = (Button) findViewById(R.id.placesBack);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), MapsActivity.class);
                startActivity(i);
            }
        });

        addPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), addPlace.class);
                startActivity(i);
            }
        });


        TextView resultView = (TextView) findViewById(R.id.result);

       resultView.setText(tsp.getTspRoute());
    }
}
