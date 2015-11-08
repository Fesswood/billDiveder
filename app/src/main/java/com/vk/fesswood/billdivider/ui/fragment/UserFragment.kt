package com.vk.fesswood.billdivider.ui.fragment


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import co.moonmonkeylabs.realmrecyclerview.RealmRecyclerView
import com.vk.fesswood.billdivider.R
import com.vk.fesswood.billdivider.data.adapter.SumPartsAdapter
import com.vk.fesswood.billdivider.data.model.Contact
import com.vk.fesswood.billdivider.data.model.SumPart
import com.vk.fesswood.billdivider.ui.activity.MainActivity
import com.vk.fesswood.billdivider.utils.ContactAccessor
import io.realm.Realm

/**
 * A simple [Fragment] subclass.
 */
public class UserFragment : BaseRecyclerViewFragment(), MainActivity.OnContactAddListener {


    private var mAdapter: SumPartsAdapter? = null
    private val TAG: String = BillFragment::class.simpleName as String
    public final val PICK_CONTACT: Int = 2015;

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is MainActivity) {
            Log.d(TAG, "onAttach" + context)
            context.setUserAddListener(this)
        }
    }

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
        return view;
    }

    override fun addContact() {
        var i = Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(i, PICK_CONTACT);
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == PICK_CONTACT && resultCode == Activity.RESULT_OK) {
            var contactUri: Uri = data?.data as Uri
            var accessor = ContactAccessor()
            var contact = accessor.loadContact(activity.contentResolver, contactUri)
            //var bimap = accessor.loadContactPhoto(activity.contentResolver,contact.contactId,contact.photoId)
            Realm.getDefaultInstance().executeTransaction {
                it.copyToRealmOrUpdate(contact)
            }
            Log.d(TAG, "user ${contactToString(contact)} ")
        }
    }

    fun contactToString(contact: Contact): String {
        return " ${contact.contactId} ${contact.capName} ${contact.name} ${contact.phone} ${contact.photoId}"
    }
}// Required empty public constructor
