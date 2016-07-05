package com.quemb.mmitodoapp.controller;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.quemb.mmitodoapp.R;
import com.quemb.mmitodoapp.model.ConnectionSetting;
import com.quemb.mmitodoapp.model.ConnectionSettingFactory;
import com.quemb.mmitodoapp.util.Authentication;
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

import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.util.List;


/**
 * Created by tonimoeckel on 21.06.16.
 */
public class ConnectionSettingsActivity extends AppCompatActivity implements OnFormRowClickListener, OnFormRowValueChangedListener,
        ReachabilityListener {


    private static final java.lang.String SECTION_DESCRIPTOR_TAG_SAVE = "save_section";
    private static final java.lang.String ROW_DESCRIPTOR_TAG_SAVE = "save_row";

    private static final java.lang.String SECTION_DESCRIPTOR_TAG_TEST_CONNECTION = "test_section";
    private static final java.lang.String ROW_DESCRIPTOR_TAG_TEST_CONNECTION = "test_row";
    private static final java.lang.String SECTION_DESCRIPTOR_TAG_SKIP = "SECTION_DESCRIPTOR_TAG_SKIP";
    private static final String ROW_DESCRIPTOR_TAG_SKIP = "ROW_DESCRIPTOR_TAG_SKIP";

    private ConnectionSetting mConnectionSetting;
    private ListView mListView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_progress_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActivityManager activityManager = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.AppTask> tasks = activityManager.getAppTasks();

        if (tasks.size()>1){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mListView = (ListView) findViewById(android.R.id.list);

        mConnectionSetting = ConnectionSettingFactory.getSharedPreferencesSetting(this);

        buildForm();

    }

    private void buildForm() {

        FormDescriptorAnnotationFactory factory = new FormDescriptorAnnotationFactory(this);
        FormDescriptor formDescriptor = factory.createFormDescriptorFromAnnotatedClass(mConnectionSetting);

        SectionDescriptor saveSection = SectionDescriptor.newInstance(SECTION_DESCRIPTOR_TAG_SAVE);
        saveSection.addRow(RowDescriptor.newInstance(ROW_DESCRIPTOR_TAG_SAVE, RowDescriptor.FormRowDescriptorTypeButton, getString(R.string.label_save)));
        formDescriptor.addSection(saveSection);

        SectionDescriptor skipSection = SectionDescriptor.newInstance(SECTION_DESCRIPTOR_TAG_SKIP);
        skipSection.addRow(RowDescriptor.newInstance(ROW_DESCRIPTOR_TAG_SKIP, RowDescriptor.FormRowDescriptorTypeButton, getString(R.string.label_skip)));
        formDescriptor.addSection(skipSection);

        SectionDescriptor testConnectionSection = SectionDescriptor.newInstance(SECTION_DESCRIPTOR_TAG_TEST_CONNECTION);
        testConnectionSection.addRow(RowDescriptor.newInstance(ROW_DESCRIPTOR_TAG_TEST_CONNECTION, RowDescriptor.FormRowDescriptorTypeButtonInline, getString(R.string.label_test_connection)));
        formDescriptor.addSection(testConnectionSection);

        FormManager formManager = new FormManager();
        formManager.setup(formDescriptor, mListView, this);
        formManager.setOnFormRowClickListener(this);
        formManager.setOnFormRowValueChangedListener(this);

    }


    @Override
    public void onFormRowClick(FormItemDescriptor rowDescriptor) {

        if (rowDescriptor.getTag().equals(ROW_DESCRIPTOR_TAG_SAVE)){
            saveConnectionSetting();

            ConnectionSetting connectionSetting = ConnectionSettingFactory.getSharedPreferencesSetting(this);
            if (connectionSetting.isValid()){
                if (Authentication.isAuthenticated()){
                    startActivity(new Intent(this, LoginActivity.class));
                }else {
                    startActivity(new Intent(this, TodoListActivity.class));
                }
                finish();

            }else {
                Toast.makeText(this,getString(R.string.toast_connection_setting_is_unvalid), Toast.LENGTH_SHORT).show();
            }

        }else if (rowDescriptor.getTag().equals(ROW_DESCRIPTOR_TAG_TEST_CONNECTION)){
            performConnectionTest();
        }else if (rowDescriptor.getTag().equals(ROW_DESCRIPTOR_TAG_SKIP)){
            startActivity(new Intent(this, TodoListActivity.class));
            finish();
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
