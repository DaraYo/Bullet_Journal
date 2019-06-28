package com.example.bullet_journal.activities;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.MultiSelectListPreference;
import android.preference.Preference;
import android.preference.SwitchPreference;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBar;

import com.example.bullet_journal.R;
import com.example.bullet_journal.helpClasses.LocaleHelper;
import com.example.bullet_journal.helpClasses.PreferencesHelper;

import java.util.HashSet;
import java.util.Locale;

public class SettingsActivity extends AppCompatPreferenceActivity{
    private ListPreference languageSwitch;
    private SwitchPreference themeSwitch;
    private MultiSelectListPreference navigationOptionsView;
    private Locale myLocale;
    private Context context;
    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        context= this;
        super.onCreate(savedInstanceState);
        setLocale(PreferencesHelper.getLanguage(context));
        addPreferencesFromResource(R.xml.preferences);
        getSupportActionBar().setTitle(getString(R.string.action_settings));

        getListView().setPadding(0,0,0,0);
        setupActionBar();


        languageSwitch= (ListPreference)findPreference(this.getResources().getString((R.string.language_setting_key)));
        languageSwitch.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                LocaleHelper.setLocale(context, (String)newValue);
                refreshPage((String)newValue);
                PreferencesHelper.saveLanguage(getApplicationContext(), (String)newValue);
                return true;
            }
        });

        themeSwitch= (SwitchPreference)findPreference(this.getResources().getString(R.string.theme_key));
        themeSwitch.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                PreferencesHelper.saveTheme(getApplicationContext(), (boolean)newValue);
                setTheme(R.style.OurDarkTheme);
                finish();
                startActivity(getIntent());
                return true;
            }
        });

        navigationOptionsView= (MultiSelectListPreference)findPreference(this.getResources().getString(R.string.options_setting_key));
        navigationOptionsView.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                HashSet<String> selectedItems= (HashSet<String>)newValue;
                PreferencesHelper.saveMenuItems(getApplicationContext(), selectedItems);
                return true;
            }
        });
}

    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void refreshPage(String lang) {
        setLocale(lang);
        finish();
        startActivity(getIntent());
    }

    private void setLocale(String language){
        myLocale = new Locale(language);
        Resources res = getResources();
//        DisplayMetrics dm = res.getDisplayMetrics();
//        Configuration conf = res.getConfiguration();
//        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = myLocale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
    }
}
