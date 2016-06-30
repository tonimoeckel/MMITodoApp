package com.quemb.mmitodoapp.util;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

/**
 * Created by tonimockel on 01.07.16.
 */

public class ContactFetcher {


    private final Cursor mCursor;

    public ContactFetcher(Context context, Uri contactUri){

        String[] projection = {
                ContactsContract.Contacts._ID,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
        };
        mCursor = context.getContentResolver().query(contactUri, projection,
                null, null, null);
        mCursor.moveToFirst();

    }

    public String getName(){
        return mCursor.getString(mCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
    }

    public String getPhone(){
        return mCursor.getString(mCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
    }

    public String getEmail(){
        return mCursor.getString(mCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
    }

    public Cursor getCursor(){
        return mCursor;
    }

}
