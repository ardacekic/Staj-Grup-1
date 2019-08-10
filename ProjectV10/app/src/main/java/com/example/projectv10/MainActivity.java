package com.example.projectv10;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    private TextView textViewTitle, textViewForget, textViewSing;
    private EditText editTextUsername, editTextPassword;
    private ImageView imageViewLogo;
    private Button buttonLogin;
    JSONObject json, jobj;
    public String username = "";
    public String password = "";
    public String passwordagain = "";
    public String res;
    String action="";

    String reqUrl = "http://7e0d342d.ngrok.io";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        textViewTitle = findViewById( R.id.textViewtitle );
        textViewForget = findViewById( R.id.textViewForgot );
        textViewSing = findViewById( R.id.textViewSingup );
        editTextPassword = findViewById( R.id.PasswordEdit );
        editTextUsername = findViewById( R.id.UsernameEdit );
        buttonLogin = findViewById( R.id.buttonLogin );

        json = new JSONObject();
        jobj = new JSONObject();

        //final String reqUrl = getResources().getString( R.string.server_url );

        buttonLogin.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                username = editTextUsername.getText().toString();
                password = editTextPassword.getText().toString();
                //Toast.makeText( MainActivity.this, "username : " + username + " Password : " + password,
                        //Toast.LENGTH_SHORT ).show();

                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText( MainActivity.this, "Fill empty fields.", Toast.LENGTH_SHORT ).show();
                } else {
                    try {
                        json.put( "user_name", username );
                        json.put( "password", password );
                        Toast.makeText( MainActivity.this, reqUrl+"/login",
                                Toast.LENGTH_LONG ).show();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                     new postData(json,username).execute(reqUrl+"/login");
                }
            }
        } );

        textViewForget.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText( MainActivity.this, "Forget anything...", Toast.LENGTH_SHORT ).show();

            }
        } );

        textViewSing.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                View alert = getLayoutInflater().inflate( R.layout.activity_alertview, null );
                final EditText editTextusername = alert.findViewById( R.id.usernameedit );
                final EditText editTextpassword = alert.findViewById( R.id.passwordedit );
                final EditText editTextpasswordAgain = alert.findViewById( R.id.passwordagain );


                AlertDialog.Builder ao = new AlertDialog.Builder( MainActivity.this );
                ao.setTitle( "  Add New User: " );
                ao.setIcon( R.drawable.ic_exposure_plus_1_black_24dp );
                ao.setView( alert );

                ao.setPositiveButton( "Sing Up", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        username = editTextusername.getText().toString();
                        password = editTextpassword.getText().toString();
                        passwordagain = editTextpasswordAgain.getText().toString();


                        if (username.isEmpty() || password.isEmpty() || passwordagain.isEmpty()) {
                            Toast.makeText( MainActivity.this, "Fill empty fields.", Toast.LENGTH_SHORT ).show();
                        } else {
                            if (password.equals( passwordagain )) {
                                try {
                                    Toast.makeText( MainActivity.this, "u :" + username + "p: " + password + "pa : " + passwordagain, Toast.LENGTH_SHORT ).show();

                                    json.put( "user_name", username );
                                    json.put( "password", password );
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                new postData(json,username).execute(reqUrl+"/signup");
                            } else {
                                Toast.makeText( MainActivity.this, "invalid password!!", Toast.LENGTH_SHORT ).show();

                            }
                        }

                    }


                } );

                ao.setNegativeButton( "Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText( MainActivity.this, "Canceled", Toast.LENGTH_SHORT ).show();
                    }
                } );

                ao.create().show();
            }
        } );

    }


    public class postData extends AsyncTask<String, String, String> {
        JSONObject jObj;
        String username;
        ProgressDialog pd = new ProgressDialog( MainActivity.this );

        //constructor ile verilerimizi jsonobject objesine atıyoruz
        public postData(JSONObject postDatas, String username) {
            if (postDatas != null) {
                this.jObj = postDatas;
            }
            this.username = username;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd.setMessage( "Please wiat" );
            pd.setTitle( "Content is loading" );
            pd.setIcon( R.drawable.ic_sync_black_24dp  );
            pd.setCancelable( false );
            pd.show();

        }


        @Override
        protected String doInBackground(String... strings) {
            Log.e("STRING",strings[0]);
            Log.e("Name: ",username);
            HttpConnection connection = new HttpConnection();
            res = connection.sendDataHttpConnection(strings[0], jObj );

            if (!res.isEmpty()) {
                Intent login = new Intent( MainActivity.this, Anasayfa.class );
                login.putExtra( "user_name", username );
                startActivity( login );
            } else {
                Log.e("Name: ","Boş döndü");
                return res;
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute( s );
            pd.dismiss();
        }

    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK && isTaskRoot()){
            finish();
        }
        return true;
    }
}

