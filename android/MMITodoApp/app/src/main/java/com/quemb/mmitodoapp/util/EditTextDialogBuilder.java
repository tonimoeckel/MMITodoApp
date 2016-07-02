package com.quemb.mmitodoapp.util;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.quemb.mmitodoapp.R;


/**
 * Created by tonimockel on 02.07.16.
 */

public class EditTextDialogBuilder {

    public AlertDialog.Builder buildDialog(Context context, final EditTextDialogListener successListener){

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle(context.getString(R.string.text_edit_address));
        alertDialog.setMessage(context.getString(R.string.text_enter_your_own_address));

        final EditText input = new EditText(context);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        alertDialog.setView(input);
        alertDialog.setIcon(R.drawable.ic_edit_location_black_24dp);

        alertDialog.setPositiveButton(context.getString(R.string.button_save),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String text = input.getText().toString();
                        if (successListener != null){
                            successListener.onSuccess(text);
                        }
                    }
                });

        alertDialog.setNegativeButton(context.getString(R.string.button_cancel),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        return alertDialog;

    }

    public interface EditTextDialogListener {
        public void onSuccess(String text);
    }

}
