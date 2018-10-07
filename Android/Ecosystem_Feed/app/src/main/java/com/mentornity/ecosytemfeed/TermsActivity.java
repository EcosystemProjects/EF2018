package com.mentornity.ecosytemfeed;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class TermsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getSharedPreferences("SETTINGS",MODE_PRIVATE).getString("LANGUAGE",null)==null
                ||getSharedPreferences("SETTINGS",MODE_PRIVATE)==null)
        {
            //do nothing
        }
        else if(getSharedPreferences("SETTINGS",MODE_PRIVATE).getString("LANGUAGE",null).compareTo("en")==0) {
            LanguageChanger.setLocaleEN(TermsActivity.this);
        }
        else if (getSharedPreferences("SETTINGS",MODE_PRIVATE).getString("LANGUAGE",null).compareTo("tr")==0)
            LanguageChanger.setLocaleTR(TermsActivity.this);
        setContentView(R.layout.activity_terms);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this,MainActivity.class));
    }
}
