package com.example.mobadclienttestapp_java

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.imagineworks.mobad_sdk.MobAd

class MobAdClass {
     fun MobAdEvents(context: Context) {
        Log.i("MobAd", "MainActivity Started")


        val mobAd = MobAd(context)


        mobAd.onAdOpenedState(isAdOpened = {
                adOpened, isAdOpened ->
            if (isAdOpened == adOpened){
                Log.i("TAG", "MobAdEvents: open")
            }
        })
        mobAd.onAdClosedState(isOnAdClosed = {
                adClosed, isOnAdClosed ->
            if (isOnAdClosed == adClosed){
                Log.i("TAG", "MobAdEvents: close")
            }
        })
         mobAd.onInterstitialAdFailedToLoadState(isOnInterstitialAdFailedToLoad = {
                 onInterstitialAdFailedToLoad, isOnInterstitialAdFailedToLoad ->
             if (isOnInterstitialAdFailedToLoad== onInterstitialAdFailedToLoad){
                 //Your Code
                 Log.i("TAG", "MobAdEvents: Failed")
             }
         })








    }

}