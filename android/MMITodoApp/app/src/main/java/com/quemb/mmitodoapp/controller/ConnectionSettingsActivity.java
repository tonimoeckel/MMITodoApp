package com.quemb.mmitodoapp.controller;

import com.quemb.mmitodoapp.R;
import com.quemb.mmitodoapp.model.ConnectionSetting;
import com.quemb.mmitodoapp.model.ConnectionSettingFactory;
import com.quemb.qmbform.FormManager;
import com.quemb.qmbform.OnFormRowClickListener;
import com.quemb.qmbform.annotation.FormDescriptorAnnotationFactory;
import com.quemb.qmbform.descriptor.FormDescriptor;
import com.quemb.qmbform.descriptor.FormItemDescriptor;
import com.quemb.qmbform.descriptor.OnFormRowValueChangedListener;
import com.quemb.qmbform.descriptor.RowDescriptor;
import com.quemb.qmbform.descriptor.SectionDescriptor;
import com.quemb.qmbform.descriptor.Value;
import com.quemb.reachability.Reachability;
import com.quemb.reachability.ReachabilityListener;
import com.quemb.reachability.ReachabilityStatus;

import android.annotation.TargetApi;
import android.app.ListActivity;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.net.MalformedURLException;


/**
 * Created by tonimoeckel on 21.06.16.
 */
public class ConnectionSettingsActivity extends ListActivity implements OnFormRowClickListener, OnFormRowValueChangedListener,
        ReachabilityListener {


    private static final java.lang.String SECTION_DESCRIPTOR_TAG_SAVE = "save_section";
    private static final java.lang.String ROW_DESCRIPTOR_TAG_SAVE = "save_row";

    private static final java.lang.String SECTION_DESCRIPTOR_TAG_TEST_CONNECTION = "test_section";
    private static final java.lang.String ROW_DESCRIPTOR_TAG_TEST_CONNECTION = "test_row";

    private ConnectionSetting mConnectionSetting;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_progress_list);

        mConnectionSetting = ConnectionSettingFactory.getSharedPreferencesSetting(this);

        buildForm();

    }

    private void buildForm() {

        FormDescriptorAnnotationFactory factory = new FormDescriptorAnnotationFactory(this);
        FormDescriptor formDescriptor = factory.createFormDescriptorFromAnnotatedClass(mConnectionSetting);

        SectionDescriptor saveSection = SectionDescriptor.newInstance(SECTION_DESCRIPTOR_TAG_SAVE);
        saveSection.addRow(RowDescriptor.newInstance(ROW_DESCRIPTOR_TAG_SAVE, RowDescriptor.FormRowDescriptorTypeButton, getString(R.string.label_save)));
        formDescriptor.addSection(saveSection);

        SectionDescriptor testConnectionSection = SectionDescriptor.newInstance(SECTION_DESCRIPTOR_TAG_TEST_CONNECTION);
        testConnectionSection.addRow(RowDescriptor.newInstance(ROW_DESCRIPTOR_TAG_TEST_CONNECTION, RowDescriptor.FormRowDescriptorTypeButtonInline, getString(R.string.label_test_connection)));
        formDescriptor.addSection(testConnectionSection);

        FormManager formManager = new FormManager();
        formManager.setup(formDescriptor, getListView(), this);
        formManager.setOnFormRowClickListener(this);
        formManager.setOnFormRowValueChangedListener(this);

    }


    @Override
    public void onFormRowClick(FormItemDescriptor rowDescriptor) {

        if (rowDescriptor.getTag().equals(ROW_DESCRIPTOR_TAG_SAVE)){
            saveConnectionSetting();
        }else if (rowDescriptor.getTag().equals(ROW_DESCRIPTOR_TAG_TEST_CONNECTION)){
            performConnectionTest();
        }

    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public void onValueChanged(RowDescriptor rowDescriptor, Value<?> oldValue, Value<?> newValue) {

        try {

            Field field = mConnectionSetting.getClass().getField(rowDescriptor.getTag());
            field.set(mConnectionSetting, newValue.getValue());


        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }


    }

    private void saveConnectionSetting() {

        ConnectionSettingFactory.putSharedPreferencesSetting(mConnectionSetting, this);

    }

    private void performConnectionTest() {

        startProgress();
        try {

            Reachability reachability = new Reachability(this, mConnectionSetting.getURL());
            reachability.checkReachability(this);

        } catch (MalformedURLException e) {
            e.printStackTrace();
            stopProgress();
        }

    }

    private void startProgress() {

        findViewById(R.id.progressContainer).setVisibility(View.VISIBLE);

    }

    private void stopProgress() {

        findViewById(R.id.progressContainer).setVisibility(View.INVISIBLE);

    }

    private ProgressBar getProgressBar(){
        return  (ProgressBar) findViewById(android.R.id.progress);
    }

    @Override
    public void onReachabilityChecked(ReachabilityStatus status) {

        stopProgress();

        Toast toast;
        if (status.equals(ReachabilityStatus.Reachable)){
            toast = Toast.makeText(this, getString(R.string.txt_reachable), Toast.LENGTH_SHORT);

        }else {
            toast = Toast.makeText(this, getString(R.string.txt_not_reachable), Toast.LENGTH_SHORT);
        }

        toast.show();

    }
}
