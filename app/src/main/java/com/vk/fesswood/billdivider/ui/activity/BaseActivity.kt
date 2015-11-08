package com.vk.fesswood.billdivider.ui.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.vk.fesswood.billdivider.R
import io.realm.Realm

public open class BaseActivity : AppCompatActivity() {

    private var realm: Realm? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)
        realm : Realm = Realm.getDefaultInstance()
    }

    public fun getRealm(): Realm {
        if (realm == null) {
            realm : Realm = Realm.getDefaultInstance()
            return realm!!;
        } else {
            return realm!!;
        }
    }

    override fun onDestroy() {
        realm!!.close();
        super.onStop()
    }
}
