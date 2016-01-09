package com.vk.fesswood.billdivider.ui.activity

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import com.vk.fesswood.billdivider.R
import com.vk.fesswood.billdivider.data.adapter.BillsAdapter
import com.vk.fesswood.billdivider.data.adapter.SumPartsAdapter
import com.vk.fesswood.billdivider.data.model.Contact
import com.vk.fesswood.billdivider.data.model.SumPart
import com.vk.fesswood.billdivider.utils.ContactAccessor
import com.vk.fesswood.billdivider.utils.GUIUtils
import io.realm.Realm
import kotlinx.android.synthetic.activity_contactv2.*

public class ContactActivity : BaseActivity(), View.OnClickListener {

    private val PERMISSIONS_REQUEST_READ_CONTACTS: Int = 0
    public final val PICK_CONTACT: Int = 2015;
    private val TAG: String = ContactActivity::class.simpleName as String
    private val CONTACT_ID: String = "contact_id"
    private var mResultContactId: Int? = 0;
    private var mAdapter: BillsAdapter? = null
    private var mIsContactExist:Boolean? = false
    private var mContact: Contact? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contactv2)
        civContactImage.setOnClickListener(this)
        var result = getRealm().where(SumPart::class.java).findAll()
        var layoutManager = LinearLayoutManager(this)
        mAdapter = BillsAdapter(this, result, true, true)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        rwBills.layoutManager = layoutManager
        rwBills.adapter = mAdapter;
        getRealm().addChangeListener {
            var sum : Double = 0.0
            mContact?.sumParts?.forEach { sum+=it.value }
            tvSum.text = sum.toString()
        }
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
        // Here, thisActivity is the current activity
        if (checkSelfPermission(
                Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            if (shouldShowRequestPermissionRationale(
                    Manifest.permission.READ_CONTACTS)) {
                startContactPicker()
            } else {
                var perms: Array<String> = arrayOf(Manifest.permission.READ_CONTACTS)
                requestPermissions(
                        perms,
                        PERMISSIONS_REQUEST_READ_CONTACTS);
            }
        }else{
            startContactPicker()
        }
    }

    private fun startContactPicker() {
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
            var bitmap = accessor.loadContactPhoto(contentResolver, contact.contactId, contact.photoId)
            Realm.getDefaultInstance().executeTransaction {
                it.copyToRealmOrUpdate(contact)
            }
            Log.d(TAG, "user ${contactToString(contact)} ")
            etName.setText(contact.name)
            etPhone.setText(contact.phone)
            Log.d(TAG,"start save the contact to db")
            getRealm().executeTransaction {
                mContact = it.copyToRealmOrUpdate(contact)
                mAdapter?.setContact(mContact)
            }
            Log.d(TAG,"finish save the contact to db")
            if (!contact.photoId.equals(0)) {
                civContactImage.setImageBitmap(bitmap)
            }
            bitmap.recycle();
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>?, grantResults: IntArray?) {
        when (requestCode) {
            PERMISSIONS_REQUEST_READ_CONTACTS -> {
                if (grantResults!!.size > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startContactPicker()
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {
                    GUIUtils.showSnackbar(fabFinish, R.string.cant_do_with_out_perms)
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
        }
    }


    fun contactToString(contact: Contact): String {
        return " ${contact.contactId} ${contact.capName} ${contact.name} ${contact.phone} ${contact.photoId}"
    }
}
