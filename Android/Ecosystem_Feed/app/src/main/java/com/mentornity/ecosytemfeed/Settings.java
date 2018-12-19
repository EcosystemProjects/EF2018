package com.mentornity.ecosytemfeed;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import com.linkedin.platform.LISessionManager;
import com.mentornity.ecosytemfeed.jsonConnection.FetchData;
import com.onesignal.OneSignal;
import com.onesignal.OneSignalDbHelper;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;
import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class Settings extends Fragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {


    View v;
    Button settingTabButton,aboutTabButton,contactTabButton;
    ImageButton twitterFallowBtn,facebookFallowBtn,linkedlnFallowBtn;
    ToggleButton changeLanguageBtn;
    TextView webSiteTv,mailTv,termTv,logoutTv,userNameTV;
    Switch sendEmailSw, sendNotificationSw;
    RelativeLayout settingsLy,contactLy;
    LinearLayout aboutLy, frontendDevelopersLy,androidDevelopersLy,iosDevelopersLy,backendDevelopersLy;
    ListView language_lists;
    ImageView profileImage;
    //There are three screens here.
    public Settings() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v=inflater.inflate(R.layout.fragment_settings, container, false);

        init(v);
        addDevelopers();

        facebookFallowBtn.setOnClickListener(this);
        twitterFallowBtn.setOnClickListener(this);
        linkedlnFallowBtn.setOnClickListener(this);
        sendEmailSw.setOnCheckedChangeListener(this);
        sendNotificationSw.setOnCheckedChangeListener(this);

        changeLanguageBtn.setOnCheckedChangeListener(this);

        webSiteTv.setOnClickListener(this);
        mailTv.setOnClickListener(this);
        termTv.setOnClickListener(this);
        logoutTv.setOnClickListener(this);

        //SET SWITCHES POSITIONS
        if(OneSignal.getPermissionSubscriptionState().getSubscriptionStatus().getSubscribed()){
            sendNotificationSw.setChecked(true);
        }else{
            sendNotificationSw.setChecked(false);
        }
        if(OneSignal.getPermissionSubscriptionState().getEmailSubscriptionStatus().getSubscribed()){
            sendEmailSw.setChecked(true);
        }else{
            sendEmailSw.setChecked(false);
        }

        userNameTV.setText(getActivity().getSharedPreferences("Login",Context.MODE_PRIVATE).getString("name",null));
        Picasso.get().load(getActivity().getSharedPreferences("Login",Context.MODE_PRIVATE).getString("pic",null)).into(profileImage, new Callback() {
            @Override
            public void onSuccess() {
                Bitmap imageBitmap=((BitmapDrawable)profileImage.getDrawable()).getBitmap();
                RoundedBitmapDrawable roundedBitmapDrawable= RoundedBitmapDrawableFactory.create(getContext().getResources(),imageBitmap);
                roundedBitmapDrawable.setCircular(true);
                roundedBitmapDrawable.setCornerRadius(Math.max(imageBitmap.getWidth(),imageBitmap.getHeight())/2.0f);
                profileImage.setImageDrawable(roundedBitmapDrawable);
            }
            @Override
            public void onError(Exception e) {
                profileImage.setImageResource(R.drawable.app_icon);
            }
        });

        //Changing screens
        settingTabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                settingsLy.setVisibility(View.VISIBLE);
                aboutLy.setVisibility(View.GONE);
                contactLy.setVisibility(View.GONE);
                activateButton(settingTabButton);deactivateButton(aboutTabButton);deactivateButton(contactTabButton);
            }
        });
        aboutTabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                settingsLy.setVisibility(View.GONE);
                aboutLy.setVisibility(View.VISIBLE);
                contactLy.setVisibility(View.GONE);
                deactivateButton(settingTabButton);activateButton(aboutTabButton);deactivateButton(contactTabButton);
            }
        });
        contactTabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                settingsLy.setVisibility(View.GONE);
                aboutLy.setVisibility(View.GONE);
                contactLy.setVisibility(View.VISIBLE);
                deactivateButton(settingTabButton);deactivateButton(aboutTabButton);activateButton(contactTabButton);
            }
        });

        return v;
    }


    private void init(View v) {
        settingTabButton=v.findViewById(R.id.settings_btn);
        aboutTabButton=v.findViewById(R.id.about_btn);
        contactTabButton=v.findViewById(R.id.contact_btn);

        settingsLy= v.findViewById(R.id.settings_ly);
        aboutLy=v.findViewById(R.id.about_ly);
        contactLy=v.findViewById(R.id.contact_ly);
        language_lists=v.findViewById(R.id.languages_listv);


        facebookFallowBtn=v.findViewById(R.id.facebook_fallow_btn);
        twitterFallowBtn=v.findViewById(R.id.twitter_fallow_btn);
        linkedlnFallowBtn=v.findViewById(R.id.linkedin_fallow_btn);
        changeLanguageBtn= v.findViewById(R.id.language_change_ly_btn);

        webSiteTv=v.findViewById(R.id.website_link_tv);
        mailTv=v.findViewById(R.id.mail_link_tv);
        termTv=v.findViewById(R.id.settings_term);
        logoutTv=v.findViewById(R.id.log_out_tv);
        userNameTV=v.findViewById(R.id.settings_name_tv);
        profileImage=v.findViewById(R.id.settings_profile_iv);

        sendEmailSw = v.findViewById(R.id.settings_send_email_switch);
        sendNotificationSw = v.findViewById(R.id.settings_send_notification_switch);

        frontendDevelopersLy =v.findViewById(R.id.settings_frontendDevelopers_ly);
        androidDevelopersLy=v.findViewById(R.id.settings_AndroidDevelopers_ly);
        iosDevelopersLy=v.findViewById(R.id.settings_iosDevelopers_ly);
        backendDevelopersLy=v.findViewById(R.id.settings_backendDevelopers_ly);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.facebook_fallow_btn:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/mentornity")));
                break;
            case R.id.twitter_fallow_btn:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/mentornity")));
                break;
            case R.id.linkedin_fallow_btn:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://tr.linkedin.com/company/mentornity")));
                break;
            case R.id.website_link_tv:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com")));
                break;
            case R.id.mail_link_tv:
                startActivity(new Intent(Intent.ACTION_SEND).setData(Uri.parse("mailto:")).setType("message/rfc222").
                        putExtra(Intent.EXTRA_EMAIL, new String[]{"ef@mentornity.com"}));
                break;
            case R.id.settings_term:
                Intent intent = new Intent(getActivity(), TermsActivity.class);
                startActivity(intent);
                break;
            case R.id.log_out_tv:
                logOut();
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()){
            case R.id.settings_send_email_switch:

                break;
            case R.id.settings_send_notification_switch:
                OneSignal.setSubscription(isChecked);
                break;
            case R.id.language_change_ly_btn:
                if(buttonView.isChecked())
                {
                    language_lists.setVisibility(View.VISIBLE);
                    language_lists.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                            if(position==0)
                                changeLanguage("en","US");
                            else if(position==1)
                                changeLanguage("tr","TR");
                        }
                    });
                }
                else
                {
                    language_lists.setVisibility(View.GONE);
                }
                break;
        }
    }

    public void activateButton(Button b)
    { b.setTextColor(getResources().getColor(R.color.black_text)); }
    public void deactivateButton(Button b)
    { b.setTextColor(getResources().getColor(R.color.grayText)); }


    public void changeLanguage(String language,String region)
    {
        Locale locale;

            locale = new Locale(language,region);
        Locale.setDefault(locale);
        // Create a new configuration object
        Configuration config = new Configuration();
        // Set the locale of the new configuration
        config.locale = locale;
        // Update the configuration of the Accplication context
        getResources().updateConfiguration(
                config,
                getResources().getDisplayMetrics()
        );
        SharedPreferences sharedPreferences=getActivity().getSharedPreferences("SETTINGS",MODE_PRIVATE);
        sharedPreferences.edit().putString("LANGUAGE",language).apply();//After that screen must refresh
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(this).attach(this).commit();
    }

    private void addDevelopers() {
        ViewGroup.LayoutParams layoutParams=new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        TextView alitolakTV=new TextView(getContext());
        alitolakTV.setText("Ali TOLAK");
        TextView beratKara=new TextView(getContext());
        beratKara.setText("Berat KARA");
        backendDevelopersLy.addView(alitolakTV,layoutParams);
        backendDevelopersLy.addView(beratKara,layoutParams);
        TextView berkacikelTV=new TextView(getContext());
        berkacikelTV.setText("Berk AÇIKEL");
        frontendDevelopersLy.addView(berkacikelTV,layoutParams);
        TextView tarikbasoglu=new TextView(getContext());
        tarikbasoglu.setText("Tarık Ramazan BAŞOĞLU");
        TextView edaaydin=new TextView(getContext());
        edaaydin.setText("Eda AYDIN");
        TextView eminKivanc = new TextView(getContext());
        eminKivanc.setText("Emin KIVANÇ");
        androidDevelopersLy.addView(tarikbasoglu,layoutParams);
        androidDevelopersLy.addView(edaaydin,layoutParams);
        androidDevelopersLy.addView(eminKivanc,layoutParams);
        TextView bugraPesman=new TextView(getContext());
        bugraPesman.setText("Ahmet Buğra PEŞMAN");
        TextView damlaKarakulah=new TextView(getContext());
        damlaKarakulah.setText("Damla Karaküllah");
        TextView enverKaan=new TextView(getContext());
        enverKaan.setText("Enver KAAN");
        TextView mansuraminKaya=new TextView(getContext());
        mansuraminKaya.setText("Mansuremin Kaya");

        iosDevelopersLy.addView(bugraPesman,layoutParams);
        iosDevelopersLy.addView(damlaKarakulah,layoutParams);
        iosDevelopersLy.addView(enverKaan,layoutParams);
        iosDevelopersLy.addView(mansuraminKaya,layoutParams);
    }

    public void logOut() {
        LISessionManager.getInstance(this.getActivity().getApplicationContext()).clearSession();
        SharedPreferences sp=this.getActivity().getSharedPreferences("Login", Context.MODE_PRIVATE);
        sp.edit().putBoolean("logged",false).apply();
        startActivity(new Intent(this.getActivity(),Login.class));
    }
}