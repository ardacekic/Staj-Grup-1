package com.example.projectv10;

import android.util.Log;

import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

public class HttpConnection {


    public String getDataHttpConnection(String reqUrl){
        String data="";

        try {
            URL url=new URL(reqUrl);
            HttpURLConnection connection=(HttpURLConnection) url.openConnection();
            connection.connect();

            //okuma islemi icin stream olusturulması
            InputStream is=connection.getInputStream();
            BufferedReader reader=new BufferedReader(new InputStreamReader(is));
            String line;
            while((line=reader.readLine())!=null){
                data+=line;
                Log.e( "text: ",data );
            }

        } catch (MalformedURLException e) {
            Log.e( "Mesaj","Error1" );
            e.printStackTrace();
        } catch (IOException e)
        {
            Log.e( "Mesaj","Error2" );
            e.printStackTrace();
        }
        return data;
    }

    public String sendDataHttpConnection(String reqUrl, JSONObject jObj){

        String toplam="";
        try {
            Log.e("Name: ","girdi");

            //belirtigimiz adres ile baglantı kurulumu
            URL url=new URL(reqUrl);
            HttpURLConnection connection=(HttpURLConnection) url.openConnection();

            //baglantı zaman asımı
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(10000);

            //baglantıyı veri göndermeye acma
            connection.setDoInput(true);
            connection.setDoOutput(true);

            //gonderecegimiz veri turunu ve methodun tanıtımı
            connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
            connection.setRequestMethod("POST");

            //gonderme islemi icin stream olusturma
            OutputStream os=connection.getOutputStream();
            BufferedWriter writer=new BufferedWriter(new OutputStreamWriter(os,"UTF-8"));
            writer.write(getPostDataString(jObj));
            writer.flush();
            writer.close();
            os.close();


            //suncudan donen cevabın alınması
            InputStream is=connection.getInputStream();
            BufferedReader read=new BufferedReader(new InputStreamReader(is));
            String line;
            while((line=read.readLine())!=null){
                toplam+=line;
            }
            Log.e("Name: ",reqUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.e( "toplam",toplam );
        return toplam;
    }

    //json olarak olusturdugumuz objeyi göndermek icin x-www-form-urlencoded formatına donusturuyoruz
    public String getPostDataString(JSONObject params) throws Exception {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        Iterator<String> itr = params.keys();
        while(itr.hasNext()){
            String key= itr.next();
            Object value = params.get(key);
            if (first) {
                first = false;
            }else {
                result.append("&");
            }
            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));
        }
        return result.toString();
    }
}

