package com.xtremelabs.droidsugar.fakes;

import android.app.Activity;
import android.app.Application;
import android.content.*;
import com.xtremelabs.droidsugar.DroidSugarAndroidTestRunner;
import com.xtremelabs.droidsugar.util.FakeHelper;
import com.xtremelabs.droidsugar.util.Transcript;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertThat;

@RunWith(DroidSugarAndroidTestRunner.class)
public class ContextWrapperTest {
    public Transcript transcript;

    @Before public void setUp() throws Exception {
        DroidSugarAndroidTestRunner.addGenericProxies();

        FakeHelper.application = new Application();

        transcript = new Transcript();
    }

    @Test
    public void registerReceiver_shouldRegisterForAllIntentFilterActions() throws Exception {
        IntentFilter intentFilter = new IntentFilter("foo");
        intentFilter.addAction("baz");

        ContextWrapper contextWrapper = new ContextWrapper(new Activity());
        contextWrapper.registerReceiver(new BroadcastReceiver() {
            @Override public void onReceive(Context context, Intent intent) {
                transcript.add("notified of " + intent.getAction());
            }
        }, intentFilter);

        contextWrapper.sendBroadcast(new Intent("foo"));
        transcript.assertEventsSoFar("notified of foo");

        contextWrapper.sendBroadcast(new Intent("womp"));
        transcript.assertNoEventsSoFar();

        contextWrapper.sendBroadcast(new Intent("baz"));
        transcript.assertEventsSoFar("notified of baz");
    }

    @Test
    public void shouldReturnSameApplicationEveryTime() throws Exception {
        Activity activity = new Activity();
        assertThat(activity.getApplication(), sameInstance(activity.getApplication()));

        assertThat(activity.getApplication(), sameInstance(new Activity().getApplication()));
    }

    @Test
    public void shouldReturnSameApplicationContextEveryTime() throws Exception {
        Activity activity = new Activity();
        assertThat(activity.getApplicationContext(), sameInstance(activity.getApplicationContext()));

        assertThat(activity.getApplicationContext(), sameInstance(new Activity().getApplicationContext()));
    }

    @Test
    public void shouldReturnSameContentResolverEveryTime() throws Exception {
        Activity activity = new Activity();
        assertThat(activity.getContentResolver(), sameInstance(activity.getContentResolver()));

        assertThat(activity.getContentResolver(), sameInstance(new Activity().getContentResolver()));
    }
}
