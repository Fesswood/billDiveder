package com.vk.fesswood.billdivider.data.model;

import android.graphics.Bitmap;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by fesswood on 03.10.15.
 */
public class Contact extends RealmObject {

    @PrimaryKey
    private String phone;
    private String name;
    private String capName;
    private boolean isMegaLSUser;
    private long photoId;
    private long contactId;
    private int color;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCapName() {
        return capName;
    }

    public void setCapName(String capName) {
        this.capName = capName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isMegaLSUser() {
        return isMegaLSUser;
    }

    public void setIsMegaLSUser(boolean isMegaLSUser) {
        this.isMegaLSUser = isMegaLSUser;
    }

    public void setPhotoId(long photoId) {
        this.photoId = photoId;
    }

    public long getPhotoId() {
        return photoId;
    }

    public void setContactId(long contactId) {
        this.contactId = contactId;
    }

    public long getContactId() {
        return contactId;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
