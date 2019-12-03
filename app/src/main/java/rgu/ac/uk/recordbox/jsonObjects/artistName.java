package rgu.ac.uk.recordbox.jsonObjects;

import org.json.JSONException;
import org.json.JSONObject;

public class artistName {
    JSONObject data;
    public String name;

    public artistName(JSONObject obj){
        this.data = obj;

        try {
            this.name = obj.getString("name");
        }catch (JSONException e){
            e.toString();
        }
    }
}
