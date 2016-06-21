package com.quemb.mmitodoapp.controller;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.app.ListActivity;
import android.content.Intent;
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
import com.quemb.mmitodoapp.application.ApplicationController;
import com.quemb.mmitodoapp.model.LoginForm;
import com.quemb.mmitodoapp.network.ToDoService;
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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


/**
 * Created by tonimockel on 19.06.16.
 */

public class LoginActivity extends ListActivity implements OnFormRowValueChangedListener, OnFormRowClickListener {

    private static final String TAG = "LoginActivity";

    private static final String SECTION_DESCRIPTOR_TAG_LOGIN = "login";
    private static final String SECTION_DESCRIPTOR_TAG_SETTINGS = "settings";

    private static final String ROW_DESCRIPTOR_TAG_LOGIN = "perform_login";
    private static final String ROW_DESCRIPTOR_TAG_CONNECTION = "connection";

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

        SectionDescriptor buttonSection = SectionDescriptor.newInstance(SECTION_DESCRIPTOR_TAG_LOGIN);
        mButtonDescriptor = RowDescriptor.newInstance(ROW_DESCRIPTOR_TAG_LOGIN, RowDescriptor.FormRowDescriptorTypeButton, getString(R.string.label_login));
        mButtonDescriptor.setDisabled(true);
        buttonSection.addRow(mButtonDescriptor);
        mFormDescriptor.addSection(buttonSection);

        SectionDescriptor settingsSection = SectionDescriptor.newInstance(SECTION_DESCRIPTOR_TAG_SETTINGS);
        settingsSection.addRow(RowDescriptor.newInstance(ROW_DESCRIPTOR_TAG_CONNECTION, RowDescriptor.FormRowDescriptorTypeButtonInline, getString(R.string.label_connection_settings)));
        mFormDescriptor.addSection(settingsSection);

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
        if (itemDescriptor.getTag().equals(ROW_DESCRIPTOR_TAG_LOGIN)){

            if (mFormDescriptor.isValid(this)){
                processLogin(mLoginForm);
            }

        }else if (itemDescriptor.getTag().equals(ROW_DESCRIPTOR_TAG_CONNECTION)){

            Intent formIntend = new Intent(this, ConnectionSettingsActivity.class);
            startActivity(formIntend);

        }

    }

    private void startProgress() {

        getProgressBar().setVisibility(View.VISIBLE);

    }

    private void stopProgress() {

        getProgressBar().setVisibility(View.INVISIBLE);

    }

    private ProgressBar getProgressBar(){
        return  (ProgressBar) findViewById(android.R.id.progress);
    }

    private void processLogin(LoginForm loginForm) {


        startProgress();

        ToDoService toDoService = ApplicationController.getSharedInstance().getToDoService();
        Call<Boolean> call = toDoService.authenticate(mLoginForm);
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {

                stopProgress();

                if (response.body()){
                    LoginActivity.this.saveLoginData();
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {

            }
        });

    }

    private void saveLoginData() {

        mLoginForm.save(this);

    }
}
