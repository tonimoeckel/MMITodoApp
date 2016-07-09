package com.quemb.qmbform.view;

import com.quemb.qmbform.descriptor.RowDescriptor;
import com.quemb.qmbform.descriptor.Value;

import android.content.Context;

import java.text.DateFormat;
import java.util.Date;

/**
 * Created by tonimoeckel on 15.07.14.
 */
public class FormTimeFieldCell extends FormDateFieldCell {

    public FormTimeFieldCell(Context context,
                             RowDescriptor rowDescriptor) {
        super(context, rowDescriptor);
    }

    public void onDateChanged(Date date) {

        updateDateLabel(date);
        onValueChanged(new Value<Date>(date));

    }


    @Override
    protected void updateDateLabel(Date date) {

        DateFormat dateFormat = android.text.format.DateFormat.getTimeFormat(getContext());
        String s = dateFormat.format(date);
        getDetailTextView().setText(s);

    }

}
