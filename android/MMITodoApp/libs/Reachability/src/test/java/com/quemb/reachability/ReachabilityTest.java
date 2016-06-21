package com.quemb.reachability;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.net.URL;
import java.util.concurrent.CountDownLatch;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 19)
public class ReachabilityTest {

    @Test
    public void should_be_reachable() throws Exception {


        final CountDownLatch signal = new CountDownLatch(1);

        ConnectTask task = new ConnectTask();
        task.setListener(new ReachabilityListener() {
            @Override
            public void onReachabilityChecked(ReachabilityStatus status) {
                assertTrue(status == ReachabilityStatus.Reachable);
                signal.countDown();
            }
        });
        task.execute(new URL("http://httpstat.us/200"));

        signal.await();

    }

    @Test
    public void should_reachable_but_server_error() throws Exception {


        final CountDownLatch signal = new CountDownLatch(1);

        ConnectTask task = new ConnectTask();
        task.setListener(new ReachabilityListener() {
            @Override
            public void onReachabilityChecked(ReachabilityStatus status) {
                assertTrue(status == ReachabilityStatus.RemoteError);
                signal.countDown();
            }
        });
        task.execute(new URL("http://httpstat.us/500"));

        signal.await();

    }

    @Test
    public void should_timeout() throws Exception {


        final CountDownLatch signal = new CountDownLatch(1);

        ConnectTask task = new ConnectTask();
        task.setTimeout(500);
        task.setListener(new ReachabilityListener() {
            @Override
            public void onReachabilityChecked(ReachabilityStatus status) {
                assertTrue(status == ReachabilityStatus.Timeout);
                signal.countDown();
            }
        });
        task.execute(new URL("http://192.168.178.122"));

        signal.await();

    }

    @Test
    public void should_timeout_server() throws Exception {


        final CountDownLatch signal = new CountDownLatch(1);

        ConnectTask task = new ConnectTask();
        task.setTimeout(500);
        task.setListener(new ReachabilityListener() {
            @Override
            public void onReachabilityChecked(ReachabilityStatus status) {
                assertTrue(status == ReachabilityStatus.Timeout);
                signal.countDown();
            }
        });
        task.execute(new URL("http://httpstat.us/524"));

        signal.await();

    }
}