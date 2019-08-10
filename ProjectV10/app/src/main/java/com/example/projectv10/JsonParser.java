package com.example.projectv10;

import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class JsonParser {
    Double latitude,longitude;
    String toplam;

    //overloading islemi yapılacak
    public String objectParse(String jObj, List<User> users){
        if(jObj!=null){
            try{
                JSONArray contacts=new JSONArray(jObj);
                for(int i=0;i<contacts.length();i++) {
                    JSONObject jsonObj = contacts.getJSONObject(i);
                    String username = jsonObj.getString("user_name");
                    String countGps=jsonObj.getString("count");
                    int count = Integer.parseInt(countGps);
                    users.add(new User(username, count));
                }

            } catch (JSONException e) {
                Log.e( "mesaj: ","error jp" );
                e.printStackTrace();
            }
        }

        return toplam;
    }
}
