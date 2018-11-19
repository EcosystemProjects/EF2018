package com.mentornity.ecosytemfeed;

import android.app.Application;
import android.content.SharedPreferences;
import android.util.Log;

import com.onesignal.OneSignal;

import net.gotev.uploadservice.UploadService;

import io.intercom.android.sdk.Intercom;
import io.intercom.android.sdk.identity.Registration;

public class CustomApplication extends Application {
    //https://documentation.onesignal.com/docs/android-sdk-setup
    //https://developers.intercom.com/installing-intercom/docs/android-configuration
    //This application uses onesignal and intercom services.
    @Override
    public void onCreate() {
        super.onCreate();

        //Upload service Initialization
        UploadService.NAMESPACE = BuildConfig.APPLICATION_ID;

        // OneSignal Initialization
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();
        SharedPreferences sp=getSharedPreferences("Login",MODE_PRIVATE);
        String mail=sp.getString("mail",null);
        if(mail!=null)
        {
            OneSignal.setEmail(mail);
            Log.d("CustomApplication", "onCreate: OneSignal "+mail+" added");
        }

        Intercom.initialize(this, "928af204e6d5918375aea2d46a5f53c670619813", "rn9o19e1");
        if (sp.getBoolean("logged",false)) {
            /* We're logged in, we can register the user with Intercom */
            //!!!!LOOK HERE:  it must be user id.It can be an email adress or twitterID.Change it.
            Registration registration = Registration.create().withUserId("123456");
            Intercom.client().registerIdentifiedUser(registration);
        } else {
            /* Since we aren't logged in, we are an unidentified user.
             * Let's register. */
            Intercom.client().registerUnidentifiedUser();
        }
    }

}
