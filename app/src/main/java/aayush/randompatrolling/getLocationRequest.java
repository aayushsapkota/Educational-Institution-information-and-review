package aayush.randompatrolling;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;


public class getLocationRequest extends StringRequest {


    private static final String LOCATION_REQUEST_URL = "https://asapkota.spinetail.cdu.edu.au/stealth_patrolling/getlocations.php";
    private Map<String, String> params;

    public getLocationRequest(String user_id, Response.Listener<String> listener) {
        super(Request.Method.POST, LOCATION_REQUEST_URL, listener, null);

        params = new HashMap<>();
        params.put("user_id", user_id);

    }

    public Map<String, String> getParams() {
        return params;
    }


}
