package com.vk.fesswood.billdivider.data.adapter

import android.content.Context
import android.graphics.drawable.BitmapDrawable
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import com.vk.fesswood.billdivider.App.App
import com.vk.fesswood.billdivider.R
import com.vk.fesswood.billdivider.data.model.Contact
import com.vk.fesswood.billdivider.data.model.SumPart
import com.vk.fesswood.billdivider.utils.ContactAccessor
import io.realm.RealmBasedRecyclerViewAdapter
import io.realm.RealmResults

/**
 * Created by fesswood on 19.09.15.
 */
public class ContactsAdapter(context: Context, realmResults: RealmResults<Contact>, automaticUpdate: Boolean, animateResults: Boolean) : RealmBasedRecyclerViewAdapter<Contact, ContactsAdapter.ViewHolder>(context, realmResults, automaticUpdate, animateResults) {
    var accessor = ContactAccessor()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val contact = realmResults.get(position)
        holder.Sum.text = "" + contact.name
        holder.Title.text = contact.name

        val bitmap = accessor.loadContactPhoto(App.getGlobalContext().contentResolver, contact.contactId, contact.photoId)
        if (bitmap != null) {
            var bitmap = BitmapDrawable(App.getGlobalContext().resources, bitmap)
            holder.flColor.background = bitmap
        } else {
            holder.flColor.setBackgroundColor(contact.color)
        }

    }

    private var mSumPartDeleted: SumPart = SumPart();

    public fun getDeletedItems(): SumPart {
        return mSumPartDeleted;
    }

    public inner class ViewHolder(container: View) : RecyclerView.ViewHolder(container) {

        public val Title: TextView
        public val Sum: TextView
        public val flColor: FrameLayout

        init {
            Title = container.findViewById(R.id.textView1) as TextView
            Sum = container.findViewById(R.id.textView2) as TextView
            flColor = container.findViewById(R.id.flColor) as FrameLayout
        }
    }
}
