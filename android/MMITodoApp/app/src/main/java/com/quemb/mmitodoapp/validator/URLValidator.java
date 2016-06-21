package com.quemb.mmitodoapp.validator;

import com.quemb.mmitodoapp.R;
import com.quemb.qmbform.annotation.FormValidator;
import com.quemb.qmbform.descriptor.RowDescriptor;
import com.quemb.qmbform.descriptor.RowValidationError;
import com.quemb.qmbform.descriptor.Value;

/**
 * Created by pmaccamp on 8/26/2015.
 */


public class URLValidator implements FormValidator {
    private static final String PASSWORD_PATTERN = "^[0-9]{6}$";

    @Override
    public RowValidationError validate(RowDescriptor descriptor) {

        RowValidationError result = null;

        Value value = descriptor.getValue();
        if (value.getValue() != null && value.getValue() instanceof String) {
            String val = (String) value.getValue();
            if (!val.matches(PASSWORD_PATTERN)){
                result = new RowValidationError(descriptor, R.string.validation_invalid_password);
            }
        }else {
            result = new RowValidationError(descriptor, R.string.validation_invalid_password);
        }
        return result;
    }
}


