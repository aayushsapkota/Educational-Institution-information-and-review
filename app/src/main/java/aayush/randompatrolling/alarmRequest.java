package aayush.randompatrolling;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;


public class alarmRequest extends StringRequest{


    private static final String REGISTER_REQUEST_URL = "https://asapkota.spinetail.cdu.edu.au/stealth_patrolling/alarm.php";
    private Map<String, String> params;

    public alarmRequest(String title, String message,  Response.Listener<String>
            listener){
        super(Method.POST, REGISTER_REQUEST_URL, listener, null);

        params = new HashMap<>();
        params.put("title", title);
        params.put("message",message);

    }

    public Map<String, String> getParams() {
        return params;
    }
}


