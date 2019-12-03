package rgu.ac.uk.recordbox.tools;

import android.content.Context;
import android.content.SharedPreferences;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RequestsManager {
    private Context context;
    public JSONObject output = null;
    private SharedPreferences editor;

    public RequestsManager(Context context) {
        this.context = context;
        this.editor = context.getSharedPreferences("SPOTIFY", 0);
    }

    public JsonObjectRequest executeSearchRequest(String query){
        String url = "https://api.spotify.com/v1/search?" + query;

        JsonObjectRequest getRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>(){

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            output = response.getJSONObject("tracks");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders(){
                Map<String, String>  params = new HashMap<>();
                params.put("Authorization", "Bearer " + editor.getString("token", "TOKEN"));

                return params;
            }
        };
        return getRequest;
    }
}