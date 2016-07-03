package com.quemb.mmitodoapp.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.text.TextUtils;


import com.orm.SugarDb;
import com.orm.SugarRecord;
import com.orm.query.Condition;
import com.orm.query.Select;
import com.quemb.mmitodoapp.model.ToDo;

import java.util.List;

public class DataContentProvider extends ContentProvider {

    private static final int TODOS = 1;
    private static final int TODO_ID = 2;

    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    private static final String AUTHORITY = "com.quemb.mmitodoapp.provider";

    static {
        uriMatcher.addURI(AUTHORITY, "todos", TODOS);
        uriMatcher.addURI(AUTHORITY, "todos/#", TODO_ID);
    }


    @Override
    public boolean onCreate() {


        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] columns, String selection, String[] selectionArgs,
        String sortOrder) {

        sortOrder = TextUtils.isEmpty(sortOrder) ? "_ID ASC" : sortOrder;

        List<String> segments;
        segments = uri.getPathSegments();


        switch (uriMatcher.match(uri)) {
            case TODOS:
                return Select.from(ToDo.class).getCursor();
            case TODO_ID:
                assert segments != null;
                return Select.from(ToDo.class).where(Condition.prop("id").eq(selection)).getCursor();
            default:
                throw new RuntimeException("No content provider URI match.");
        }
    }

    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case TODOS:
                return "vnd.android.cursor.dir/vnd.com.quemb.provider.todo";
            case TODO_ID:
                return "vnd.android.cursor.item/vnd.com.quemb.provider.todo";
            default:
                throw new RuntimeException("No content provider URI match.");
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }


}
