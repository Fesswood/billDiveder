package com.vk.fesswood.billdivider.ui.fragment


import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import co.moonmonkeylabs.realmrecyclerview.RealmRecyclerView
import com.vk.fesswood.billdivider.R
import com.vk.fesswood.billdivider.data.adapter.SumPartsAdapter
import com.vk.fesswood.billdivider.data.model.SumPart

/**
 * A simple [Fragment] subclass.
 */
public class BillFragment : BaseRecyclerViewFragment() {

    private var mAdapter: SumPartsAdapter? = null

    private val TAG: String = BillFragment::class.simpleName as String

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {


        var view: View? = inflater!!.inflate(R.layout.fragment_base_recycler_view, container, false)
        var rrvData: RealmRecyclerView = view!!.findViewById(R.id.rrvData) as RealmRecyclerView
        rrvData.setRefreshing(false)
        var layoutManager = LinearLayoutManager(context);
        var result = getRealm()!!.where(SumPart::class.java).findAll()
        mAdapter = SumPartsAdapter(activity, result, true, true)
        layoutManager.orientation = LinearLayoutManager.VERTICAL;
        rrvData.setLayoutManager(layoutManager)
        rrvData.setAdapter(mAdapter)
        mAdapter?.initSwipeToDelete(rrvData.recyclerView)
        mAdapter?.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {

            override fun onChanged() {
                super.onChanged()
                mChangeListener?.DataChanged()
            }

            override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
                super.onItemRangeMoved(fromPosition, toPosition, itemCount)
                Log.d(TAG, "onItemRangeMoved")
                mChangeListener?.DataChanged()
            }

            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                super.onItemRangeInserted(positionStart, itemCount)
                Log.d(TAG, "onItemRangeInserted")
                mChangeListener?.DataChanged()
            }

            override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
                super.onItemRangeRemoved(positionStart, itemCount)
                Log.d(TAG, "onItemRangeRemoved")
                var deletedRow = mAdapter!!.getDeletedItems()
                if (deletedRow.title != null && deletedRow.value != 0.0) {
                    mChangeListener?.rowDeleted(deletedRow)
                }
                mChangeListener?.DataChanged()

            }

            override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
                super.onItemRangeChanged(positionStart, itemCount)
                Log.d(TAG, "onItemRangeChanged")
                mChangeListener?.DataChanged()
            }

            override fun onItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?) {
                super.onItemRangeChanged(positionStart, itemCount, payload)
                Log.d(TAG, "onItemRangeChanged")
                mChangeListener?.DataChanged()
            }
        })
        return view;
    }

}// Required empty public constructor
