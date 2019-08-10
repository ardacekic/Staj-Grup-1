package com.example.said.projecttwo;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginPage extends AppCompatActivity {
    EditText editUsername,editPassword,signuserName,signPassword;
    TextView txtForgetPassword,txtSingIn;
    Button btnLogin;
    public String userName="",password="";
    public String res;
    JSONObject json,jobj;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);
        editUsername = (EditText) findViewById(R.id.editUsername);
        editPassword = (EditText) findViewById(R.id.editPassword);
        btnLogin = (Button) findViewById(R.id.button);
        txtForgetPassword = (TextView) findViewById(R.id.textForgetPass);
        txtSingIn = (TextView) findViewById(R.id.textSignUp);

        json = new JSONObject();
        jobj = new JSONObject();

        final String reqUrl = getResources().getString(R.string.server_url);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                userName=editUsername.getText().toString();
                password=editPassword.getText().toString();
                if(userName.isEmpty() || password.isEmpty()){
                    Toast.makeText(LoginPage.this, "Fill empty fields.", Toast.LENGTH_SHORT).show();
                }else {
                    try {
                        json.put("user_name", userName);
                        json.put("password", password);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    new postData(json,userName).execute(reqUrl+"/login");
                }
            }
        });

        //sifre yenileme islemleri
        txtForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(LoginPage.this, "Service not available yet.", Toast.LENGTH_SHORT).show();
            }
        });

        //yeni kayıt işlemleri
        txtSingIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder=new AlertDialog.Builder(LoginPage.this);
                View mView=getLayoutInflater().inflate(R.layout.custom_signin,null);
                Button btnSignin=(Button) mView.findViewById(R.id.butonSign);
                signuserName=(EditText) mView.findViewById(R.id.signinUsername);
                signPassword=(EditText) mView.findViewById(R.id.signinPassword);
                btnSignin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        userName=signuserName.getText().toString();
                        password=signPassword.getText().toString();
                        if(userName.isEmpty() || password.isEmpty()){
                            Toast.makeText(LoginPage.this, "fill empty fields.", Toast.LENGTH_SHORT).show();
                        }else{

                            try{
                                jobj.put("user_name",userName);
                                jobj.put("password",password);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            new postData(jobj,userName).execute(reqUrl+"/signup");
                            if(res==null){
                                Toast.makeText(LoginPage.this, "User already exist", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
                mBuilder.setView(mView);
                AlertDialog dialog=mBuilder.create();
                dialog.show();
            }
        });
    }
    class postData extends AsyncTask<String,String,String>{
        JSONObject jObj;
        String userName;
        //constructor ile verilerimizi jsonobject objesine atıyoruz
        public postData(JSONObject postDatas,String userName) {
            if(postDatas!=null){
                this.jObj=postDatas;
            }
            this.userName=userName;
        }

        @Override
        protected String doInBackground(String... strings) {

            HttpConnection connection = new HttpConnection();
            res = connection.sendDataHttpConnection(strings[0], jObj);
            if (!res.isEmpty()) {
                Intent login = new Intent(LoginPage.this, Anasayfa.class);
                login.putExtra("user_name",userName);
                startActivity(login);
            } else {
                return res;
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
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
