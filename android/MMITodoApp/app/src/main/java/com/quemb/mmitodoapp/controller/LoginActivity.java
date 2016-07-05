package com.quemb.mmitodoapp.controller;

import android.annotation.TargetApi;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.quemb.mmitodoapp.R;
import com.quemb.mmitodoapp.application.ApplicationController;
import com.quemb.mmitodoapp.model.LoginForm;
import com.quemb.mmitodoapp.network.ToDoService;
import com.quemb.mmitodoapp.util.Authentication;
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


/**
 * Created by tonimockel on 19.06.16.
 */

public class LoginActivity extends ListActivity implements OnFormRowValueChangedListener, OnFormRowClickListener {

    private static final String TAG = "LoginActivity";

    private static final String SECTION_DESCRIPTOR_TAG_LOGIN = "login";
    private static final String SECTION_DESCRIPTOR_TAG_SETTINGS = "settings";

    private static final String ROW_DESCRIPTOR_TAG_LOGIN = "perform_login";
    private static final String ROW_DESCRIPTOR_TAG_CONNECTION = "connection";

    private static final String SECTION_DESCRIPTOR_TAG_SKIP = "SECTION_DESCRIPTOR_TAG_SKIP";
    private static final String ROW_DESCRIPTOR_TAG_SKIP = "ROW_DESCRIPTOR_TAG_SKIP";

    private LoginForm mLoginForm;
    private FormManager mFormManager;
    private FormDescriptor mFormDescriptor;
    private TextView mValidationTextView;

    private RowDescriptor mButtonDescriptor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        mValidationTextView = (TextView) findViewById(R.id.validationTextView);

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

        SectionDescriptor settingsSection = SectionDescriptor.newInstance(SECTION_DESCRIPTOR_TAG_SETTINGS, getString(R.string.section_settings));
        settingsSection.addRow(RowDescriptor.newInstance(ROW_DESCRIPTOR_TAG_CONNECTION, RowDescriptor.FormRowDescriptorTypeButtonInline, getString(R.string.label_connection_settings)));
        settingsSection.addRow(RowDescriptor.newInstance(ROW_DESCRIPTOR_TAG_SKIP, RowDescriptor.FormRowDescriptorTypeButton, getString(R.string.label_skip)));
        mFormDescriptor.addSection(settingsSection);



        mFormManager = new FormManager();
        mFormManager.setup(mFormDescriptor, getListView(), this);
        mFormManager.setOnFormRowValueChangedListener(this);
        mFormManager.setOnFormRowClickListener(this);

        getListView().getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                checkValidation();
                getListView().getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

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

        checkValidation();

    }

    private void checkValidation(){

        List<RowValidationError> valdationErrors = mFormDescriptor.getFormValidation(this)
                .getRowValidationErrors();
        showValidationText(valdationErrors);
        setDisabled(valdationErrors.size() > 0);

    }

    private void setDisabled(boolean b) {

        mButtonDescriptor.setDisabled(b);

        // Nur für optische Hervorhebung
        RowDescriptor descriptor = mFormDescriptor.findRowDescriptor(ROW_DESCRIPTOR_TAG_LOGIN);
        View view = descriptor.getCell();
        view.setAlpha(b ? 0.5f : 1.0f);


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
        if (itemDescriptor != null && itemDescriptor.getTag().equals(ROW_DESCRIPTOR_TAG_LOGIN)){

            if (mFormDescriptor.isValid(this)){
                processLogin(mLoginForm);
            }

        }else if (itemDescriptor != null && itemDescriptor.getTag().equals(ROW_DESCRIPTOR_TAG_CONNECTION)){

            Intent formIntend = new Intent(this, ConnectionSettingsActivity.class);
            startActivity(formIntend);

        }else if (itemDescriptor.getTag().equals(ROW_DESCRIPTOR_TAG_SKIP)){

            startActivity(new Intent(this, TodoListActivity.class));
            finish();

        }

    }


    private void processLogin(LoginForm loginForm) {


        final ProgressDialog dialog = ProgressDialog.show(this, getString(R.string.dialog_login),getString(R.string.dialog_please_wait), true);
        dialog.show();

        ToDoService toDoService = ApplicationController.getSharedInstance().getToDoService();
        Call<Boolean> call = toDoService.authenticate(loginForm);
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, final Response<Boolean> response) {


                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        if (response.isSuccessful() && response.body()){
                            LoginActivity.this.saveLoginData();
                            Authentication.setAuthenticated();
                            Toast.makeText(LoginActivity.this, getString(R.string.auth_success), Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(LoginActivity.this, TodoListActivity.class));
                            finish();
                        } else {
                            showLoginUnvalidMessage();
                        }
                    }
                });
                dialog.dismiss();

            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {

                showLoginUnvalidMessage();
                dialog.dismiss();

            }
        });

    }

    private void showLoginUnvalidMessage() {
        mValidationTextView.setText(getString(R.string.login_not_successful));
        mValidationTextView.setVisibility(View.VISIBLE);
        Toast.makeText(LoginActivity.this, getString(R.string.auth_failed), Toast.LENGTH_SHORT).show();
    }

    private void saveLoginData() {

        mLoginForm.save(this);

    }
}
