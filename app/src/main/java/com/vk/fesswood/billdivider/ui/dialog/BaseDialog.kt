package com.vk.fesswood.billdivider.ui.dialog


import android.content.Context
import android.support.v7.app.AppCompatDialog
import io.realm.Realm

/**
 * Created by fesswood on 20.09.15.
 */
public abstract class BaseDialog(context: Context, style: Int) : AppCompatDialog(context, style) {
    private var mRealm: Realm? = null


    public fun getRealm(): Realm? {
        if (mRealm == null) {
            mRealm = Realm.getDefaultInstance()
        }
        return mRealm
    }

    companion object {
        private val TAG = BaseDialog::class.java.simpleName
    }
}