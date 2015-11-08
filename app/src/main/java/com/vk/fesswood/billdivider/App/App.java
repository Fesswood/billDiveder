package com.vk.fesswood.billdivider.App;

import android.app.Application;
import android.content.Context;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmMigration;

/**
 * Created by fesswood on 19.09.15.
 */
public class App extends Application {
    private static Context mGlobalContext;

    public static Context getGlobalContext() {
        return mGlobalContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initRealm();
        mGlobalContext = getApplicationContext();
    }

    private  void  initRealm(){
        RealmConfiguration config = new RealmConfiguration.Builder(this)
                .name("myrealm.realm")
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(config);
    }

    private void resetRealm() {
        RealmConfiguration realmConfig = new RealmConfiguration
                .Builder(this)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.deleteRealm(realmConfig);
    }


    private class MyMigration implements RealmMigration {
        @Override
        public long execute(Realm realm, long version) {
            resetRealm();
            return ++version;
        }
    }
}
