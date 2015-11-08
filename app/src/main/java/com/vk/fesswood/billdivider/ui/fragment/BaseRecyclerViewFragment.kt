package com.vk.fesswood.billdivider.ui.fragment


import android.app.Activity
import android.os.Bundle
import android.app.Fragment
import android.content.Context
import android.database.DataSetObserver
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.vk.fesswood.billdivider.R
import com.vk.fesswood.billdivider.data.adapter.SumPartsAdapter

import co.moonmonkeylabs.realmrecyclerview.RealmRecyclerView
import com.vk.fesswood.billdivider.data.interaction.SumInteractionListener
import com.vk.fesswood.billdivider.data.model.SumPart
import io.realm.Realm
/**
 * A simple [Fragment] subclass.
 */
public open class BaseRecyclerViewFragment : android.support.v4.app.Fragment() {

    private var mAdapter: SumPartsAdapter? = null
    private var mRealm: Realm? = null;
    private var mColors: IntArray? = null;
    protected  var mChangeListener: SumAdapterChangeListener? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if(context is SumAdapterChangeListener){
            mChangeListener = context
        } else {
            throw IllegalArgumentException("Context must implements SumAdapterChangeListener")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        mColors = activity.resources.getIntArray(R.array.sumcolors)
        mRealm = Realm.getDefaultInstance()

    }


    public fun getRealm():Realm?{
        return mRealm;
    }

    override fun onDestroy() {
        super.onDestroy();
        mRealm?.close();
        mRealm = null;
    }

    fun  getColor(i: Int): Int{
        val count = mColors!!.count()
        var position = i
        if(i > count){
            position -= count
        }
        if(position > 0){
            position -= 1
        }
        return mColors!!.get(position)
    }

    public interface SumAdapterChangeListener{
        public fun DataChanged();
        public fun rowDeleted(deletedRow:SumPart);
    }

}// Required empty public constructor
