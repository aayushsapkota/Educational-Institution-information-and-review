package aayush.randompatrolling;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;



public class RegisterRequest extends StringRequest {
    private static final String REGISTER_REQUEST_URL = "https://asapkota.spinetail.cdu.edu.au/stealth_patrolling/register.php";
private Map<String, String> params;

    public RegisterRequest(String name, String organization, String username, String password, String user_type, Response.Listener<String>
                           listener){
        super(Request.Method.POST, REGISTER_REQUEST_URL, listener, null);

        params = new HashMap<>();
        params.put("name", name);
        params.put("organization",organization);
        params.put("username", username);
        params.put("password", password);
        params.put("user_type", user_type);
    }

    public Map<String, String> getParams() {
        return params;
    }
}
