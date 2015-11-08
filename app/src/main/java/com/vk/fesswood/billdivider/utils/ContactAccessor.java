/*******************************************************************************
 * Created by Carlos Yaconi
 * Copyright 2012 Fork Ltd. All rights reserved.
 * License: GPLv3
 * Full license at "/LICENSE"
 ******************************************************************************/
package com.vk.fesswood.billdivider.utils;

import java.io.InputStream;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Contacts;
import android.util.Log;

import com.vk.fesswood.billdivider.data.model.Contact;


/**
 * This implementation has several advantages:
 * <ul>
 * <li>It sees contacts from multiple accounts.
 * <li>It works with aggregated contacts. So for example, if the contact is the result
 * of aggregation of two raw contacts from different accounts, it may return the name from
 * one and the phone number from the other.
 * <li>It is efficient because it uses the more efficient current API.
 * <li>Not obvious in this particular example, but it has access to new kinds
 * of data available exclusively through the new APIs. Exercise for the reader: add support
 * for nickname (see {@link android.provider.ContactsContract.CommonDataKinds.Nickname}) or
 * social status updates (see {@link android.provider.ContactsContract.StatusUpdates}).
 * </ul>
 */
public class ContactAccessor {

    private static final String TAG = ContactAccessor.class.getSimpleName();

    public Intent getPickContactIntent() {
        return new Intent(Intent.ACTION_PICK, Contacts.CONTENT_URI);
    }

    /**
     * Retrieves the contact information.
     */
    public Contact loadContact(ContentResolver contentResolver, Uri contactUri) {
        Contact contactInfo = new Contact();
        long contactId = -1;

        // Load the display name for the specified person
        Cursor cursor = contentResolver.query(contactUri,
                new String[]{Contacts._ID, Contacts.DISPLAY_NAME, Contacts.PHOTO_ID}, null, null, null);
        try {
            if (cursor.moveToFirst()) {
                contactId = cursor.getLong(0);
                contactInfo.setContactId(contactId);
                contactInfo.setName(cursor.getString(1));
                contactInfo.setPhotoId(cursor.getLong(2));
            }
        } finally {
            cursor.close();
        }

        boolean modified = false;
        cursor = contentResolver.query(Phone.CONTENT_URI,
                new String[]{Phone.NUMBER},
                Phone.TYPE + "=" + Phone.TYPE_MOBILE + " and " + Phone.CONTACT_ID + "=" + contactId, null, null);
        try {
            if (cursor.moveToNext()) {
                contactInfo.setPhone(cursor.getString(0));
                modified = true;
            }

        } finally {
            cursor.close();
        }
        if (!modified) {
            // Load the phone number (if any).
            cursor = contentResolver.query(Phone.CONTENT_URI,
                    new String[]{Phone.NUMBER},
                    Phone.CONTACT_ID + "=" + contactId, null, Phone.IS_SUPER_PRIMARY + " DESC");
            try {
                if (cursor.moveToFirst()) {
                    contactInfo.setPhone(cursor.getString(0));
                }
            } finally {
                cursor.close();
            }
        }
        // contactInfo.setPicture(this.loadContactPhoto(contentResolver, contactId, contactInfo.getPhotoId()));
        return contactInfo;
    }

    public Bitmap loadContactPhoto(ContentResolver cr, long id, long photo_id) {
        Uri uri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, id);
        InputStream input = ContactsContract.Contacts.openContactPhotoInputStream(cr, uri);
        if (input != null) {
            return BitmapFactory.decodeStream(input);
        } else {
            Log.d(TAG, "first try failed to load photo");

        }

        byte[] photoBytes = null;

        Uri photoUri = ContentUris.withAppendedId(ContactsContract.Data.CONTENT_URI, photo_id);

        Cursor c = cr.query(photoUri, new String[]{ContactsContract.CommonDataKinds.Photo.PHOTO}, null, null, null);

        try {
            if (c.moveToFirst())
                photoBytes = c.getBlob(0);

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();

        } finally {

            c.close();
        }

        if (photoBytes != null)
            return BitmapFactory.decodeByteArray(photoBytes, 0, photoBytes.length);
        else
            Log.d(TAG, "second try also failed");
        return null;
    }
}