package com.mentornity.ecosytemfeed;


import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.linkedin.platform.APIHelper;
import com.linkedin.platform.LISessionManager;
import com.linkedin.platform.errors.LIApiError;
import com.linkedin.platform.errors.LIAuthError;
import com.linkedin.platform.listeners.ApiListener;
import com.linkedin.platform.listeners.ApiResponse;
import com.linkedin.platform.listeners.AuthListener;
import com.linkedin.platform.utils.Scope;
import com.mentornity.ecosytemfeed.jsonConnection.FetchData;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.concurrent.DelayQueue;

import okio.Utf8;

public class Login extends AppCompatActivity {
    public static String TAG="Login",FIRST_LOGIN="FIRST LOGIN";
    private  Button loginBtn,browseBtn;
    private int isLoginCode=0;
    SharedPreferences sp;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Log.d(TAG, "onCreate: ");
        if(getSharedPreferences("SETTINGS",MODE_PRIVATE).getString("LANGUAGE",null)==null
                ||getSharedPreferences("SETTINGS",MODE_PRIVATE)==null)
        {
            //do nothing//Eger ilk giris ise dil secilmemistir ve default olarak ingilizce secilir.
        }
        else if(getSharedPreferences("SETTINGS",MODE_PRIVATE).getString("LANGUAGE",null).compareTo("en")==0) {
            LanguageChanger.setLocaleEN(Login.this);
        }
        else if (getSharedPreferences("SETTINGS",MODE_PRIVATE).getString("LANGUAGE",null).compareTo("tr")==0)
            LanguageChanger.setLocaleTR(Login.this);
        Log.d(TAG, "onCreate: Language selected");


        computePackageHash();//to get Key Hash
        sp=getSharedPreferences("Login",MODE_PRIVATE);

