package iceepotmobile.ui;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import iceepot.iceepotmobile.R;

/**
 * Created by manos on 15/11/2015.
 */
public class SettingsFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);
    }
}
