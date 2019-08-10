package com.example.said.projecttwo;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import static android.location.LocationManager.GPS_PROVIDER;
import static android.location.LocationManager.NETWORK_PROVIDER;

public class AddPage extends AppCompatActivity implements LocationListener {

    Button btnAddGpsData, btnAddImage, btnFinalAddButon,btnShowGps;
    ImageButton homeButton,searchButton,addButton,profileButton;
    TextView txtDatasForAdd;

    Location location;
    protected LocationManager locationManager;
    boolean isGpsEnable = false, isNetworkEnable = false;
    static final long min_distance_change_for_update = 1; //1 metre yer değiştirme durumu
    static final long min_time_change_for_update = 1000 * 6; //6000 milisaniyede bir location güncellemesi
    double latitude=0, longitude=0;
    String res;
    String userName;
    JSONObject jObj = new JSONObject();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_page);
        final String reqUrl=getResources().getString(R.string.server_url);  //veri göndereceğimiz sunucu adresi
        userName=getIntent().getStringExtra("user_name");

        btnAddGpsData = (Button) findViewById(R.id.btnAddGpsData);
        btnAddImage = (Button) findViewById(R.id.btnAddImage);
        btnFinalAddButon = (Button) findViewById(R.id.btnFinalAddButton);
        txtDatasForAdd = (TextView) findViewById(R.id.textView4);
        btnShowGps = (Button) findViewById(R.id.btnShowAddButton);

        homeButton= (ImageButton) findViewById(R.id.homeButton);
        searchButton= (ImageButton) findViewById(R.id.searchButton);
        addButton= (ImageButton) findViewById(R.id.addButton);
        profileButton= (ImageButton) findViewById(R.id.profileButton);

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goHome=new Intent(AddPage.this,Anasayfa.class);
                goHome.putExtra("user_name",userName);
                startActivity(goHome);
            }
        });
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goSearch=new Intent(AddPage.this,SearchPage.class);
                goSearch.putExtra("user_name",userName);
                startActivity(goSearch);
            }
        });
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goProfilPage=new Intent(AddPage.this,Profil.class);
                goProfilPage.putExtra("user_name",userName);
                startActivity(goProfilPage);
            }
        });

        //butona tıklandığında gpsten veri alınıyor
        btnAddGpsData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    //gerekli izinler ve servislerin kontrolü
                    locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                    isNetworkEnable = locationManager.isProviderEnabled(NETWORK_PROVIDER);
                    isGpsEnable = locationManager.isProviderEnabled(GPS_PROVIDER);
                    if (isGpsEnable == true && isNetworkEnable == true) {
                        if (ActivityCompat.checkSelfPermission(AddPage.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(AddPage.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            return;
                        }
                        location = locationManager.getLastKnownLocation(GPS_PROVIDER);

                        //gpsten konumu bulabilmek icin istek gönderme
                        locationManager.requestLocationUpdates(GPS_PROVIDER,min_distance_change_for_update,min_time_change_for_update,AddPage.this);

                        if(location != null){
                            latitude=location.getLatitude();
                            longitude=location.getLongitude();
                        }
                    }else{
                        Toast.makeText(AddPage.this, "network or gps service not active", Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

                txtDatasForAdd.setText(String.valueOf(latitude)+"\n"+String.valueOf(longitude));
                txtDatasForAdd.setVisibility(View.VISIBLE);
                btnFinalAddButon.setVisibility(View.VISIBLE);
                btnShowGps.setVisibility(View.VISIBLE);
            }
        });

        btnAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        btnFinalAddButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    //sunucuya gönderilecek verinin json objesine dönüştürülmesi
                    jObj.put("user_name",userName);
                    jObj.put("latitude",latitude);
                    jObj.put("longitude",longitude);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //http bağlantısı ve gönderme işlemi
                new postData(jObj).execute(reqUrl+"/insert");
                Toast.makeText(AddPage.this, res, Toast.LENGTH_SHORT).show();
            }
        });

        btnShowGps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goMap=new Intent(AddPage.this,MapsActivity.class);
                goMap.putExtra("latitude",latitude);
                goMap.putExtra("longitude",longitude);
                startActivity(goMap);
            }
        });
    }
    class postData extends AsyncTask<String,String,String> {
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
            res=connection.sendDataHttpConnection(strings[0],jObj);
            return res;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        latitude=location.getLatitude();
        longitude=location.getLongitude();
        txtDatasForAdd.setText(String.valueOf(latitude)+ "\n"+String.valueOf(longitude));
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
