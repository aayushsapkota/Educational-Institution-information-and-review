package aayush.randompatrolling;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;


public class AddPlaceRequest extends StringRequest {
    private static final String ADD_PLACE_REQUEST_URL = "https://asapkota.spinetail.cdu.edu.au/stealth_patrolling/locations.php";
    private Map<String, String> params;

    public AddPlaceRequest(String name, String latitude,
                           String longitude, String minStay, String maxStay, String priorityIndex, String checkBackTime,
                           String userID, Response.Listener<String> listener) {
        super(Method.POST, ADD_PLACE_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("name", name);
        params.put("latitude", latitude);
        params.put("longitude",longitude);
        params.put("minStay", minStay);
        params.put("maxStay", maxStay);
        params.put("priorityIndex", priorityIndex);
        params.put("checkBackTime", checkBackTime);
        params.put("userID", userID);
        Log.d("hash",params.toString());
    }

    public Map<String, String> getParams() {
        return params;
    }
}
