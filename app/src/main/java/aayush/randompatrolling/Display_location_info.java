package aayush.randompatrolling;

import android.content.Intent;
import android.support.v4.view.ScrollingView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ScrollView;
import android.widget.TextView;

public class Display_location_info extends AppCompatActivity {
    private String address;
    private String minStay;
    private String maxStay;
    private String priority;
    private String checkBackOn;
    private String result;
    private String latitude;
    private String longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_location_info);

        Intent i = getIntent();
        address = i.getStringExtra("address");
        latitude = i.getStringExtra("Latitude");
        longitude = i.getStringExtra("Longitude");
        minStay = i.getStringExtra("minTime");
        maxStay = i.getStringExtra("MaxTime");
        priority = i.getStringExtra("priority");
        checkBackOn = i.getStringExtra("checkBackOn");

        result = "Place Info\n\n\n  Address:"+address+
        "\n\n Latitude: " + latitude+ "\n\n Longitude: " + longitude+ "\n\n Min Time to Stay: " + minStay+
                " mins \n\n Max time to stay: " + maxStay+" mins\n\n Priority: " +priority+ "\n\n Visit Again in: " + checkBackOn;

        TextView view = (TextView) findViewById(R.id.viewData);
        view.setText(result);
    }
}
