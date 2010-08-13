package com.xtremelabs.droidsugar.fakes;

import android.content.Intent;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import com.xtremelabs.droidsugar.DroidSugarAndroidTestRunner;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.Serializable;

@RunWith(DroidSugarAndroidTestRunner.class)
public class IntentTest extends TestCase {

    @Before
    public void setUp() throws Exception {
        DroidSugarAndroidTestRunner.addProxy(Intent.class, FakeIntent.class);
    }

    @Test
    public void testStringExtra() throws Exception {
        Intent intent = new Intent();
        intent.putExtra("foo", "bar");

        FakeIntent fakeIntent = (FakeIntent) DroidSugarAndroidTestRunner.proxyFor(intent);
        assertEquals("bar", fakeIntent.extras.get("foo"));
    }

    @Test
    public void testIntExtra() throws Exception {
        Intent intent = new Intent();
        intent.putExtra("foo", 2);
        FakeIntent fakeIntent = (FakeIntent) DroidSugarAndroidTestRunner.proxyFor(intent);
        assertEquals(2, fakeIntent.extras.get("foo"));
        assertEquals(2, fakeIntent.getIntExtra("foo", -1));
    }

    @Test
    public void testSerializableExtra() throws Exception {
        Intent intent = new Intent();
        TestSerializable serializable = new TestSerializable("some string");
        intent.putExtra("foo", serializable);
        FakeIntent fakeIntent = (FakeIntent) DroidSugarAndroidTestRunner.proxyFor(intent);
        assertEquals(serializable, fakeIntent.extras.get("foo"));
        assertNotSame(serializable, fakeIntent.extras.get("foo"));
        assertEquals(serializable, fakeIntent.getSerializableExtra("foo"));
        assertNotSame(serializable, fakeIntent.getSerializableExtra("foo"));
    }

    @Test
    public void testParcelableExtra() throws Exception {
        Intent intent = new Intent();
        Parcelable parcelable = new TestParcelable();
        intent.putExtra("foo", parcelable);
        FakeIntent fakeIntent = (FakeIntent) DroidSugarAndroidTestRunner.proxyFor(intent);
        assertSame(parcelable, fakeIntent.extras.get("foo"));
        assertSame(parcelable, fakeIntent.getParcelableExtra("foo"));
    }

    @Test
    public void testLongExtra() throws Exception {
        Intent intent = new Intent();
        intent.putExtra("foo", 2L);
        assertEquals(2L, ((FakeIntent) DroidSugarAndroidTestRunner.proxyFor(intent)).extras.get("foo"));
    }

    @Test
    public void testHasExtra() throws Exception {
        Intent intent = new Intent();
        intent.putExtra("foo", "");
        assertTrue(intent.hasExtra("foo"));
        assertFalse(intent.hasExtra("bar"));
    }

    @Test
    public void testGetActionReturnsWhatWasSet() throws Exception {
        Intent intent = new Intent();
        intent.setAction("foo");
        assertEquals("foo", intent.getAction());
    }

    @Test
    public void testSetData() throws Exception {
        Intent intent = new Intent();
        Uri uri = Uri.parse("content://this/and/that");
        Intent returnedIntent = intent.setData(uri);

        FakeIntent fakeIntent = (FakeIntent) DroidSugarAndroidTestRunner.proxyFor(intent);
        assertSame(uri, fakeIntent.data);
        assertSame(intent, returnedIntent);
    }

    @Test
    public void testSetClassName() throws Exception {
        Intent intent = new Intent();
        Class<? extends IntentTest> thisClass = getClass();
        intent.setClassName("package.name", thisClass.getName());
        FakeIntent fakeIntent = (FakeIntent) DroidSugarAndroidTestRunner.proxyFor(intent);
        assertSame(thisClass, fakeIntent.componentClass);
        assertEquals("package.name", fakeIntent.componentPackageName);
    }

    private static class TestSerializable implements Serializable {
        private String someValue;

        public TestSerializable(String someValue) {
            this.someValue = someValue;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            TestSerializable that = (TestSerializable) o;

            if (someValue != null ? !someValue.equals(that.someValue) : that.someValue != null) return false;

            return true;
        }

        @Override
        public int hashCode() {
            return someValue != null ? someValue.hashCode() : 0;
        }
    }

    private class TestParcelable implements Parcelable {
        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
        }
    }
}
