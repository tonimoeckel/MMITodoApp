package com.quemb.qmbform.view;

import com.quemb.qmbform.R;
import com.quemb.qmbform.descriptor.RowDescriptor;

import android.content.Context;

/**
 * Created by tonimoeckel on 15.07.14.
 */
public class FormButtonFieldCell extends FormTitleFieldCell {


    public FormButtonFieldCell(Context context,
                               RowDescriptor rowDescriptor) {
        super(context, rowDescriptor);
    }


    @Override
    protected int getResource() {
        return R.layout.button_field_cell;
    }

    @Override
    protected void update() {
        super.update();

        getTextView().setTextAppearance(getContext(), R.style.TextAppearance_AppCompat_Body2);
    }

    @Override
    public void onCellSelected() {
        super.onCellSelected();


    }
}
