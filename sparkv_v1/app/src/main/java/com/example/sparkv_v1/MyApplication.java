package com.example.sparkv_v1;

import android.app.Application;

import com.google.firebase.FirebaseApp;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // Inicializar Firebase
        FirebaseApp.initializeApp(this);
    }
}