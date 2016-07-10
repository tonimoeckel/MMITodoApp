package com.quemb.mmitodoapp.util;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;

/**
 * Created by tonimockel on 01.07.16.
 */

public class ContactFetcher {


    private static final String TAG = "ContactFetcher";
    private String mName;
    private String mPhone;
    private String mEmail;

    public ContactFetcher(Context context, String contactId){

        Log.d("hier fetch", contactId);
        Cursor cursor = context.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null,
                ContactsContract.Data._ID + " = " + contactId, null, null);
        if (cursor.moveToFirst()){
            mName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
        }else {
            Log.e(TAG, "Contact not found");
        }

        cursor.close();

        Cursor emailCursor = context.getContentResolver().query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, null,
                ContactsContract.Data.CONTACT_ID + " = " + contactId,null, null);
        if (emailCursor.moveToFirst()){
            if (mName == null){
                mName = emailCursor.getString(emailCursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            }
            mEmail = emailCursor.getString(emailCursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
        }else {
            Log.e(TAG, "Email not found");
        }
        emailCursor.close();

        Cursor phoneCursor = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                ContactsContract.Data.CONTACT_ID + " = " + contactId,null, null);
        if (phoneCursor.moveToFirst()){
            mPhone = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DATA));
        }else {
            Log.e(TAG, "Phone not found");
        }
        phoneCursor.close();

    }

    public String getName(){

        return mName; //mCursor.getString(mCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
    }

    public String getPhone(){
        return mPhone; //mCursor.getString(mCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
    }

    public String getEmail(){
        return mEmail; //mCursor.getString(mCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
    }


}
