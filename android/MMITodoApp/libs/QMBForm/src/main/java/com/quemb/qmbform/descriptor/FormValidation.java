package com.quemb.qmbform.descriptor;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tonimoeckel on 01.12.14.
 */
public class FormValidation {
    private Context mContext;

    public FormValidation(Context context) {
        mContext = context;
    }

    private List<RowValidationError> mRowValidationErrors = new ArrayList<RowValidationError>();

    public List<RowValidationError> getRowValidationErrors() {
        return mRowValidationErrors;
    }

    public List<String> getRowValidationErrorsAsStrings() {
        ArrayList<String> errors = new ArrayList<String>();
        for (RowValidationError error : mRowValidationErrors) {
            errors.add(error.getMessage(mContext));
        }

        return errors;
    }

    public boolean isValid() {

        return getRowValidationErrors().size() == 0;
    }

    public RowValidationError getFirstValidationError() {
        if (getRowValidationErrors().size() > 0) {
            return getRowValidationErrors().get(0);
        }

        return null;
    }
}
