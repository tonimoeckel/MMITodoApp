package com.quemb.mmitodoapp.model;

import org.junit.Test;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

/**
 * Created by tonimockel on 22.06.16.
 */

public class ConnectionSettingTest {

    @Test
    public void should_not_be_valid() throws Exception {

        ConnectionSetting connectionSetting = new ConnectionSetting();
        assertFalse(connectionSetting.isValid());

        connectionSetting.protocol = "http";
        assertFalse(connectionSetting.isValid());

        connectionSetting.port = 80;
        assertFalse(connectionSetting.isValid());

    }

    @Test
    public void should_be_valid() throws Exception {

        ConnectionSetting connectionSetting = new ConnectionSetting();
        connectionSetting.protocol = "http";
        connectionSetting.port = 80;
        connectionSetting.host = "192.168.178.20";
        assertTrue(connectionSetting.isValid());
    }
}
