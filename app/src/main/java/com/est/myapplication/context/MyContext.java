package com.est.myapplication.context;

import android.app.Application;

import com.est.myapplication.business.Services;
import com.est.myapplication.business.ServicesImpl;
import com.est.myapplication.models.Player;

public class MyContext extends Application {

    private Services services;
    private Player starter;


    @Override
    public void onCreate() {
        super.onCreate();
        services = new ServicesImpl();
    }

    public Player getStarter() {
        return starter;
    }

    public void setStarter(Player starter) {
        this.starter = starter;
    }

    public Services getServices() { return services; }

}
