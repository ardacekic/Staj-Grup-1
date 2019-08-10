package com.example.said.projecttwo;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Anasayfa extends AppCompatActivity {
    public String datas;
    Double latitude,longitude;

    ImageButton homeButton,searchButton,addButton,profileButton;
    List<User> users=new ArrayList<User>();

    String userName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anasayfa);
        homeButton= (ImageButton) findViewById(R.id.homeButton);
        searchButton= (ImageButton) findViewById(R.id.searchButton);
        addButton= (ImageButton) findViewById(R.id.addButton);
        profileButton= (ImageButton) findViewById(R.id.profileButton);
        userName=getIntent().getExtras().getString("user_name");
        final String reqUrl = getResources().getString(R.string.server_url);
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(getIntent());
            }
        });
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentSearchPage=new Intent(Anasayfa.this,SearchPage.class);
                intentSearchPage.putExtra("user_name",userName);
                startActivity(intentSearchPage);
            }
        });
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentAddPage=new Intent(Anasayfa.this,AddPage.class);
                intentAddPage.putExtra("user_name",userName);
                startActivity(intentAddPage);
            }
        });
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentProfile=new Intent(Anasayfa.this,Profil.class);
                intentProfile.putExtra("user_name",userName);
                startActivity(intentProfile);
            }
        });
        new getData().execute(reqUrl+"/mainpage");

        final ListView listView=(ListView) findViewById(R.id.listView);
        CustomAdapter adapter=new CustomAdapter(Anasayfa.this,users);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String selected=((TextView) view.findViewById(R.id.userName)).getText().toString();
                JSONObject jsonObject =new JSONObject();
                try{
                    jsonObject.put("user_name",selected);
                }catch (Exception e){
                    e.printStackTrace();
                }
                new getGps(jsonObject).execute(reqUrl+"/profil");
                Toast.makeText(Anasayfa.this, selected, Toast.LENGTH_SHORT).show();
            }
        });
    }
    class getData extends AsyncTask<String ,String,String>{
        @Override
        protected String doInBackground(String... strings) {
            HttpConnection connection=new HttpConnection();
            datas=connection.getDataHttpConnection(strings[0]);
            JsonParser jp=new JsonParser();
            jp.objectParse(datas,users);
            return null;
        }
    }
    class getGps extends AsyncTask<String,String,String>{
        JSONObject userName;
        public getGps(JSONObject userName){
            this.userName=userName;
        }
        @Override
        protected String doInBackground(String... strings) {
            HttpConnection connection = new HttpConnection();
            String jObj = connection.sendDataHttpConnection(strings[0], userName);
            if (jObj != null) {
                try {
                    JSONArray contacts = new JSONArray(jObj);
                    JSONObject jsonObj = contacts.getJSONObject(0);
                    latitude = jsonObj.getDouble("latitude");
                    longitude = jsonObj.getDouble("longitude");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Intent goMap=new Intent(Anasayfa.this,MapsActivity.class);
                goMap.putExtra("latitude",latitude);
                goMap.putExtra("longitude",longitude);
                startActivity(goMap);
            }
            return null;
        }
    }
}
