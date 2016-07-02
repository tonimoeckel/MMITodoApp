package com.quemb.mmitodoapp.controller;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.quemb.mmitodoapp.R;

/**
 * Created by tonimoeckel on 29.06.16.
 */
public class SelectContactListActivity extends AppCompatActivity {

    private static final int CONTENT_VIEW_ID = 55555555;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_contact_list);
    }

}
