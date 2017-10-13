package aayush.randompatrolling;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Luffy on 13-Oct-17.
 */

public class alarmsRequest extends StringRequest{



    private static final String ALARM_REQUEST_URL = "https://asapkota.spinetail.cdu.edu.au/stealth_patrolling/alerts.php";
    private Map<String, String> params;

    public alarmsRequest( Response.Listener<String> listener){
        super(Method.GET, ALARM_REQUEST_URL, listener, null);

        params = new HashMap<>();

    }

    public Map<String, String> getParams() {
        return params;
    }







}
