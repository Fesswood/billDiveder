package com.vk.fesswood.billdivider.data.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView

import com.vk.fesswood.billdivider.R
import com.vk.fesswood.billdivider.data.model.SumPart

import io.realm.Realm
import io.realm.RealmBaseAdapter
import io.realm.RealmBasedRecyclerViewAdapter
import io.realm.RealmResults

/**
 * Created by fesswood on 19.09.15.
 */
public class SumPartsAdapter(context: Context, realmResults: RealmResults<SumPart>, automaticUpdate: Boolean, animateResults: Boolean) : RealmBasedRecyclerViewAdapter<SumPart, SumPartsAdapter.ViewHolder>(context, realmResults, automaticUpdate, animateResults) {

    private var mSumPartDeleted: SumPart = SumPart();

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_sum_part, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val sumPart = realmResults.get(position)
        holder.Sum.text = "" + sumPart.value
        holder.Title.text = sumPart.title
        holder.flColor.setBackgroundColor(sumPart.color)
    }

    public inner class ViewHolder(container: View) : RecyclerView.ViewHolder(container) {

        public  val Title: TextView
        public  val Sum: TextView
        public  val flColor: FrameLayout

        init {
            Title = container.findViewById(R.id.textView1) as TextView
            Sum = container.findViewById(R.id.textView2) as TextView
            flColor = container.findViewById(R.id.flColor) as FrameLayout
        }
    }

    public fun initSwipeToDelete(rv :RecyclerView) {
        val mIth = ItemTouchHelper(
                object : ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP, ItemTouchHelper.LEFT) {
                    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                        return false
                    }

                    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                        var realm = Realm.getDefaultInstance()
                        var sumPart =realmResults[viewHolder.adapterPosition];
                        mSumPartDeleted = SumPart();
                        mSumPartDeleted.id = sumPart.id
                        mSumPartDeleted.color = sumPart.color
                        mSumPartDeleted.title = sumPart.title
                        mSumPartDeleted.value = sumPart.value
                        realm.executeTransaction {
                            realmResults.remove(viewHolder.adapterPosition)
                        }
                        realm.close()
                    }

                })
        mIth.attachToRecyclerView(rv)
    }
    public fun getDeletedItems(): SumPart {
        return mSumPartDeleted;
    }
}
