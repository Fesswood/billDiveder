package com.vk.fesswood.billdivider.ui.activity

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import com.vk.fesswood.billdivider.R
import com.vk.fesswood.billdivider.data.adapter.SumPartsAdapter
import com.vk.fesswood.billdivider.data.model.Contact
import com.vk.fesswood.billdivider.data.model.SumPart
import com.vk.fesswood.billdivider.utils.ContactAccessor
import io.realm.Realm
import kotlinx.android.synthetic.activity_contactv2.civContactImage
import kotlinx.android.synthetic.activity_contactv2.rwBills

public class ContactActivity : BaseActivity(), View.OnClickListener {

    public final val PICK_CONTACT: Int = 2015;
    private val TAG: String = ContactActivity::class.simpleName as String
    private val CONTACT_ID: String = "contact_id"
    private var mResultContactId: Int? = 0;
    private var mAdapter: SumPartsAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contactv2)
        civContactImage.setOnClickListener(this)
        var result = getRealm().where(SumPart::class.java).findAll()
        var layoutManager = LinearLayoutManager(this)
        mAdapter = SumPartsAdapter(this, result, true, true)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        rwBills.layoutManager = layoutManager
        rwBills.adapter = mAdapter;
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.civContactImage ->
                addContact();
            R.id.fabClear ->
                clearAll();
            R.id.fabSelectAll ->
                selectAll();
            R.id.fabOK ->
                clickFinish();
        }
    }

    fun addContact() {
        var i = Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(i, PICK_CONTACT);
    }

    private fun clickFinish() {
        Log.d(TAG, "clickFinish()")
        var i = Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        i.putExtra(CONTACT_ID, mResultContactId as Int);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    private fun selectAll() {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun clearAll() {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == PICK_CONTACT && resultCode == Activity.RESULT_OK) {
            var contactUri: Uri = data?.data as Uri
            var accessor = ContactAccessor()
            var contact = accessor.loadContact(contentResolver, contactUri)
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
}
