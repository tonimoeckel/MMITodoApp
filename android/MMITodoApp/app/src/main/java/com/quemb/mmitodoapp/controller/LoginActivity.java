package com.quemb.mmitodoapp.controller;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ListActivity;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.quemb.mmitodoapp.R;
import com.quemb.mmitodoapp.model.LoginForm;
import com.quemb.qmbform.FormManager;
import com.quemb.qmbform.OnFormRowClickListener;
import com.quemb.qmbform.annotation.FormDescriptorAnnotationFactory;
import com.quemb.qmbform.descriptor.FormDescriptor;
import com.quemb.qmbform.descriptor.FormItemDescriptor;
import com.quemb.qmbform.descriptor.OnFormRowValueChangedListener;
import com.quemb.qmbform.descriptor.RowDescriptor;
import com.quemb.qmbform.descriptor.RowValidationError;
import com.quemb.qmbform.descriptor.SectionDescriptor;
import com.quemb.qmbform.descriptor.Value;

import java.lang.reflect.Field;
import java.util.List;


/**
 * Created by tonimockel on 19.06.16.
 */

public class LoginActivity extends ListActivity implements OnFormRowValueChangedListener, OnFormRowClickListener {

    private static final String TAG = "LoginActivity";
    private LoginForm mLoginForm;
    private FormManager mFormManager;
    private FormDescriptor mFormDescriptor;
    private TextView mValidationTextView;
    private ProgressBar mProgressBar;

    private RowDescriptor mButtonDescriptor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        mValidationTextView = (TextView) findViewById(R.id.validationTextView);
        mProgressBar = (ProgressBar) findViewById(android.R.id.progress);

        setupForm();
    }

    private void setupForm(){

        mLoginForm = new LoginForm();

        FormDescriptorAnnotationFactory factory = new FormDescriptorAnnotationFactory(this);
        mFormDescriptor = factory.createFormDescriptorFromAnnotatedClass(mLoginForm);

        SectionDescriptor buttonSection = SectionDescriptor.newInstance("button");
        mButtonDescriptor = RowDescriptor.newInstance("login", RowDescriptor.FormRowDescriptorTypeButton, getString(R.string.label_login));
        mButtonDescriptor.setDisabled(true);
        buttonSection.addRow(mButtonDescriptor);
        mFormDescriptor.addSection(buttonSection);

        mFormManager = new FormManager();
        mFormManager.setup(mFormDescriptor, getListView(), this);
        mFormManager.setOnFormRowValueChangedListener(this);
        mFormManager.setOnFormRowClickListener(this);

    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public void onValueChanged(RowDescriptor rowDescriptor, Value<?> oldValue, Value<?> newValue) {


        if (rowDescriptor.isValid()){

            try {
                Field field = mLoginForm.getClass().getField(rowDescriptor.getTag());
                field.set(mLoginForm, newValue.getValue());

            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        List<RowValidationError> valdationErrors = mFormDescriptor.getFormValidation(this)
                .getRowValidationErrors();
        showValidationText(valdationErrors);
        mButtonDescriptor.setDisabled(valdationErrors.size() > 0);
        mFormManager.updateRows();

    }

    private void showValidationText(List<RowValidationError> validationErrors) {

        if (validationErrors.size() > 0){
            if (mValidationTextView.getVisibility() != View.VISIBLE){
                mValidationTextView.setVisibility(View.VISIBLE);
            }

            RowValidationError rowValidationError = validationErrors.get(0);
            mValidationTextView.setText(rowValidationError.getMessage(this));
        }else {
            hideValidationText();
        }

    }

    private void hideValidationText() {
        mValidationTextView.setVisibility(View.INVISIBLE);
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

        Log.d(TAG, "processLogin");

    }
}
