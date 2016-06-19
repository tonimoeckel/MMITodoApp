package com.quemb.mmitodoapp.controller;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ListActivity;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.ViewTreeObserver;
import android.widget.ListView;

import com.quemb.mmitodoapp.R;
import com.quemb.mmitodoapp.model.LoginForm;
import com.quemb.qmbform.FormManager;
import com.quemb.qmbform.OnFormRowClickListener;
import com.quemb.qmbform.annotation.FormDescriptorAnnotationFactory;
import com.quemb.qmbform.descriptor.FormDescriptor;
import com.quemb.qmbform.descriptor.FormItemDescriptor;
import com.quemb.qmbform.descriptor.OnFormRowValueChangedListener;
import com.quemb.qmbform.descriptor.RowDescriptor;
import com.quemb.qmbform.descriptor.SectionDescriptor;
import com.quemb.qmbform.descriptor.Value;

import java.lang.reflect.Field;


/**
 * Created by tonimockel on 19.06.16.
 */

public class LoginActivity extends ListActivity implements OnFormRowValueChangedListener, OnFormRowClickListener {

    private static final String TAG = "LoginActivity";
    private LoginForm mLoginForm;
    private FormManager mFormManager;
    private FormDescriptor mFormDescriptor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        setupForm();
    }

    private void setupForm(){

        mLoginForm = new LoginForm();

        FormDescriptorAnnotationFactory factory = new FormDescriptorAnnotationFactory(this);
        mFormDescriptor = factory.createFormDescriptorFromAnnotatedClass(mLoginForm);

        SectionDescriptor buttonSection = SectionDescriptor.newInstance("button");
        buttonSection.addRow(RowDescriptor.newInstance("login", RowDescriptor.FormRowDescriptorTypeButton, getString(R.string.label_login)));
        mFormDescriptor.addSection(buttonSection);

        mFormManager = new FormManager();
        mFormManager.setup(mFormDescriptor, getListView(), this);
        mFormManager.setOnFormRowValueChangedListener(this);
        mFormManager.setOnFormRowClickListener(this);

    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public void onValueChanged(RowDescriptor rowDescriptor, Value<?> oldValue, Value<?> newValue) {


        try {
            Field field = mLoginForm.getClass().getField(rowDescriptor.getTag());
            field.set(mLoginForm, newValue.getValue());

        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onFormRowClick(FormItemDescriptor itemDescriptor) {

        //If login button clicked
        if (itemDescriptor.getTag().equals("login")){
            if (mFormDescriptor.isValid(this)){
                processLogin(mLoginForm);
            }
        }

    }

    private void processLogin(LoginForm loginForm) {

    }
}
