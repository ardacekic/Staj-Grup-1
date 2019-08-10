package com.example.projectv10;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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
    public ListView listView;
    Double latitude,longitude;
    public List<User> users2=new ArrayList<>();
    ImageButton homeButton,searchButton,addButton,profileButton;
    //public List<User> users2=new ArrayList<>();
    private TextView textView;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        users2.clear();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anasayfa);
        homeButton= (ImageButton) findViewById(R.id.homeButton);
        searchButton= (ImageButton) findViewById(R.id.searchButton);
        addButton= (ImageButton) findViewById(R.id.addButton);
        profileButton= (ImageButton) findViewById(R.id.profileButton);
        textView = findViewById( R.id.Hello );
        textView.setTextSize( 30 );
       // textView.setText("Hello " +getIntent().getStringExtra( "user_name" ) );
        username=getIntent().getExtras().getString("user_name");

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
                intentSearchPage.putExtra("user_name",username);
                startActivity(intentSearchPage);
            }
        });


        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentAddPage=new Intent(Anasayfa.this,AddPage.class);
                intentAddPage.putExtra("user_name",username);
                startActivity(intentAddPage);
            }
        });

        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentProfile=new Intent(Anasayfa.this,Profil.class);
                intentProfile.putExtra("user_name",username);
                startActivity(intentProfile);
            }
        });






        new getData().execute(reqUrl+"/mainpage");
            //Toast.makeText(Anasayfa.this, "hello", Toast.LENGTH_SHORT).show();

        while(users2.size()==0){
            Log.e( "while","while" );

            //Log.e( "muco",);
        }
Log.e( "whiledıs","whiledıs" );
        if(users2.size()>0) {
            listView = (ListView) findViewById( R.id.listView );

            Adapter adapter = new Adapter( Anasayfa.this, users2 );
            listView.setAdapter( adapter );
            listView.setOnItemClickListener( new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    String selected = ((TextView) view.findViewById( R.id.username )).getText().toString();
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put( "user_name", selected );
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    new getGps( jsonObject ).execute( reqUrl + "/profil" );
                    Toast.makeText( Anasayfa.this, selected, Toast.LENGTH_SHORT ).show();
                }
            } );
        }
    }


    class getData extends AsyncTask<String ,String,String> {

        @Override

        protected String doInBackground(String... strings) {
            Log.e( "mesaj : ",  strings[0] );
            HttpConnection connection=new HttpConnection();
            datas=connection.getDataHttpConnection(strings[0]);
            Log.e( "mesaj : ",  datas );
            JsonParser jp=new JsonParser();
            jp.objectParse(datas,users2);


            for (User u: users2){

                Log.e("username:",u.getUsername().toString()+" "+u.getCountOfGps());//kontrol et
            }
            Log.e( "fire", ( users2.get(0).getUsername()));
            return null;
        }
    }

    class getGps extends AsyncTask<String,String,String>{
        JSONObject username;
        public getGps(JSONObject username){
            this.username=username;
        }
        @Override
        protected String doInBackground(String... strings) {
            HttpConnection connection = new HttpConnection();
            String jObj = connection.sendDataHttpConnection(strings[0], username);
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
