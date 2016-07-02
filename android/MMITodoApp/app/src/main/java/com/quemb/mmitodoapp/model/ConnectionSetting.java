package com.quemb.mmitodoapp.model;

import com.quemb.mmitodoapp.R;
import com.quemb.qmbform.annotation.FormElement;
import com.quemb.qmbform.annotation.FormElementDelegate;
import com.quemb.qmbform.descriptor.DataSource;
import com.quemb.qmbform.descriptor.DataSourceListener;
import com.quemb.qmbform.descriptor.RowDescriptor;

import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tonimockel on 19.06.16.
 */

public class ConnectionSetting implements FormElementDelegate {

    @FormElement(rowDescriptorType = RowDescriptor.FormRowDescriptorTypeSelectorPickerDialog, required = true, label = R.string.label_protocol, tag = "protocol", sortId = 0, hint = R.string.hint_protocol)
    public String protocol;

    @FormElement(rowDescriptorType = RowDescriptor.FormRowDescriptorTypeText, required = true, label = R.string.label_host, tag = "host", sortId = 5, hint = R.string.hint_host)
    public String host;

    @FormElement(rowDescriptorType = RowDescriptor.FormRowDescriptorTypeInteger, required = true, label = R.string.label_port, tag = "port", sortId = 10, hint = R.string.hint_port)
    public Integer port;


    @Override
    public boolean shouldAddRowDescriptorForField(RowDescriptor rowDescriptor, Field field) {

        if (rowDescriptor.getTag().equals("protocol")){
            rowDescriptor.setDataSource(new DataSource() {
                @Override
                public void loadData(DataSourceListener listener) {
                    List list = new ArrayList();
                    list.add("HTTP");
                    list.add("HTTPS");
                    listener.onDataSourceLoaded(list);
                }
            });
        }

        return true;
    }

    public URL getURL() throws MalformedURLException {
        return new URL(protocol.toLowerCase()+"://"+host+":"+port);
        //return new URL(protocol.toLowerCase(), host, port, null);
    }

    public boolean isValid() {

        return protocol != null && !protocol.isEmpty() && host != null && !host.isEmpty() && port != null && port != 0;
    }
}
