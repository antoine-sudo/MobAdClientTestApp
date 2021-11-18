package com.example.mobadclienttestapp_java;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;

import com.example.mobadclienttestapp_java.Interests.Interests;
import com.example.mobadclienttestapp_java.Languages.AdsLanguages;
import com.example.mobadclienttestapp_java.Utils.MyCallback;
import com.imagineworks.mobad_sdk.MobAd;

public class MainActivity extends AppCompatActivity {

    TextView greeting,interests,ad_languages;
    Toolbar toolbar;
    SwitchCompat showAddsSwitch;
    EditText numberOfAdsEditText;
    MobAdClass mobAdEvents;

    MobAd mobAd;
    Boolean is_initialised=false,ads_enabled;
    RelativeLayout number_of_ads;
    Button update,request_ad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registered);

        Bundle bundle = getIntent().getExtras();
        String email = bundle.getString("email");

        number_of_ads = findViewById(R.id.numberOfAdsLayout);
        update = findViewById(R.id.updateButton);
        request_ad = findViewById(R.id.request_ad);

        //Initialize the toolbar
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(0xFFFFFFFF);
        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        assert ab != null;

        greeting = findViewById(R.id.registeredTextView);
        greeting.setText(getString(R.string.You_have_successfully_registered_with_this__s,email));

        showAddsSwitch = findViewById(R.id.showAddsSwitch);


        //this.startMobAd(email);
        startMobAd(email, created -> is_initialised=created);

        if(is_initialised){
            ads_enabled = mobAd.getAdsEnabled();
        }

        if(ads_enabled){
            showAddsSwitch.setChecked(true);
            number_of_ads.setVisibility(View.VISIBLE);
            update.setVisibility(View.VISIBLE);

        }
        else{
            showAddsSwitch.setChecked(false);
            number_of_ads.setVisibility(View.GONE);
            update.setVisibility(View.GONE);
        }

        showAddsSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked){
                mobAd.setAdsEnabled(true); //False to disable;
                number_of_ads.setVisibility(View.VISIBLE);
                update.setVisibility(View.VISIBLE);
            }
            else{
                mobAd.setAdsEnabled(false); //False to disable;
                number_of_ads.setVisibility(View.GONE);
                update.setVisibility(View.GONE);
            }
        });

        numberOfAdsEditText = findViewById(R.id.numberOfAdsEditText);
        int number_of_ads = 1;
        if(is_initialised){
            number_of_ads = mobAd.getMaximumAdsPerDay();
        }
        numberOfAdsEditText.setText(String.valueOf(number_of_ads));

        update.setOnClickListener(view -> {
            if( numberOfAdsEditText.getText().toString().isEmpty()|| Integer.parseInt(numberOfAdsEditText.getText().toString())<=0){
                numberOfAdsEditText.setText("1");
            }
            mobAd.setMaximumAdsPerDay(Integer.parseInt(numberOfAdsEditText.getText().toString()), new MobAd.SetMaxAdsPerDayCompletionListener() {
                @Override
                public void onSuccess(boolean limitExceeded, int newMaxAdsPerDayValue) {
                    if(limitExceeded){
                        Toast.makeText(getApplicationContext(),"You can receive only "+newMaxAdsPerDayValue,Toast.LENGTH_SHORT).show();
                        numberOfAdsEditText.setText(String.valueOf(newMaxAdsPerDayValue));
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"Number of ads updated.",Toast.LENGTH_SHORT).show();
                    }
                    Log.i("MobAd", "Number of ads updated");
                }
                @Override
                public void onError(int code, @Nullable String message) {
                    Toast.makeText(getApplicationContext(),"Failed to update number of ads.",Toast.LENGTH_SHORT).show();
                    Log.i("MobAd", "Failed to update number of ads");
                }
            });

        });

        //Request button for Interstitial Ad
        request_ad.setOnClickListener(v -> {
            mobAd.requestInterstitialAd(0);
            Log.i("MobAd", "Interstitial Ad Requested");
        });

        //To fire up the Interests Activity
        interests = findViewById(R.id.preferencesBox);
        interests.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, Interests.class);
            startActivity(intent);

        });

        //To fire up the Languages Activity
        ad_languages = findViewById(R.id.adLanguagesBox);
        ad_languages.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AdsLanguages.class);
            startActivity(intent);

        });

    }

    //Function to initialize the MobAd SDK and check for permissions
    private void startMobAd(String email, MyCallback callback) {
        Log.i("MobAd", "MainActivity Started");

        mobAd = new MobAd(this);

        mobAd.checkMobAdPermissionsAndStartService(this,null);

        boolean is_it = mobAd.hasReadPhoneStatePermission();

        mobAd.startMobAdService(this,is_it,null);

        mobAd.initializeUser(email, null, null, new MobAd.InitializeCompletionListener() {

            @Override
            public void onSuccess() {
                is_initialised=true;
                Log.i("MobAd", "User initialized successfully.");
            }

            @Override
            public void onError(int code, @Nullable String message) {
                is_initialised=false;
                Log.i("MobAd", "User is not initialized.");
            }
        });


        callback.onCreated(true);

        mobAdEvents = new MobAdClass();
        mobAdEvents.MobAdEvents(this);





    }
}