        loginBtn=findViewById(R.id.linkedin_login_btn);
        browseBtn=findViewById(R.id.browse_btn);
        browseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(i);
                finish();
            }
        });
        loginBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                handleLogin();
            }
        });


        Log.d(TAG, "onCreate: "+sp.getBoolean("logged",false));
        if(sp.getBoolean("logged",false))
        {//login olunduysa Anasayfaya gider.
            Log.d(TAG, "onCreate: Already Logined,Main activity openning");
            Intent i = new Intent(this,MainActivity.class);
            i.putExtra(FIRST_LOGIN,false);
            startActivity(i);
            finish();
        }
        else Log.d(TAG, "onCreate: login waiting");

    }

    public void gotomainActivity() {
        sp=getSharedPreferences("Login",MODE_PRIVATE);
        sp.edit().putBoolean("logged",true).apply();
        Intent i = new Intent(this,MainActivity.class);
        i.putExtra(FIRST_LOGIN,true);
        startActivity(i);
        finish();

    }


    public void computePackageHash()
    {   //It is used to see keyhash.It is function for developer to use on services that needs it.
        Log.d(TAG, "computePackageHash: computing");
        try
        {
            PackageInfo packageInfo=getPackageManager().getPackageInfo(
                    "com.mentornity.ecosytemfeed",
                    PackageManager.GET_SIGNATURES);
            for(Signature signature:packageInfo.signatures){
                MessageDigest md=MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash",Base64.encodeToString(md.digest(),Base64.DEFAULT));
            }
        }catch (Exception e)
        {
            Log.e("TAG",e.getMessage());
        }
    }
    private void handleLogin()
    {
        //Look https://developer.linkedin.com/docs/signin-with-linkedin
        Log.d(TAG, "handleLogin: handling");
        final CustomProgressDialog dialog = new CustomProgressDialog(this,1);
        LISessionManager.getInstance(getApplicationContext()).init(this, buildScope(), new AuthListener() {
            @Override
            public void onAuthSuccess() {
                // Authentication was successful.  You can now do
                // other calls with the SDK.
                Log.d(TAG, "onAuthSuccess: fetching personal info");
                fetchPersonalInfo();
                dialog.show();
                Handler handler = new Handler();
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        if(isLoginCode!=-1 &&isLoginCode!=0)
                        {   //User can be blocked by system.
                            // -1 banned
                            //0 no login
                            //1 user
                            //2 reader
                            //3 publisher
                            dialog.dismiss();
                            gotomainActivity();
                        }
                        else Toast.makeText(getApplicationContext(),"Login Error",Toast.LENGTH_LONG).show();
                        Log.d(TAG, "onAuthSuccess: info fetched.Returning main activity");
                    }
                };
                handler.postDelayed(runnable,3000);
            }

            @Override
            public void onAuthError(LIAuthError error) {
                // Handle authentication errors
                Log.e("MIKHIL onAuthError:",error.toString());
            }
        }, true);
    }
    private static Scope buildScope() {
        return Scope.build(Scope.R_BASICPROFILE, Scope.W_SHARE,Scope.R_EMAILADDRESS);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Add this line to your existing onActivityResult() method
        LISessionManager.getInstance(getApplicationContext()).onActivityResult(this, requestCode, resultCode, data);
    }

    private void fetchPersonalInfo()
    {//Look https://developer.linkedin.com/docs/signin-with-linkedin
        Log.d(TAG, "fetchPersonalInfo: fetching started");
        String url = "https://api.linkedin.com/v1/people/~:(id,first-name,last-name,public-profile-url,picture-url,email-address,picture-urls::(original))";


        APIHelper apiHelper = APIHelper.getInstance(getApplicationContext());
        apiHelper.getRequest(this, url, new ApiListener() {
            @Override
            public void onApiSuccess(ApiResponse apiResponse) {
                Log.d(TAG, "onApiSuccess: Succesfully connected");// Success!
                try {
                    JSONObject jsonObject = apiResponse.getResponseDataAsJson();
                    System.out.println("RESULT"+jsonObject.toString());
                    final String firstName = jsonObject.getString("firstName");
                    final String lastName = jsonObject.getString("lastName");
                    String pictureUrl = jsonObject.getString("pictureUrl");
                    final String emailAddress = jsonObject.getString("emailAddress");
                    final String linkedinUrl=jsonObject.getString("publicProfileUrl");
                    final String id=jsonObject.getString("id");

                    StringBuilder sb = new StringBuilder();
                    sb.append(firstName);sb.append(" ");sb.append(lastName);

                    sp=getSharedPreferences("Login",MODE_PRIVATE);
                    sp.edit().putString("name",sb.toString()).apply();
                    sp.edit().putString("pic",pictureUrl).apply();
                    sp.edit().putString("mail",emailAddress).apply();

                    //below code for save/check user on database.
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            //baseUrl+query sorgu linkini olusturur.
                            String baseUrl="http://ecosystemfeed.com/Service/Web.php?process=isLogin&json=";
                             String query="{\"json\":{\"emailAddress\":\""
                                    +emailAddress
                                    +"\",\"firstName\":\""+firstName
                                    +"\",\"lastName\":\""+lastName
                                    +"\",\"publicProfileUrl\":\""+linkedinUrl
                                    +"\",\"authId\":\""+id+"\"}}";
                            String charset="UTF-8";
                            try {
                                query=URLEncoder.encode(query,charset);
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                            FetchData fetchData=new FetchData(baseUrl+query);
                            fetchData.execute();
                            Log.d(TAG, "run: fetchdata executing");
                            for(int k=0;k<1;)
                            {
                                if(fetchData.fetched)k++;
                            }
                            try {
                                JSONObject jsonObject1=new JSONObject(fetchData.getData());
                                isLoginCode=jsonObject1.getInt("isLogin");
                                sp.edit().putString("authName",jsonObject1.getString("authName")).apply();
                                sp.edit().putString("sessId",jsonObject1.getString("sessId")).apply();
                                Log.d(TAG, "run: login infos taken.");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            Log.d(TAG, "run: data:"+fetchData.getData());
                            Log.d("this", "onNavigationItemSelected: fetched");
                            Log.d(TAG, "run: dialog dismiss");
                        }
                    }).start();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d(TAG, "onApiSuccess: Error occured"+e.getMessage());
                }
            }

            @Override
            public void onApiError(LIApiError liApiError) {
                // Error making GET request!
                Log.d(TAG, "onApiError: API error occured");
            }
        });
    }
}
