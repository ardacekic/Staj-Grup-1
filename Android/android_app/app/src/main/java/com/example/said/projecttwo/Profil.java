package com.example.said.projecttwo;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Profil extends AppCompatActivity {

    String userName;
    double latitude ,longitude;
    JSONObject jObj=new JSONObject();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

        ImageButton homeButton,searchButton,addButton,profileButton;
        Button btnLastLocation=(Button) findViewById(R.id.btnUserGpsDatas);
        Button btnLastImage=(Button) findViewById(R.id.btnUserImageDatas);
        Button btnSetting=(Button) findViewById(R.id.btnSetting);
        Button btnExit=(Button) findViewById(R.id.btnExit);

        homeButton= (ImageButton) findViewById(R.id.homeButton);
        searchButton= (ImageButton) findViewById(R.id.searchButton);
        addButton= (ImageButton) findViewById(R.id.addButton);
        profileButton= (ImageButton) findViewById(R.id.profileButton);

        userName=getIntent().getStringExtra("user_name");
        final String reqUrl = getResources().getString(R.string.server_url);
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goHome=new Intent(Profil.this,Anasayfa.class);
                goHome.putExtra("user_name",userName);
                startActivity(goHome);
            }
        });
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goSearch=new Intent(Profil.this,SearchPage.class);
                goSearch.putExtra("user_name",userName);
                startActivity(goSearch);
            }
        });
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goAddPage=new Intent(Profil.this,AddPage.class);
                goAddPage.putExtra("user_name",userName);
                startActivity(goAddPage);
            }
        });

        TextView txtUser=(TextView) findViewById(R.id.txtUserName);
        txtUser.setText(userName);
        btnLastLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    jObj.put("user_name",userName);
                }catch (Exception e){
                    e.printStackTrace();
                }
                new getGps(jObj).execute(reqUrl+"/profil");
            }
        });
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goLogin=new Intent(Profil.this,LoginPage.class);
                startActivity(goLogin);
            }
        });
    }
    class getGps extends AsyncTask<String,String,String> {
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
                    Intent goMap=new Intent(Profil.this,MapsActivity.class);
                    goMap.putExtra("latitude",latitude);
                    goMap.putExtra("longitude",longitude);
                    startActivity(goMap);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
    }
}
