package iceepotmobile.ui;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by manos on 15/11/2015.
 */
public class SettingsActivity extends Activity {

    public static final String KEY_PREF_HOST = "prefHost";
    public static final String KEY_PREF_PORT = "prefPort";
    public static final String KEY_PREF_TIMEOUT = "prefTimeout";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getFragmentManager().beginTransaction().replace(android.R.id.content, new SettingsFragment()).commit();
    }
}
