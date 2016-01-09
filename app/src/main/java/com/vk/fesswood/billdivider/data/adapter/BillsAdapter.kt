package com.vk.fesswood.billdivider.data.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.FrameLayout
import android.widget.TextView
import com.vk.fesswood.billdivider.R
import com.vk.fesswood.billdivider.data.model.Contact
import com.vk.fesswood.billdivider.data.model.SumPart
import com.vk.fesswood.billdivider.utils.GUIUtils
import io.realm.Realm
import io.realm.RealmBasedRecyclerViewAdapter
import io.realm.RealmResults

/**
 * Created by fesswood on 19.09.15.
 */
public class BillsAdapter(context: Context, realmResults: RealmResults<SumPart>, automaticUpdate: Boolean, animateResults: Boolean) : RealmBasedRecyclerViewAdapter<SumPart, BillsAdapter.ViewHolder>(context, realmResults, automaticUpdate, animateResults) {

    private var mContact: Contact? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_select_sum_part, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val sumPart = realmResults.get(position)
        holder.tvSum.text = "" + sumPart.value
        holder.tvTitle.text = sumPart.title
        holder.flColor.setBackgroundColor(sumPart.color)

        holder.cbAddToContactSum.setOnCheckedChangeListener { compoundButton, isChecked ->
            if(mContact != null){
                var realm = Realm.getDefaultInstance();
                if(!isChecked){
                    realm.executeTransaction { (mContact as Contact).sumParts.remove(sumPart) }

                }else{
                    realm.executeTransaction { (mContact as Contact).sumParts.add(sumPart) }
                    realm.close()
                }
            }else{
              GUIUtils.showSnackbar(holder.flColor,R.string.choose_contact_first)
            }
        }
    }

    public inner class ViewHolder(container: View) : RecyclerView.ViewHolder(container) {

        public val tvTitle: TextView
        public val tvSum: TextView
        public val cbAddToContactSum: CheckBox
        public val flColor: FrameLayout

        init {
            tvTitle = container.findViewById(R.id.tvTitle) as TextView
            tvSum = container.findViewById(R.id.tvSum) as TextView
            flColor = container.findViewById(R.id.flColor) as FrameLayout
            cbAddToContactSum = container.findViewById(R.id.cbAddToContactSum) as CheckBox
        }
    }


    fun setContact(contact: Contact?) {
        mContact = contact
    }
}
