package com.game.junglelaw;

import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * Created by apple on 11/30/15.
 */
public class SettingsActivity extends PreferenceActivity {

    private static final String LOG_TAG = SettingsActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_general);
    }
}