package rgu.ac.uk.recordbox.jsonObjects;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

import java.util.ArrayList;

public class Items {

    private JSONObject data;
    public String name;
    public JSONArray allArtistsData;
    public ArrayList<artistName> allArtistName = new ArrayList<>();

    public Items(JSONObject obj){
        //
        this.data = obj;

        try {
            this.name = obj.getString("name");
        }catch (JSONException e){
            e.toString();
        }

        try {
            allArtistsData = obj.getJSONArray("artists");

            //JSONArray outputarray = requestResponse.getJSONObject("tracks").getJSONArray("items");
            allArtistName.clear();

            //adding artistNames to arraylist
            for(int i=0 ; i <allArtistsData.length() ; i++){
                JSONObject data123 = allArtistsData.getJSONObject(i);
                artistName artistN = new artistName(data123);
                //ArrayList of artistName objects
                allArtistName.add(artistN);
            }

        }catch (JSONException e){
            e.toString();
        }



    }
}
