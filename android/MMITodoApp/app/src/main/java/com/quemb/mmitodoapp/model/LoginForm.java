package com.quemb.mmitodoapp.model;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.annotations.Expose;
import com.quemb.mmitodoapp.R;
import com.quemb.mmitodoapp.validator.PasswordValidator;
import com.quemb.qmbform.annotation.FormElement;
import com.quemb.qmbform.annotation.validators.EmailValidator;
import com.quemb.qmbform.descriptor.RowDescriptor;

/**
 * Created by tonimockel on 19.06.16.
 */

public class LoginForm {

    private static final String SP_KEY_EMAIL = "SP_KEY_EMAIL";
    private static final String SP_KEY_PASSWORD = "SP_KEY_PASSWORD";

    @Expose
    @FormElement(rowDescriptorType = RowDescriptor.FormRowDescriptorTypeEmailInline, validatorClasses = EmailValidator.class, label = R.string.label_email, tag = "email", sortId = 0, hint = R.string.hint_email)
    public String email = "s@bht.de";

    @Expose
    @FormElement(rowDescriptorType = RowDescriptor.FormRowDescriptorTypePasswordInline, validatorClasses = PasswordValidator.class, label = R.string.label_password, tag = "pwd", sortId = 1)
    public String pwd = "000000";


    public void save(Context context) {

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(SP_KEY_EMAIL, email);
        editor.putString(SP_KEY_PASSWORD, pwd);
        editor.apply();

    }
}
