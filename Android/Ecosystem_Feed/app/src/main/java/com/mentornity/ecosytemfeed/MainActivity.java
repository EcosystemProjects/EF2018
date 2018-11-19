package com.mentornity.ecosytemfeed;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.mentornity.ecosytemfeed.jsonConnection.FetchData;

public class MainActivity extends AppCompatActivity {
    public String TAG="MainActivity";
    private Boolean guestMode;
    private BottomNavigationView mainNav;
    //private RelativeLayout mainFrame;
    private Feed feedFragment;
    private Regions ecosytemsFragment;
    private Settings settingsFragment;
    private Create_post_fragment create_postFragment;
    private Welcome welcomeFragment;
    private boolean doubleBackTab;
    CustomProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        guestMode=false;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dialog=new CustomProgressDialog(MainActivity.this,1);
        Log.d(TAG, "onCreate: Languages are selecting.");

        if(getSharedPreferences("SETTINGS",MODE_PRIVATE).getString("LANGUAGE",null)==null
                ||getSharedPreferences("SETTINGS",MODE_PRIVATE)==null)
        {
            Log.d(TAG, "onCreate: null language settings"); //do nothing
        }
        else if(getSharedPreferences("SETTINGS",MODE_PRIVATE).getString("LANGUAGE",null).compareTo("en")==0) {
            LanguageChanger.setLocaleEN(MainActivity.this);
        }
        else if (getSharedPreferences("SETTINGS",MODE_PRIVATE).getString("LANGUAGE",null).compareTo("tr")==0)
            LanguageChanger.setLocaleTR(MainActivity.this);
        Log.d(TAG, "onCreate: Language selected.");

        feedFragment=new Feed();
        ecosytemsFragment=new Regions();
        settingsFragment=new Settings();
        create_postFragment=new Create_post_fragment();
        welcomeFragment=new Welcome();


        mainNav=findViewById(R.id.main_nav);

        mainNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.nav_feed:
                        if(!guestMode)
                        {
                            Log.d(TAG, "onNavigationItemSelected: Feed selected");
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Log.d(TAG, "run: dialog showing");
                                            dialog.show();//Hata burada olabilir
                                        }
                                    });
                                    FetchData fetchData=new FetchData("http://ecosystemfeed.com/Service/Web.php?process=getPostsFollow&authid="+
                                    getSharedPreferences("Login",MODE_PRIVATE).getString("sessId",null));
                                    fetchData.execute();
                                    for(int k=0;k<1;)
                                    {
                                        if(fetchData.fetched)k++;
                                    }
                                    Log.d(TAG, "run: executed");
                                    Bundle bundle=new Bundle();
                                    bundle.putString("FEED",fetchData.getData());
                                    feedFragment.setArguments(bundle);
                                    Log.d("this", "onNavigationItemSelected: fetched");
                                    dialog.dismiss();
                                    Log.d(TAG, "run: dialog dismiss");
                                    setFragment(feedFragment);
                                }
                            }).start();
                            //"http://ecosystemfeed.com/Service/Web.php?process=GetPostsFollow&authid="+userauthId
                        }
                        return true;
                    case R.id.nav_ecosystem:
                        Log.d("this", "onNavigationItemSelected: ecosystem selected");
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Log.d(TAG, "run: dialog showing");
                                        dialog.show();//Hata burada olabilir
                                        Log.d(TAG, "run: dialogg");
                                    }
                                });
                                FetchData fetchData=new FetchData("http://ecosystemfeed.com/Service/Web.php?process=getRegion");Log.d(TAG, "run: FetchData varable created");
                                fetchData.execute();
                                Log.d(TAG, "run: fetchdata executing");
                                for(int k=0;k<1;)
                                {
                                    if(fetchData.fetched)k++;
                                }
                                Log.d(TAG, "run: "+fetchData.fetched+" "+fetchData.getFetched());
                                Log.d(TAG, "run: executed");
                                Bundle bundle=new Bundle();
                                bundle.putString("region",fetchData.getData());
                                ecosytemsFragment.setArguments(bundle);
                                Log.d("this", "onNavigationItemSelected: fetched");
                                dialog.dismiss();
                                Log.d(TAG, "run: dialog dismiss");
                                setFragment(ecosytemsFragment);
                            }
                        }).start();
                        return true;
                    case R.id.nav_settings:
                        if(!guestMode)setFragment(settingsFragment);
                        return true;
                    case R.id.nav_create_post:
                        if(!guestMode)setFragment(create_postFragment);
                        return true;
                        default:
                            //You can set Toast message.
                            return false;
                }

            }
        });

        if (!getSharedPreferences("Login",MODE_PRIVATE).getBoolean("logged",false))
        {
            //setFragment(ecosytemsFragment);
            guestMode=true;
            mainNav.setSelectedItemId(R.id.nav_ecosystem);
            Log.d(TAG, "onCreate: ecosystem fragment opened");
        }
        else if(getIntent().getBooleanExtra(Login.FIRST_LOGIN,false))
        {//If first login
            setFragment(welcomeFragment);
            Log.d(TAG, "onCreate: welcome fragment opened");
        }else
        {//otherwise
            mainNav.setSelectedItemId(R.id.nav_ecosystem);
        }

    }
    private void setFragment(Fragment fragment){
        clearBackStack();
        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_frame,fragment);
        fragmentTransaction.commit();
    }
    private void clearBackStack(){
        FragmentManager fm = getSupportFragmentManager();
        fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager=getSupportFragmentManager();
        int count=fragmentManager.getBackStackEntryCount();
        if(count>0)//İf user is using inner screens back button works,otherwise ask to close app.
            super.onBackPressed();
        else
        {
            if(doubleBackTab)
                super.onBackPressed();
            else
            {
                Toast.makeText(this,"Çıkmak için geri tuşuna iki defa basınız.",Toast.LENGTH_SHORT).show();
                doubleBackTab=true;
                Handler handler=new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        doubleBackTab=false;
                    }
                },500);
            }
        }



    }
}
