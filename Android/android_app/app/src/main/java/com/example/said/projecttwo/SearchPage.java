package com.example.said.projecttwo;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SearchPage extends AppCompatActivity {
    String userName;
    JSONObject jObj=new JSONObject();
    List<User> users=new ArrayList<User>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_page);

        final String reqUrl=getResources().getString(R.string.server_url);
        userName=getIntent().getStringExtra("user_name");
        final EditText searchText=(EditText) findViewById(R.id.searchPageText);
        Button btnSearch = (Button) findViewById(R.id.butonSearch);
        ListView listView=(ListView) findViewById(R.id.searchResultListview);

        ImageButton homeButton,searchButton,addButton,profileButton;
        homeButton= (ImageButton) findViewById(R.id.homeButton);
        searchButton= (ImageButton) findViewById(R.id.searchButton);
        addButton= (ImageButton) findViewById(R.id.addButton);
        profileButton= (ImageButton) findViewById(R.id.profileButton);

        //navigation buttons events
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goHomePage=new Intent(SearchPage.this,Anasayfa.class);
                goHomePage.putExtra("user_name",userName);
                startActivity(goHomePage);
            }
        });
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goAddPage=new Intent(SearchPage.this,AddPage.class);
                goAddPage.putExtra("user_name",userName);
                startActivity(goAddPage);
            }
        });
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goProfilPage=new Intent(SearchPage.this,Profil.class);
                goProfilPage.putExtra("user_name",userName);
                startActivity(goProfilPage);
            }
        });

        //search button
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name=searchText.getText().toString();
                try{
                    jObj.put("user_name",name);
                }catch (Exception e){
                    e.printStackTrace();
                }
                new postData(jObj).execute(reqUrl+"/search");
                final ListView listView=(ListView) findViewById(R.id.searchResultListview);
                CustomAdapter adapter=new CustomAdapter(SearchPage.this,users);
                listView.setAdapter(adapter);
            }
        });
    }
    class postData extends AsyncTask<String ,String,String>{
        JSONObject jObj;

        //constructor ile verilerimizi jsonobject objesine atıyoruz
        public postData(JSONObject postDatas) {
            if(postDatas!=null){
                this.jObj=postDatas;
            }
        }

        @Override
        protected String doInBackground(String... strings) {

            HttpConnection connection=new HttpConnection();
            String res=connection.sendDataHttpConnection(strings[0],jObj);
            JsonParser jp=new JsonParser();
            jp.objectParse(res,users);
            return null;
        }
    }
}
