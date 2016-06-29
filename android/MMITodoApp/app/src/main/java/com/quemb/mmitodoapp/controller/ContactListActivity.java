package com.quemb.mmitodoapp.controller;

import com.quemb.mmitodoapp.R;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

/**
 * Created by tonimoeckel on 29.06.16.
 */
public class ContactListActivity extends AppCompatActivity {

    private static final int CONTENT_VIEW_ID = 55555555;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_contact_list);
    }

}
